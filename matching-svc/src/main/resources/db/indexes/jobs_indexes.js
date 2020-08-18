db.jobs.createIndex({
  "location.country": 1,
  "location.city": 1,
  "createdAt": -1
});

db.jobs.createIndex({
  "location.country": 1,
  "location.city": 1,
  "keywords.majors": 1,
  "keywords.value": 1,
  "createdAt": -1
}, {
  "partialFilterExpression": {"keywords.majors": {$exists: true}}
});

db.jobs.createIndex({
  "location.country": 1,
  "location.city": 1,
  "keywords.value": 1,
  "createdAt": -1
});

db.jobs.createIndex({
  "location.country": 1,
  "location.city": 1,
  "title": 1,
  "createdAt": -1
});

db.jobs.createIndex({
  "location.country": 1,
  "location.city": 1,
  "relevantMajors": 1,
  "createdAt": -1
}, {
  "partialFilterExpression": {"keywords.majors": {$exists: true}}
});

db.jobs.createIndex({
  "location.country": 1,
  "location.city": 1,
  "title": 1,
  "relevantMajors": 1,
  "createdAt": -1
}, {
  "partialFilterExpression": {"keywords.majors": {$exists: true}}
});

db.jobs.createIndex(
  {
    "title": "text",
    "companyName": "text",
    "relevantMajors": "text"
  },
  {
    "weights": {
      "title": 40,
      "companyName": 30,
      "relevantMajors": 30
    },
    "name": "generalSearchIndex"
  }
)

db.jobs.createIndex({"jobId": 1});
