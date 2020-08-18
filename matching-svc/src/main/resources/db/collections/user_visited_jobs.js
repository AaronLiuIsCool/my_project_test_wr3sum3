db.createCollection('user_visited_jobs', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['userId', 'visitedJobs'],
      properties: {
        userId: {
          bsonType: 'string'
        },
        visitedJobs: {
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
