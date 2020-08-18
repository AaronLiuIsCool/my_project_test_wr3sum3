db.createCollection('jobs', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['jobId', 'title', 'companyName', 'location', 'createdAt', 'isActive'],
      properties: {
        jobId: {
          bsonType: 'string'
        },
        title: {
          bsonType: 'string'
        },
        companyName: {
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
        jobType: {
          enum: [ "part-time", "full-time" ]
        },
        employmentType: {
          enum: [ "permanent", "contract" ]
        },
        compensation: {
          bsonType: 'object',
          required: ['currency', 'type', 'lowBound', 'highBound'],
          properties: {
            currency: {
              bsonType: 'string'
            },
            type: {
              enum: [ "salary", "wage" ]
            },
            lowBound: {
              bsonType: 'string'
            },
            highBound: {
              bsonType: 'string'
            }
          }
        },
        relevantMajors: {
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
        },
        createdAt: {
          bsonType: 'timestamp'
        },
        isActive: {
          bsonType: 'bool'
        }
      }
    }
  },
  autoIndexId: true,
  validationLevel: 'strict',
  validationAction: 'error'
});
