#!/usr/bin/env python
# -*- coding: UTF-8 -*-

import json
import time
import requests
import logging
import os
from sentry_sdk import capture_exception, init
import asyncio
from aio_pika import connect, IncomingMessage
import aiohttp

init(dsn=os.getenv('SENTRY_DSN', 'https://270864132b0845e4a9ae4f68f96c77c2@o434398.ingest.sentry.io/5391423'))

def callService(endpoint, payload):
    for retry in range(config["queue"]["retries"]):
        try:
            headers = {'Authorization': 'Basic'}
            r = requests.post(endpoint, json = payload, headers=headers) 
            return r.json()
        except Exception as e:
            capture_exception(e)
            time.sleep(config["queue"]["retryWaitSecs"])
    return None    

async def on_message(message: IncomingMessage):
    headers = {'Authorization': 'Basic'}
    jobs = json.loads(message.body)

    for job in jobs:
        processedJob = callService(endpoint="http://job-service/v1/jobs/jobFetcher", payload=job)
        matching = callService(endpoint="http://matching-service/v1/matching/jobs", payload=processedJob)
        print("matching", matching)

async def main(loop, config):
    while True:
        try:
            connection = await connect(config["queue"]["host"], loop=loop)
            headers = {'Authorization': 'Basic'}
            async with aiohttp.ClientSession() as session:
                async with session.get('http://job-service/v1/jobs/1',
                                headers=headers) as resp:
                    if resp.status == 200 or resp.status == 404:
                        break
        except Exception as e:
            capture_exception(e)
            time.sleep(config["queue"]["retryWaitSecs"])

    channel = await connection.channel()

    queue = await channel.declare_queue(config["queue"]["name"])

    await queue.consume(on_message, no_ack=False)

if __name__ == "__main__":
    with open("./config.json", "r") as configFile:
        config = json.load(configFile)
    loop = asyncio.get_event_loop()
    loop.create_task(main(loop, config))
    loop.run_forever()
