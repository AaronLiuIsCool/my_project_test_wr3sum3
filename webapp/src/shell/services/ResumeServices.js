import { getLogger } from 'shell/logger';
import BaseServices from './BaseServices';

import workWrittenAssistantData from './mocks/workWrittenAssistantData.json';

const logger = getLogger('JobsService');
const PREFIX = 'services-resume';

function getMockData(delay, key) {
    return new Promise(resolve => setTimeout(resolve, delay, workWrittenAssistantData[key]));
}

export default class ResumeServices extends BaseServices {
    constructor() {
        super();
        this.configsPrefix = PREFIX;
    }

    async getResume(resumeId) {
        try {
            return await this.get(`v1/resumes/${resumeId}`);
        } catch (exception) {
            logger.error(exception);
        }
    }

    async createResume(resume) {
        try {
            return await this.post('v1/resumes', resume);
        } catch (exception) {
            logger.error(exception);
        }
    }

    async createBasics(resumeId, basics) {
        try {
            return await this.post(`v1/resumes/${resumeId}/basic-info`, basics);
        } catch (exception) {
            logger.error(exception);
        }
    }

    async updateBasics(resumeId, basics) {
        try {
            return await this.put(`v1/resumes/${resumeId}/basic-info`, basics);
        } catch (exception) {
            logger.error(exception);
        }
    }

    async createEducation(resumeId, education) {
        try {
            return await this.post(`v1/resumes/${resumeId}/educations`, education);
        } catch (exception) {
            logger.error(exception);
        }
    }

    async updateEducation(eduId, education) {
        try {
            return await this.put(`v1/educations/${eduId}`, education);
        } catch (exception) {
            logger.error(exception);
        }
    }

    async createWork(resumeId, work) {
        try {
            return await this.post(`v1/resumes/${resumeId}/work-experiences`, work);
        } catch (exception) {
            logger.error(exception);
        }
    }

    async updateWork(id, work) {
        try {
            return await this.put(`v1/work-experiences/${id}`, work);
        } catch (exception) {
            logger.error(exception);
        }
    }

    async createProject(resumeId, project) {
        try {
            return await this.post(`v1/resumes/${resumeId}/project-experiences`, project);
        } catch (exception) {
            logger.error(exception);
        }
    }

    async updateProject(id, project) {
        try {
            return await this.put(`v1/project-experiences/${id}`, project);
        } catch (exception) {
            logger.error(exception);
        }
    }

    async createVolunteer(resumeId, volunteer) {
        try {
            return await this.post(`v1/resumes/${resumeId}/volunteer-experiences`, volunteer);
        } catch (exception) {
            logger.error(exception);
        }
    }

    async updateVolunteer(id, volunteer) {
        try {
            return await this.put(`v1/volunteer-experiences/${id}`, volunteer);
        } catch (exception) {
            logger.error(exception);
        }
    }

    async createCertificate(resumeId, certificate) {
        try {
            return await this.post(`v1/resumes/${resumeId}/certificates`, certificate);
        } catch (exception) {
            logger.error(exception);
        }
    }

    async updateCertificate(id, certificate) {
        try {
            return await this.put(`v1/certificates/${id}`, certificate);
        } catch (exception) {
            logger.error(exception);
        }
    }

    async getWrittenAssistant(type, key) {
        try {
            return await getMockData(Math.random() * 1000, key);
        } catch (exception) {
            logger.error(exception);
        }
    }
    
    // TODO: Update this fetch to UAT endpoint later 
    async getSuggestions(industry, title) {
        try {
            const res = await fetch(`/v1/get_suggestions_by_industry_position?industry=${industry}&limit=50&offset=0&position=${title}`)
            const {suggestions} = await res.json()
            return {
                suggestions
            }
        } catch (error) {
            console.error(error)
            return {
                suggestions: []
            }
        }        
    }
}
