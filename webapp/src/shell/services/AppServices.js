import { getLogger } from 'shell/logger';
import BaseServices from './BaseServices';

const logger = getLogger('AppServices');
const PREFIX = 'services-app';

export default class AppServices extends BaseServices {
    constructor() {
        super();
        this.configsPrefix = PREFIX;
    }

    async translate(data) {
        try {
            return await this.post('v1/translate/gcp', {
              text: data,
              from: 'zh',
              to: 'en'
            });
        } catch (exception) {
            logger.error(exception);
        }
    }
}
