db.createCollection('job_bookmarked', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['job', 'bookmarkedByUsers'],
      properties: {
        job: {
          bsonType: 'objectId'
        },
        bookmarkedByUsers: {
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
