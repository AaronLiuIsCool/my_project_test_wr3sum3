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

init(dsn=os.getenv('SENTRY_DSN', 'https://80aaf4ae889b414f9fe72e3904cd5246@sentry.io/1380198'))

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

            for retry in range(3):
                try:
                    ret = GoogleSearchResults(params).get_dict(), params
                    break
                except Exception as e:
                    capture_exception(e)
                    time.sleep(1.1 ** retry) #expo backoff
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

    def cronJob(self):
        for q in self.query():
            logging.info("q = " + q)

            for p, _params in self.getJobPage(q):

                jobs_results = self.processJobPage(p, _params)
                
                if jobs_results is None:
                    continue
                for job in jobs_results:
                    self.enqueue(job)
                if len(jobs_results) < self.config["serpConfig"]["maxResultsPerPage"]:
                    break
                time.sleep(self.config["serpConfig"]["waitTimeSec"])

    def cronJobTest(self):
        for q in self.query():
            logging.info("q = " + q)
            msg = self.callJobService(None)
            self.enqueue(q)

if __name__ == "__main__":

    with open("./config.json", "r") as configFile:
        config = json.load(configFile)

    while True:
        try:
            connection = pika.BlockingConnection(
                pika.ConnectionParameters(host=config["queue"]["host"]))
            channel = connection.channel()

            channel.queue_declare(queue=config["queue"]["name"])

            j = JobFetcher(config, connection, channel)

            break

        except Exception as e:
            capture_exception(e)
            time.sleep(config["queue"]["retryWaitSecs"])

    #schedule.every(days).days.at(when).do(j.cronJob)) #TODO use this one when E2E working

    schedule.every(5).seconds.do(j.cronJob)

    while True:
        schedule.run_pending()
        time.sleep(1)





