import { getLogger } from 'shell/logger';
import BaseServices from './BaseServices';

import mockData from './mocks/jobs.json';

const logger = getLogger('JobsService');
const PREFIX = 'jobs';

const MAX_SIZE_PER_PAGE = 7;

function getMockData(delay, pageNumber) {
  const start = pageNumber * MAX_SIZE_PER_PAGE;
  const end = Math.min((pageNumber + 1) * MAX_SIZE_PER_PAGE, mockData.results.length);
  const slicedData = {
    ...mockData,
    pageNumber,
    pageSize: MAX_SIZE_PER_PAGE,
    results: mockData.results.slice(start, end)
  }
  return new Promise(resolve => setTimeout(resolve, delay, slicedData));
}

export default class AccountServices extends BaseServices {
  constructor() {
    super();
    this.configsPrefix = PREFIX;
  }

  // TODO: replace with real API call
  async getJobs(query, country, city, resultsPageNumber) {
    try {
      if (!query && !country && !city) {
        return await Promise.resolve({});
      }
      return await getMockData(Math.random() * 1000, resultsPageNumber);
    } catch(err) {
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
}
