db.createCollection('keywords', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['value'],
      properties: {
        value: {
          bsonType: 'string'
        },
        major: {
          bsonType: 'string'
        }
      }
    }
  },
  autoIndexId: true,
  validationLevel: 'strict',
  validationAction: 'error'
});
