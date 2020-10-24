import csv
import json


def buildSingleCountry(countryShortCode, outputJSONFile):
  inputCSVFile = open(f"./data/{countryShortCode}.csv", 'r')

  fieldnames = ["city"]
  reader = csv.DictReader( inputCSVFile, fieldnames)
  rows = list(reader)
  totalrows = len(rows)

  outputJSONFile.write('[\n')
   
  for index, row in enumerate(rows):
    if index != totalrows-1:
      # note: the there are 666 cities in China
      outputJSONFile.write('{"id":"' + str(index) + '", "data": "' + row['city'] + '"},\n')
    else:
      outputJSONFile.write('{"id":"' + str(index) + '", "data": "' + row['city'] + '"}\n')
  outputJSONFile.write(']')

  inputCSVFile.close()


outputFileName = './data/city.json'

countries = ['cn', 'ca', 'us', 'au', 'jp', 'uk', 'in', 'kr']
with open(outputFileName, 'w', encoding='utf-8-sig') as outputJSONFile:
  outputJSONFile.write('{')
  for country in countries:
    outputJSONFile.write('"'+country+'": ')
    buildSingleCountry(country, outputJSONFile)
    outputJSONFile.write('\n,\n')

  outputJSONFile.write('}')
