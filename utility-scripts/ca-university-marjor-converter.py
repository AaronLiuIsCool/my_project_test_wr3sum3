import csv
import json

inputCSVFile = open('./data/ca-university-majors.csv', 'r')
outputFileName = './data/ca-university-majors.json'

fieldnames = ["m"]
reader = csv.DictReader( inputCSVFile, fieldnames)

rows = list(reader)
totalrows = len(rows)

with open(outputFileName, 'w', encoding='utf8') as outputJSONFile:
  outputJSONFile.write('[\n')
  for index, row in enumerate(rows):
    if index != totalrows-1:
      outputJSONFile.write('{"id":"' + str(index+1) + '", "major": "' + row['m'] + '"},\n')
    else:
      outputJSONFile.write('{"id":"' + str(index+1) + '", "major": "' + row['m'] + '"}\n')
  outputJSONFile.write(']')

inputCSVFile.close()
