#!/usr/bin/env python
# -*- coding: UTF-8 -*-

import os
import json
import time
from serpapi.google_search_results import GoogleSearchResults
from bs4 import BeautifulSoup
import requests
import schedule
import pika 
import logging
from sentry_sdk import capture_exception, init

init(dsn=os.getenv('SENTRY_DSN', 'https://270864132b0845e4a9ae4f68f96c77c2@o434398.ingest.sentry.io/5391423'),
    traces_sample_rate=0.01
)

with open("mock_job_results.json") as mock_job_file:
    MOCK_JOB_RESULTS = json.load(mock_job_file)

class JobFetcher:
    def __init__(self, config, connection, channel):
        self.config = config
        self.queryTemplate = '{} {} {}'
        self.connection = connection
        self.channel = channel

    def __del__(self):
        self.connection.close()

    def enqueue(self, msg):
        payload = json.dumps(msg)
        self.channel.basic_publish(exchange='', routing_key=self.config["queue"]["key"], body=payload)
        logging.info(" sent " + payload)

    def getJobPage(self, q):
        params = self.config["serpParams"] 
        params.update({"q": q})

        for pageNum in range(self.config["serpConfig"]["maxResultsPerPage"]):
            params.update({"start": pageNum * self.config["serpConfig"]["maxNumPages"]})

            ret = None, params

            for retry in range(self.config["queue"]["retries"]):
                try:
                    ret = GoogleSearchResults(params).get_dict(), params
                    break
                except Exception as e:
                    capture_exception(e)
                    time.sleep(self.config["serpConfig"]["waitTimeSec"]** retry) #expo backoff
            yield ret

    def processJobPage(self, page, params):
        if "jobs_results" not in page:
            return None
        jobs_results = page['jobs_results']
        html_link = page["search_metadata"]["raw_html_file"]
        r = requests.get(html_link, params={"api_key":params["api_key"]})
        job_links = self.getJobLinks(r.content)
        for job, link in zip(jobs_results, job_links):
            job.update({"job_link" : link})
        return jobs_results

    def getJobLinks(self, html):
        soup = BeautifulSoup(html, 'lxml') 
        return [span.find("a").get("href") for span in soup.find_all('span', attrs={'class':'DaDV9e'})]

    def query(self):
        for country in self.config["serpQueries"]["countries"]:
            for industry in self.config["serpQueries"]["industries"]:
                for level in self.config["serpQueries"]["levels"]:
                    query = self.queryTemplate.format(country, industry, level)
                    yield query

    def getJobResults(self, mock = os.getenv('MOCK_JOB_FETCH', True)):
        if mock:
            self.enqueue(MOCK_JOB_RESULTS)
            return

        for q in self.query():
            logging.info("q = " + q)

            for p, _params in self.getJobPage(q):

                jobs_results = self.processJobPage(p, _params)
                
                if jobs_results is None:
                    continue
                #for job in jobs_results:
                self.enqueue(jobs_results)
                if len(jobs_results) < self.config["serpConfig"]["maxResultsPerPage"]:
                    break
                time.sleep(self.config["serpConfig"]["waitTimeSec"])

def clearJobs():
    try:
        headers = {'Authorization': 'kdr-support'}
        r = requests.delete("http://job-service/v1/jobs",  headers=headers) 
        logging.info("all jobs deleted")
    except Exception as e:
        logging.info(e)

def createJobFetcher():
    with open("./config.json", "r") as configFile:
        config = json.load(configFile)

    while True:
        try:
            connection = pika.BlockingConnection(
                pika.ConnectionParameters(
                    host=config["queue"]["host"],
                    heartbeat=config["queue"]["heartbeat"],
                    blocked_connection_timeout=config["queue"]["blocked_connection_timeout"]))
            channel = connection.channel()

            channel.queue_declare(queue=config["queue"]["name"])

            j = JobFetcher(config, connection, channel)

            return j

        except Exception as e:
            #capture_exception(e) standard waiting for queue to be ready, not planning to capture this
            time.sleep(config["queue"]["retryWaitSecs"])

def cronJob(jobFetcher):
    #clearJobs()
    time.sleep(jobFetcher.config["cronjob"]["afterClearJobs"])
    jobFetcher.getJobResults()

if __name__ == "__main__":
    j = createJobFetcher()
    while True:
        time.sleep(j.config["cronjob"]["scheduleInSecs"])
        cronJob(j)





