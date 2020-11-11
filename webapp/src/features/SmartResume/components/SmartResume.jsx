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

async function getResume(dispatch, resumeId) {
    if (resumeId) {
        try {
            const resumeData = await resumeServices.getResume(resumeId);
            dispatch(actions.setResume(resumeData));
        } catch (exception) {
            logger.error(exception);
        }
        return;
    }
}

async function getAccountInfoAndSetResumeName(dispatch, userId, resumeId) {
    try {
        const responseJson = await accountServices.getAccountInfo(userId);
        if (responseJson.success) {
            const { resumes } = responseJson.account;
            const resume = resumes.find((item) => item.resumeId === resumeId);
            dispatch(actions.setAlias(resume?.alias));
        } else {
            logger.error(responseJson.message);
        }
    } catch (exception) {
        logger.error(exception);
    }
}
const SmartResume = ({ useObserver = false, resumeId }) => {
    const dispatch = useDispatch();
    const userId = useSelector(selectUserId);
    const language = useSelector(selectLanguage);
    const messages = language === 'zh' ? zh : en;
    useEffect(() => {
        const updatePreview = async () => {
            await Promise.all([
                getResume(dispatch, resumeId),
                getAccountInfoAndSetResumeName(dispatch, userId, resumeId)
            ]);
            previewResume(messages.RPreview);
        }
        updatePreview();

    }, []); // eslint-disable-line

    return (
        <I8nContext.Provider value={messages}>
            <div className="features smart-resume">
                <div className="overlay">
                    <LeftNav />
                    <Assistant />
                    <ResumePreview />
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