db.createCollection('resumes', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['resumeId', 'userId', 'location'],
      properties: {
        resumeId: {
          bsonType: 'string'
        },
        userId: {
          bsonType: 'string'
        },
        aliase: {
          bsonType: 'string'
        },
        location: {
          bsonType: 'object',
          required: ['country', 'city'],
          properties: {
            country: {
              bsonType: 'string'
            },
            state: {
              bsonType: 'string'
            },
            city: {
              bsonType: 'string'
            },
            postCode: {
              bsonType: 'string'
            }
          }
        },
        majors: {
          bsonType: 'array',
          items: {
            bsonType: 'string'
          }
        },
        keywords: {
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
