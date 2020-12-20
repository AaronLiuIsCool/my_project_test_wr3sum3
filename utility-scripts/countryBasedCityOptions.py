import csv
import json

# run this 3 times
##inputCSVFile = open('./data/cn.csv', 'r')
##inputCSVFile = open('./data/ca-en.csv', 'r')
##inputCSVFile = open('./data/us-en.csv', 'r')



##inputCSVFile = open('./data/cn-en.csv', 'r')
#inputCSVFile = open('./data/uk-en.csv', 'r')
#inputCSVFile = open('./data/au-en.csv', 'r')
##inputCSVFile = open('./data/jp-en.csv', 'r')
inputCSVFile = open('./data/kr-en.csv', 'r')
outputFileName = './data/countryBasedCityOptions_temp.json'

fieldnames = ["city"]
reader = csv.DictReader( inputCSVFile, fieldnames)
rows = list(reader)
totalrows = len(rows)



with open(outputFileName, 'w', encoding='utf-8-sig') as outputJSONFile:
  outputJSONFile.write('[\n')
  index = 0
  for row in enumerate(rows):
    if index != totalrows-1:
      outputJSONFile.write('{"id": "' + str(index) + '", "city": "' + row[1]['city'] + '"},\n')
    else:
      outputJSONFile.write('{"id": "' + str(index) + '", "city": "' + row[1]['city'] + '"}\n')
    index += 1
  outputJSONFile.write(']')
  
 

inputCSVFile.close()
