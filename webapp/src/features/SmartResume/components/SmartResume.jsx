import React, { useEffect, useState, Fragment } from 'react';
import PropTypes from 'prop-types';
import { useSelector, useDispatch } from 'react-redux';

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

const SmartResume = ({ useObserver = false, resumeId }) => {
    const dispatch = useDispatch();
    const [ready, setReady] = useState(false);
    const userId = useSelector(selectUserId);
    const language = useSelector(selectLanguage);
    const messages = language === 'zh' ? zh : en;

    useEffect(() => {
        async function getOrCreateResume() {
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
        getOrCreateResume().then(() => setReady(true));
    }, []); // eslint-disable-line

    if (!ready) {
        return Fragment;
    }

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