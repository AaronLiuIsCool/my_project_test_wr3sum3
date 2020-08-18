db.createCollection('job_visited', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['job', 'visitedByUsers'],
      properties: {
        job: {
          bsonType: 'objectId'
        },
        visitedByUsers: {
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
