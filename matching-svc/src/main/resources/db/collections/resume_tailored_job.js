db.createCollection('resume_tailored_job', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['resume', 'tailoredToJob'],
      properties: {
        resume: {
          bsonType: 'objectId'
        },
        tailoredToJob: {
          bsonType: 'objectId'
        }
      }
    }
  },
  autoIndexId: true,
  validationLevel: 'strict',
  validationAction: 'error'
});
