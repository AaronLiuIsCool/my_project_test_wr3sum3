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
        const params = `country=${country}&city=${city}&term=${query}&page=${pageNumber}&pageSize=${MAX_SIZE_PER_PAGE}`;
        const url = `v1/matching/jobs/paging-search?${params}`;
        try {
            return await this.get(url);
        } catch (err) {
            logger.error(err);
        }
    }
}
