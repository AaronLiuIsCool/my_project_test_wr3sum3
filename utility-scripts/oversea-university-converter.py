import csv
import json

inputCSVFile = open('./data/ oversea-university.csv', 'r')
outputFileName = './data/ oversea-university.json'

fieldnames = ["city", "country"]
supported_country_list = ['au', 'ca', 'cn', 'in', 'jp', 'kr', 'uk', 'us']
reader = csv.DictReader( inputCSVFile, fieldnames)

rows = list(reader)
totalrows = len(rows)

with open(outputFileName, 'w', encoding='utf-8-sig') as outputJSONFile:
  outputJSONFile.write('{\n')

  for country in supported_country_list:
    outputJSONFile.write('"'+country+'":\n [')
    for index, row in enumerate(rows):
      
      if (country != row['country']):
        continue
      
      outputJSONFile.write('{"id":"' + str(index+1) + '", "data": "' + row['city'] + '"},\n')
        
    outputJSONFile.write('],\n')

    
  outputJSONFile.write('}')

inputCSVFile.close()
