db.createCollection('job_tailored', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['job', 'tailoredByResumes'],
      properties: {
        job: {
          bsonType: 'objectId'
        },
        tailoredByResumes: {
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
