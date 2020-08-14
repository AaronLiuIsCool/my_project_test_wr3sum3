import csv
import json

inputCSVFile = open('./data/cn-certificate.csv', 'r')
outputFileName = './data/cn-certificate.json'

fieldnames = ["name"]
reader = csv.DictReader( inputCSVFile, fieldnames)
rows = list(reader)
totalrows = len(rows)

with open(outputFileName, 'w', encoding='utf8') as outputJSONFile:
  outputJSONFile.write('[\n')
  for index, row in enumerate(rows):
    # Use the ensure_ascii=False to force to encode the value to UTF-8:
    json.dump(row, outputJSONFile, ensure_ascii=False)
    if index != totalrows-1:
      outputJSONFile.write(', \n')
    else:
      outputJSONFile.write('\n')
  outputJSONFile.write(']')

inputCSVFile.close()
