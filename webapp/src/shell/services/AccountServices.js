import { getLogger } from 'shell/logger';
import BaseServices from './BaseServices';

const logger = getLogger('AccountService');
const ACCOUNT_SERVICE_CONFIGS_PREFIX = 'services-account';

export default class AccountServices extends BaseServices {
  constructor() {
    super();
    this.configsPrefix = ACCOUNT_SERVICE_CONFIGS_PREFIX;
  }

  async getAccountInfo(userId) {
    try {
      return await this.get(`v1/account/get?userId=${userId}`);
    } catch (err) {
      logger.error(err);
    }
  }

  async addResume(userId, resumeId, resumeName) {
    try {
      return await this.post(`v1/account/${userId}/resumes`, {
        "alias": resumeName,
        "createdAt": new Date().toISOString(),
        "resumeId": resumeId,
        "thumbnailUri": "thumbnailUri"
      });
    } catch (err) {
      logger.error(err);
    }
  }

  async deleteResume(userId, resumeId) {
    try {
      return await this.delete(`v1/account/${userId}/resumes/${resumeId}`);
    } catch (err) {
      logger.error(err);
    }
  }

  async updateResume(resumeId, resumeDto,) {
    try {
      return await this.put(`/v1/account/resumes/${resumeId}`, resumeDto);
    } catch (err) {
      logger.error(err);
    }
  }
}
