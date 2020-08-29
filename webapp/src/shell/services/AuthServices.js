import { getLogger } from 'shell/logger';
import BaseServices from './BaseServices';

const logger = getLogger('AuthServices');
const PREFIX = 'services-auth';

export default class AuthServices extends BaseServices {
    constructor() {
        super();
        this.configsPrefix = PREFIX;
    }

    async findWhoAmI() {
        try {
            return await this.get('v1');
        } catch (exception) {
            logger.error(exception);
        }
    }
}
