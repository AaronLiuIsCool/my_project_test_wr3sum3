import pymongo
from LocationClassifier import *
import json
import jieba
import re
import requests

"""
for reference only, needed when updating mongo.
not used in fetching process.
"""

def convert(s):
    pattern1 = re.compile(r'[^\u4e00-\u9fa5]')
    pattern2 = re.compile(r"\s+")
    clean_s = re.sub(pattern1, '', s)
    clean_s_final = re.sub(pattern2, '', clean_s)
    seg_list = jieba.cut(clean_s_final, cut_all=False)
    return ' '.join(seg_list)

def printChinesJobs():
    myclient = pymongo.MongoClient("mongodb+srv://root:mongodbroot@kuaidaoresume-uat1.kxcvj.mongodb.net/")
    mydb = myclient["kuaidaoresume-matching"]
    mycol = mydb["jobs"]

    myquery = {"title": {"$regex": '\p{Han}'}}
    
    result = mycol.find(myquery)

    for doc in result:
        print(doc['jobUuid'])

def updateChineseJob():
    myclient = pymongo.MongoClient("mongodb+srv://root:mongodbroot@kuaidaoresume-uat1.kxcvj.mongodb.net/")
    mydb = myclient["kuaidaoresume-matching"]
    mycol = mydb["jobs"]

    myquery = {"title": {"$regex": '\p{Han}'}}
    cnt = 0
    done = 0

    with open("chineseJobUuids.txt") as f:
        for line in f:
            cnt += 1
            uuid = line.strip()
            doc = mycol.find_one({'jobUuid': uuid})
            try:
                newDoc = dict(doc)
                newDoc['titleForSearch'] = convert(doc['title'])
                newDoc['companyNameForSearch'] = convert(doc['companyName'])
                mycol.find_one_and_replace({'jobUuid': uuid}, newDoc)
                done += 1
                print("done = " + str(done))
            except Exception as e:
                print(e)

def printUSCAJobs():
    myclient = pymongo.MongoClient("mongodb+srv://root:mongodbroot@kuaidaoresume-uat1.kxcvj.mongodb.net/")
    mydb = myclient["kuaidaoresume-matching"]
    mycol = mydb["jobs"]

    cnt = 0
    done = 0

    chineseJobUuids = set()

    with open("chineseJobUuids.txt") as f:
        for line in f:
            uuid = line.strip()
            chineseJobUuids.add(uuid)
    for doc in mycol.find():
        if doc['jobUuid'] not in chineseJobUuids:
            print(doc['jobUuid'])
      

def getJob(uuid):
    headers = {'Authorization': 'kdr-support'}
    r = requests.get('http://localhost:8083/v1/jobs/uuid/' + uuid + '?lazy=false', headers=headers) 
    #print(r)
    return r.json()

def updateUSJob():
    myclient = pymongo.MongoClient("mongodb+srv://root:mongodbroot@kuaidaoresume-uat1.kxcvj.mongodb.net/")
    mydb = myclient["kuaidaoresume-matching"]
    mycol = mydb["jobs"]

    myquery = {"title": {"$regex": '\p{Han}'}}
    done = 0

    chineseJobUuids = set()

    with open("uscaJobUuids.txt") as f:
        for line in f:
            uuid = line.strip()
            doc = mycol.find_one({'jobUuid': uuid})
            try:
                newDoc = dict(doc)
                job = getJob(uuid)
                if newDoc['location']['country'] == 'USA':
                    state = job['location']['state']
                    if len(state) > 2 or len(state) == 0:
                        state = job['location']['city']
                        if len(state) > 2 or len(state) == 0:
                            state = job['location']['country']
                    city = job['location']['city']
                    if len(city) == 0 or city == state:
                        city = job['location']['state']
                        if len(city) == 0 or city = state:
                            city = job['location']['country']
                        mycol.find_one_and_replace({'jobUuid': uuid}, newDoc)
                    done += 1
                    print("done = " + str(done))
                #print(job)
                #us = 'united states'
                # if us not in job['location']['country'].lower() and us not in job['location']['city'].lower() and us not in job['location']['state'].lower():
                #     newDoc['location']['country'] = job['location']['country'].replace('(+1 other)', '').strip()
                #     newDoc['location']['city'] = job['location']['city'].replace('(+1 other)', '').strip()
                #     newDoc['location']['state'] = job['location']['state'].replace('(+1 other)', '').strip()
                #     done += 1
                #     print("done = " + str(done))
            except Exception as e:
                print(e)

def updateLocation():
    with open("ids.txt") as f:
        for line in f:
            uuid = line.strip()
            doc = mycol.find_one({'jobUuid': uuid})
            try:
                cnt += 1
                title = doc['title']
                clean_title = re.sub(pattern, '', title)
                print(clean_title)
                seg_list = jieba.cut(clean_title, cut_all=False)
                s = ','.join(seg_list)
                print(s)
                city = l.classifyCity(s)
                print(city)
                if city is not None:
                    done += 1
                    #pass
                    newDoc = dict(doc)
                    newDoc['location'] = {
                            "country":"中国",
                            "city": city,
                            "state": ""
                        }
                    mycol.find_one_and_replace({'jobUuid': uuid}, newDoc)
                    print("============================")
                print("cnt = " + str(cnt) + ", done = " + str(done))
            except Exception as e:
                print(e)


if __name__ == "__main__":
    updateUSJob()
