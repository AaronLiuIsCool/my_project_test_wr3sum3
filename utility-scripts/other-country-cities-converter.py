import csv
import json

inputCSVFile = open('./data/cn-province-city.csv', 'r')
inputCSVFile2 = open('./data/other-country-cities.csv', 'r')
outputFileName = './data/province-city.json'

fieldnames = ("id","city")
reader = csv.DictReader( inputCSVFile, fieldnames)
rows = list(reader)


fieldnames2 = ["city"]
reader2 = csv.DictReader( inputCSVFile2, fieldnames2)
rows2 = list(reader2)
totalrows2 = len(rows2)


with open(outputFileName, 'w', encoding='utf-8-sig') as outputJSONFile:
  outputJSONFile.write('[\n')
  
  for index, row in enumerate(rows):
    # Use the ensure_ascii=False to force to encode the value to UTF-8:
    json.dump(row, outputJSONFile, ensure_ascii=False)
    outputJSONFile.write(', \n')
  index = 667
  for row in enumerate(rows2):
    
    if index != totalrows2-1:
      # note: the there are 666 cities in China
      outputJSONFile.write('{"id": "' + str(index) + '", "city": "' + row[1]['city'] + '"},\n')
    else:
      outputJSONFile.write('{"id": "' + str(index) + '", "city": "' + row[1]['city'] + '"}\n')
    index += 1
  outputJSONFile.write(']')

inputCSVFile2.close()
 

inputCSVFile.close()
