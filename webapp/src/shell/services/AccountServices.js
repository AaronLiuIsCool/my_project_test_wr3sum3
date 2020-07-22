import { getLogger } from 'shell/logger';
import BaseServices from './BaseServices';

const logger = getLogger('AccountService');
const ACCOUNT_SERVICE_CONFIGS_PREFIX = 'services-accounts';

export default class AccountServices extends BaseServices {
  constructor() {
    super();
    this.configsPrefix = ACCOUNT_SERVICE_CONFIGS_PREFIX;
  }

  // TODO: All the urls below are not working, need to update to correct ones once get Swagger to work
  async getUser() {
    try {
      return await this.get('v1/user');
    } catch(err) {
      logger.error(err);
    }
  }

  async updateUser() {
    try {
      return await this.post('v1/user', {"test": true});
    } catch(err) {
      logger.error(err);
    }
  }
}
