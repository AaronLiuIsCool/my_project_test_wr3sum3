db.createCollection('job_matched', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['job', 'matchedByResumes'],
      properties: {
        job: {
          bsonType: 'objectId'
        },
        matchedByResumes: {
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
