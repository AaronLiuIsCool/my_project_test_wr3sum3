#!/usr/bin/env python
# -*- coding: UTF-8 -*-

import json
import time
import requests
import pika
import logging
import os
from sentry_sdk import capture_exception, init

init(dsn=os.getenv('SENTRY_DSN', 'https://270864132b0845e4a9ae4f68f96c77c2@o434398.ingest.sentry.io/5391423'))

with open("./config.json", "r") as configFile:
    config = json.load(configFile)

while True:
    try:
        connection = pika.BlockingConnection(
            pika.ConnectionParameters(host=config["queue"]["host"]))
        channel = connection.channel()

        channel.queue_declare(queue=config["queue"]["name"])
        break
    except Exception as  e:
        capture_exception(e)
        time.sleep(config["queue"]["retryWaitSecs"])
        logging.info('wait 5 seconds')

def callJobService(job):
    for retry in range(3):
        try:
            headers = {'Authorization': 'Basic'}
            r = requests.post("http://job-service/v1/jobs/jobFetcher", json = job, headers=headers) 
            return r.json()
        except Exception as e:
            capture_exception(e)
            time.sleep(5 ** retry)
    return None
            
def callback(ch, method, properties, body):
    job = json.loads(body)
    print("Received {}".format(job))
    logging.info("Received {}".format(job))
    matching = callJobService(job)
    print("matching", matching)
    # will call matching svc when it's ready

channel.basic_consume(
    queue=config["queue"]["name"], on_message_callback=callback, auto_ack=True)

channel.start_consuming()



