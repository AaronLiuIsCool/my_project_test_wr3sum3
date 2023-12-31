import { getLogger } from 'shell/logger';
import BaseServices from './BaseServices';

const logger = getLogger('JobsService');
const PREFIX = 'services-job';

export default class JobsServices extends BaseServices {
  constructor() {
    super();
    this.configsPrefix = PREFIX;
  }

  async getAllJobs() {
    try {
      return await this.get('v1/jobs/all');
    } catch (err) {
      logger.error(err);
    }
  }

  async getJob(uuid) {
    try {
      return await this.get(`v1/jobs/uuid/${uuid}?lazy=false`);
    } catch (err) {
      logger.error(err);
    }
  }


  async getJobs(query, country, city, resultsPageNumber) {
    try {
      if (!query && !country && !city) {
        return await this.getDefaultJobs(resultsPageNumber);
      }
      return await this.post('v1/jobs/job-search', {
        locations: [{ city, country }],
        majors: [{ name: query }]
      });
    } catch (err) {
      logger.error(err);
    }
  }

  // TODO: replace with real API call
  async getJobMatchingScore() {
    return await Promise.resolve({
      score: 79,
      description: "缺少部分相关技能 ｜缺少众多关键词 ｜ 缺少相关技能证书"
    });
  }

  // TODO: Update this fetch to UAT endpoint later
  async getSuggestions(industry, title) {
    const resObj = {
      suggestions: []
    };
    try {
      const data = await this.get(`v1/get_suggestions_by_industry_position?industry=${encodeURIComponent(industry)}&limit=50&offset=0&position=${encodeURIComponent(title)}`);
      if (data.success !== false) {
        resObj.suggestions = data.suggestions;
      }
    } catch (error) {
      logger.error(error);
    } finally {
      console.log(resObj)
      return resObj;
    }
  }
}
