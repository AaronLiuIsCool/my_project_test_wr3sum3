#!/usr/bin/env python
# -*- coding: UTF-8 -*-
import csv
import json
import os
import pandas
from pypinyin import lazy_pinyin
import re
import jieba

class LocationClassifier:
    def __init__(self):
        self.dir = "locationData/China"
        self.cities = self.readCsv("cities.csv")
        self.districts = self.readCsv("districts.csv")
        self.provinces = self.readCsv("provinces.csv")

    def readCsv(self, fname):
        dataFrame = pandas.read_csv(os.path.join(self.dir, fname))
        dataFrame['name'] = dataFrame['name'].str.replace("市", "").replace("自治州","").replace("自治县","")
        dataFrame['pinyin'] = dataFrame['name'].apply(self.getPinyin)
        return dataFrame

    def is_contains_chinese(self, strs):
        for _char in strs:
            if '\u4e00' <= _char <= '\u9fa5':
                return True
        return False

    def isCityProvince(self, city):
        """
        判断城市是否为直辖市
        """
        return not self.provinces[self.provinces.name == city.name.values[0]].empty

    def convert(self, s):
        pattern1 = re.compile(r'[^\u4e00-\u9fa5]')
        pattern2 = re.compile(r"\s+")
        clean_s = re.sub(pattern1, '', s)
        clean_s_final = re.sub(pattern2, '', clean_s)
        seg_list = jieba.cut(clean_s_final, cut_all=False)
        return ' '.join(seg_list)

    def prune(self, s):
        if not s:
            return s
        return s.replace("市", "").replace("自治州","").replace("自治县","").replace('(+1 other)','').replace('(+2 others)','').replace('(+3 others)','').replace('(+4 others)','').replace('(+5 others)','')

    def updateJobLocation(self, job):
        if self.is_contains_chinese(json.dumps(job, ensure_ascii=False)):
            self.updateChineseLocation(job)
        else:
            self.updateEnglishLocation(job)
        return job

    def updateEnglishLocation(self, job):
        job['location'] = self.prune(job['location'])
        country = ''
        city = ''
        state = ''
        if 'canada' in job['location'].lower():
            country = 'Canada'
        elif 'anywhere' not in job['location'].lower():
            country = 'USA'
        else:
            return
        arr = [_.strip() for _ in job['location'].split(',')]
        city = arr[0]
        if len(arr) >= 2:
            state = arr[1]
        job['location'] = ','.join([city, state, country])

    def updateChineseLocation(self, job):
        location = {}
        locationStr = self.prune(job['location'])
        city = self.classifyCity(locationStr)
        if city is None:
            city = self.classifyLongString((job['title']))
            if city is None:
                city  = self.classifyLongString((job['description']))
        if city is not None:
            job['location'] = ",".join([city, "中国"])

    def classifyLongString(self, s):
        str_len = [2,3,4,5]
        for l in str_len:
            for i in range(len(s) - l):
                city = self.classifyCity(s[i: i+l])
                return city
        return None

    def classifyCity(self, locationString):
        """
        Rules:
        1. 看名字是否match属于直辖市的区名，这样朝阳不会先match辽宁朝阳，会先找北京，nanjing不会先找漳州的南靖，因为漳州不是直辖市
        2. 如果1没有match再看直接match城市

        corner cases
        https://en.wikipedia.org/wiki/Nanjing_County
        https://en.wikipedia.org/wiki/Chaoyang
        """
        locationString = self.prune(locationString.lower()).replace(",", " ")
        locations = [self.prune(s) for s in locationString.split() if s] #remove empty strings
        # https://stackoverflow.com/questions/26577516/how-to-test-if-a-string-contains-one-of-the-substrings-in-a-list-in-pandas
        district =  self.districts[self.districts.name.str.contains('|'.join(locations)) 
                        | self.districts.pinyin.str.contains('|'.join(locations)) ].head(1)
        if not district.empty: #matched district
            cityForDistrict = self.cities[self.cities.code == district.city_code.values[0]]
            if self.isCityProvince(cityForDistrict):
                return self.postProcessCity(cityForDistrict)

        city = self.cities[self.cities.name.str.contains('|'.join(locations)) 
                        | self.cities.pinyin.str.contains('|'.join(locations)) ].head(1)
        if not city.empty: #matched city
            return self.postProcessCity(city)
        
        return None
        #return None #match不了 #TODO fall back到原来的extraction规则

    def postProcessCity(self, city):
        return city.name.values[0].replace("市", "").replace("自治州","").replace("自治县","")

    def getPinyin(self, chineseString):
        return ''.join(lazy_pinyin(chineseString))

if __name__ == "__main__":
    #test cases
    l = LocationClassifier()
    print(l.classifyCity("nanjing")) #classifier result: 南京市
    print(l.classifyCity("chaoyang")) #classifier reulst: 北京市
    print(l.classifyCity("南京")) #classifier result: 南京市
    print(l.classifyCity("朝阳")) #classifier result: 北京市
    print(l.classifyCity("北京"))  #classifier result: 北京市
    print(l.classifyCity("重庆")) #classifier result: 重庆市
    print(l.classifyCity("Xuhui")) #classifier result: 上海市
    print(l.classifyCity("黄浦区")) #classifier result: 上海市
    print(l.classifyCity("anywhere,,")) #classifier result: 上海市
    print(l.classifyCity("China (+1 other),,Xicheng District"))
    print(l.classifyCity("Cangzhou"))
    j1 = {}
    j1['title'] = '测'
    j1['location'] = "China (+1 other),,Xicheng District"
    #j1['location'] = "中国,北京"

    l.updateJobLocation(j1)
    print(j1)

    j2 = {}
    j2['title'] = "北京新店有点公司"
    j2['location'] = ''
    l.updateJobLocation(j2)
    print(j2)

    j3 = {}
    j3['location'] = "Rimouski, QC, Canada"
    l.updateJobLocation(j3)
    print(j3)

    j4 = {}
    j4['location'] = 'Austin, Texas, United States'
    l.updateJobLocation(j4)
    print(j4)

    j5 = {}
    j5['location'] = 'Tampa, FL, US'
    l.updateJobLocation(j5)
    print(j5)

