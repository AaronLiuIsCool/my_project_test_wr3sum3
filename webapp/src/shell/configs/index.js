import baseConfigs from './configs-base.json';

let overrides = {};

switch (process.env.NODE_ENV) {
  case 'production':
    overrides = {
      "env": "production"
    };
    break;
  case 'test':
    overrides = {
      "env": "test",
      "test-baseservices-baseUrl": "test.kuaidao.com"
    }
    break;
}

const configs = {...baseConfigs, ...overrides};

export default configs;
