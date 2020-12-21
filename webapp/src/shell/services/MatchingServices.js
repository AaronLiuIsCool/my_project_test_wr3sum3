import { getLogger } from 'shell/logger';
import BaseServices from './BaseServices';

const logger = getLogger('MatchingServices');
const PREFIX = 'services-matching';

const MAX_SIZE_PER_PAGE = 7;

export default class MatchingServices extends BaseServices {
  constructor() {
    super();
    this.configsPrefix = PREFIX;
  }

  setContext(resumeDto) {
    this.resumeDto = resumeDto;
    this.findMatchingJobs(0);
  }

  // 对岗位精修分数+details
  async getJobRefinement(jobId, resumeId) {
    try {
      return await this.get(`v1/matching/resumes/score?jobUuid=${jobId}&resumeUuid=${resumeId}`);
    } catch (err) {
      logger.error(err);
    }
  }
  async findMatchingJobs(pageNumber) {
    try {
      return await this.post(
        `v1/matching/jobs/paging-match?limit=${MAX_SIZE_PER_PAGE}&offset=${pageNumber}`,
        this.resumeDto
      );
    } catch (err) {
      logger.error(err);
    }
  }

  async searchJobs(query, country, city, pageNumber) {
    let params = `term=${query}&page=${pageNumber}&pageSize=${MAX_SIZE_PER_PAGE}`;
    if(city) {
      params = `${params}&city=${city}`;
    }
    if(country) {
      params = `${params}&country=${country}`;
    }
    const url = `v1/matching/jobs/paging-search?${params}`;
    try {
      return await this.get(url);
    } catch (err) {
      logger.error(err);
    }
  }

  async bookmarkJob(resumeId, jobId) {
    try {
      return await this.post(`v1/matching/resumes/bookmark?jobUuid=${jobId}&resumeUuid=${resumeId}`);
    } catch (err) {
      logger.error(err);
    }
  }

  async unbookmarkJob(resumeId, jobId) {
    try {
      return await this.delete(`v1/matching/resumes/bookmark?jobUuid=${jobId}&resumeUuid=${resumeId}`);
    } catch (err) {
      logger.error(err);
    }
  }

  async findBookMarkJobs(resumeId) {
    try {
      const res = await this.get(`v1/matching/jobs/bookmarked?resumeUuid=${resumeId}`);
      return res;
    } catch (err) {
      logger.error(err);
    }
  }
  async findTailorJobs(resumeId) {
    try {
      const res = await this.get(`v1/matching/jobs/tailored?resumeUuid=${resumeId}`);
      return res;
    } catch (err) {
      logger.error(err);
    }
  }
}
