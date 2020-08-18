db.createCollection('resume_matched_jobs', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['resume', 'matchedJobs'],
      properties: {
        resume: {
          bsonType: 'objectId'
        },
        matchedJobs: {
          bsonType: 'array',
          items: {
            bsonType: 'objectId'
          }
        }
      }
    }
  },
  autoIndexId: true,
  validationLevel: 'strict',
  validationAction: 'error'
});
