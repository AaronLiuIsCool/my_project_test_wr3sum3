import csv, json

inputCSVFile = open('./data/resume-tips.csv', 'r')
outputFileName = './data/resume-tips.json'

fieldnames = ("type","question","answer")
reader = csv.DictReader( inputCSVFile, fieldnames)
rows = list(reader)
totalrows = len(rows)


data = {}
for row in rows:
  
  rowType = row['type']
  if rowType == "":
    continue
  if rowType not in data:
    data[rowType] = []
  else:
    data[rowType].append({"q": row['question'], 'a': row['answer']})

print(data)
