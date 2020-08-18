db.createCollection('user_bookmarked_jobs', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['userId', 'bookmarkedJobs'],
      properties: {
        userId: {
          bsonType: 'string'
        },
        bookmarkedJobs: {
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
