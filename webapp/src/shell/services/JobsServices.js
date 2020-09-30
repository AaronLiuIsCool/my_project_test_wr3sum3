import { getLogger } from 'shell/logger';
import BaseServices from './BaseServices';

const logger = getLogger('JobsService');
const PREFIX = 'services-job';

export default class JobsServices extends BaseServices {
    constructor() {
        super();
        this.configsPrefix = PREFIX;
    }

    async getJob(uuid) {
        try {
            const response = await this.get(`v1/jobs/uuid/${uuid}`);
            return await response.json();
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
}
