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
import hashlib
import itertools
from sentry_sdk import capture_exception, init
from LocationClassifier import *


init(dsn=os.getenv('SENTRY_DSN', 'https://270864132b0845e4a9ae4f68f96c77c2@o434398.ingest.sentry.io/5391423'),
    traces_sample_rate=0.01
)

with open("mock_job_results.json") as mock_job_file:
    MOCK_JOB_RESULTS = json.load(mock_job_file)

class JobFetcher:
    def __init__(self, config, connection, channel, locationClassifier):
        self.config = config
        self.queryTemplate = '{} {} {}'
        self.connection = connection
        self.channel = channel
        self.locationClassifier = locationClassifier

    def __del__(self):
        self.connection.close()

    def enqueue(self, msg):
        payload = json.dumps(msg)
        self.channel.basic_publish(exchange='', routing_key=self.config["queue"]["key"], body=payload)
        logging.info(" sent " + payload)

    def truncateIdAndDescription(self, job, jd_limit=10000, company_name_limit=75):
        if 'job_id' not in job: #https://sentry.io/organizations/kuaidaoresume/issues/1964589073/events/e133f965fce346c5b4a936210f14d1ac/
            job['job_id'] = job['job_link']
        m = hashlib.sha256()
        m.update(job['job_id'].encode('utf-8'))
        job['job_id'] = m.hexdigest()
        job['description'] = job['description'][:jd_limit]
        job['company_name'] = job['company_name'][:company_name_limit]
        return job

    def callJobService(self, endpoint, payload):
        for retry in range(self.config["queue"]["retries"]):
            try:
                headers = {'Authorization': 'kdr-support'}
                r = requests.post(endpoint, json = payload, headers=headers) 
                return r
            except Exception as e:
                capture_exception(e)
                time.sleep(self.config["queue"]["retryWaitSecs"])
        return None    
        
    def getJobPage(self, q):
        params = self.config["serpParams"] 
        params.update({"q": q})

        for pageNum in range(self.config["serpConfig"]["maxResultsPerPage"]):
            params.update({"start": pageNum * self.config["serpConfig"]["maxNumPages"]})

            ret = None, params

            for retry in range(self.config["queue"]["retries"]):
                try:
                    serp_result = GoogleSearchResults(params).get_dict()
                    if serp_result is None: #stop paging if no more results
                        return None, params
                    ret = serp_result, params
                    break
                except Exception as e:
                    capture_exception(e) #capture serp exceptions
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
        for language in self.config["serpQueries"].values():
            for q in itertools.product(*language.values()):
                yield ' '.join(q)

    def processJobResults(self, jobs_results):
        for job in jobs_results:
            if job is None:
                continue
            processedJob = self.callJobService(endpoint="http://job-service/v1/jobs/jobFetcher", payload=self.locationClassifier.updateJobLocation(self.truncateIdAndDescription(job)))
            if processedJob is None or processedJob.status_code != 200 or len(processedJob.content) == 0: #duplicate
                continue
            self.enqueue(processedJob.json())  

    def getJobResults(self, mock = os.getenv('MOCK_JOB_FETCH', True)):
        if mock:
            self.processJobResults(MOCK_JOB_RESULTS)
            return

        for q in self.query():
            logging.info("q = " + q)

            for p, _params in self.getJobPage(q):

                if p is None:
                    break

                jobs_results = self.processJobPage(p, _params)

                if jobs_results is None:
                    break

                self.processJobResults(jobs_results)
                
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
    locationClassifier = LocationClassifier()
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

            j = JobFetcher(config, connection, channel, locationClassifier)

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
        cronJob(j)
        time.sleep(int(os.getenv('JOB_FETCH_EVERY_SECS', j.config["cronjob"]["scheduleInSecs"])))





