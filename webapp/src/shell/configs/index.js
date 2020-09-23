import baseConfigs from './configs-base.json';
import uatConfigs from './configs-uat.json';
import prodConfigs from './configs-prod.json';

let overrides = {};

console.log(`Build configs for ${process.env.REACT_APP_ENV}`)

switch (process.env.REACT_APP_ENV) {
  case 'production':
    overrides = prodConfigs;
    break;
  case 'UAT':
    overrides = uatConfigs;
    break;
  case 'test':
    overrides = {
      "env": "test",
      "test-baseservices-baseUrl": "test.kuaidao.com"
    }
    break;
  default:
    break;
}

const configs = {...baseConfigs, ...overrides};

export default configs;
