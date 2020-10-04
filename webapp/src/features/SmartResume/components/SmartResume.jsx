import React, { useEffect } from 'react';
import PropTypes from 'prop-types';
import { useSelector, useDispatch } from 'react-redux';
import { previewResume } from './ResumePreview/resumeBuilder';

import { selectLanguage, selectUserId } from 'features/App/slicer';
import { actions } from 'features/SmartResume/slicer';
import { I8nContext } from 'shell/i18n';
import AccountServices from 'shell/services/AccountServices';
import ResumeServices from 'shell/services/ResumeServices';
import { getLogger } from 'shell/logger';

import LeftNav from './LeftNav';
import ExperiencesForm from './ExperiencesForm';
import Assistant from './Assistant';
import ResumePreview from './ResumePreview';

import zh from '../i18n/zh.json';
import en from '../i18n/en.json';

const logger = getLogger('ResumeStarter');
const accountServices = new AccountServices();
const resumeServices = new ResumeServices();

async function getOrCreateResume(dispatch, userId, resumeId, language) {
    if (resumeId) {
        try {
            const response = await resumeServices.getResume(resumeId);
            const resumeData = await response.json();
            dispatch(actions.setResume(resumeData));
        } catch (exception) {
            logger.error(exception)
        }
        return;
    }

    try {
        const response = await resumeServices.createResume({language});
        const data = await response.json();
        const resumeId = data.id;
        accountServices.addResume(userId, resumeId);
        dispatch(actions.setId(resumeId));
    } catch (exception) {
        logger.error(exception);
        // TODO: Need to handle retry
    }
}

const SmartResume = ({ useObserver = false, resumeId }) => {
    const dispatch = useDispatch();
    const userId = useSelector(selectUserId);
    const language = useSelector(selectLanguage);
    const messages = language === 'zh' ? zh : en;

    useEffect(() => {
        getOrCreateResume(dispatch, userId, resumeId, language);
		previewResume(messages.RPreview);
    }, []); // eslint-disable-line

    return (
        <I8nContext.Provider value={messages}>
            <div className="features smart-resume">
                <div className="overlay">
                    <LeftNav />
                    <Assistant />
                    <ResumePreview  />
                </div>
                <ExperiencesForm useObserver={useObserver} />
            </div>
        </I8nContext.Provider>
    );
};

SmartResume.propTypes = {
    useObserver: PropTypes.bool 
};

export default SmartResume;