import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { useSelector, useDispatch } from 'react-redux';
import { previewResume } from './ResumePreview/resumeBuilder';
import { useHistory } from "react-router-dom";

import { selectLanguage, selectUserId } from 'features/App/slicer';
import { actions } from 'features/SmartResume/slicer';
import { I8nContext } from 'shell/i18n';
import ResumeServices from 'shell/services/ResumeServices';
import { getLogger } from 'shell/logger';

import LeftNav from './LeftNav';
import ExperiencesForm from './ExperiencesForm';
import Assistant from './Assistant';
import ResumePreview from './ResumePreview';

import zh from '../i18n/zh.json';
import en from '../i18n/en.json';

const logger = getLogger('ResumeStarter');
const resumeServices = new ResumeServices();

async function getResume(dispatch, userId, resumeId, resumeName, language, history) {
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
    
}
async function getAccountInfoAndSetResumeName(userId, resumeId, setter) {
  try {
    const response = await accountServices.getAccountInfo(userId);
    if (!response) {
      logger.warn('No response from account service get');
    }

    const responseJson = await response.json();
    if (responseJson.success) {
      const { resumes } = responseJson.account;
      const resume = resumes.find((item) => item.resumeId === resumeId);
      if (resume) {
        setter(resume.alias);
      }
    } else {
      logger.error(response.message);
    }
  } catch (exception) {
    logger.error(exception);
  }
}
const SmartResume = ({ useObserver = false, resumeId }) => {
    const history = useHistory();
    const dispatch = useDispatch();
    const userId = useSelector(selectUserId);
    const language = useSelector(selectLanguage);
    const params = new URLSearchParams(window.location.search);
    const resumeName = params.get('resumeName');
    const messages = language === 'zh' ? zh : en;
    const [resumeName, setResumeName] = useState('');
    useEffect(() => {
        const updatePreview = async () => {
            await getResume(dispatch, userId, resumeId, resumeName, language, history);
            previewResume(messages.RPreview);
        }
        updatePreview();
        getAccountInfoAndSetResumeName(userId, resumeId, setResumeName);
    }, []); // eslint-disable-line

    return (
        <I8nContext.Provider value={messages}>
            <div className="features smart-resume">
                <div className="overlay">
                    <LeftNav />
                    <Assistant />
                    <ResumePreview  />
                </div>
                <ExperiencesForm resumeName={resumeName} useObserver={useObserver} />
            </div>
        </I8nContext.Provider>
    );
};

SmartResume.propTypes = {
    useObserver: PropTypes.bool 
};

export default SmartResume;