import csv, json

inputCSVFile = open('./data/resume-tips-v2.csv', 'r')
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
    data[rowType] = [{"q": row['question'], 'a': row['answer']}]
  else:
    data[rowType].append({"q": row['question'], 'a': row['answer']})

print(data)
