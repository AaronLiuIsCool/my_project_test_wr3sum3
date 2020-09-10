import { getLogger } from 'shell/logger';
import BaseServices from './BaseServices';

import defaultJobsMetadata from './data/defaultJobsMetadata.json';

const logger = getLogger('JobsService');
const PREFIX = 'services-job';

// const MAX_SIZE_PER_PAGE = 7;

function getDefaultJobsLocations() {
  const locations = [];
  Object.keys(defaultJobsMetadata.locations).forEach(country => {
    defaultJobsMetadata.locations[country].forEach(city => {
      locations.push({ country, city });
    });
  })
  return locations;
}

export default class JobsServices extends BaseServices {
    constructor() {
        super();
        this.configsPrefix = PREFIX;
    }

    async getAllJobs() {
      try {
          const response = await this.get('v1/jobs/all');
          const data = await response.json();
          console.log(data);
      } catch (err) {
          logger.error(err);
      }
    }

    async getDefaultJobs(resultsPageNumber) {
        try {
            return await this.post('v1/jobs/job-search', {
              locations: getDefaultJobsLocations()
            });
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
          locations: [{city, country}],
          majors: [{name: query}]
        });
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
