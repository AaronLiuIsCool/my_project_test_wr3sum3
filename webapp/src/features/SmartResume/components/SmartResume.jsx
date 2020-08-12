import React, { Fragment, useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';

import { selectLanguage } from 'features/App/slicer';
import { getLogger } from 'shell/logger';
import { I8nContext } from 'shell/i18n';
import ResumeServices from 'shell/services/ResumeServices';

import LeftNav from './LeftNav';
import ExperiencesForm from './ExperiencesForm';
import Preview from './Preview';

import { actions, selectResume } from '../slicer';
import { resumeAdaptor } from '../utils/servicesAdaptor';

import zh from '../i18n/zh.json';
import en from '../i18n/en.json';

const logger = getLogger('SmartResume');
const resumeServices = new ResumeServices();

const SmartResume = ({ useObserver = false }) => {
    const dispatch = useDispatch();
    const [ready, setReady] = useState(false);
    const resume = useSelector(selectResume);
    const language = useSelector(selectLanguage);
    const messages = language === 'zh' ? zh : en;

    useEffect(() => {
        async function initResume() {
            let id = null;
            try {
                const response = await resumeServices.createResume(resumeAdaptor(resume));
                const data = await response.json();
                id = data.id;
            } catch (exception) {
                logger.error(exception);
            } finally {
                dispatch(actions.setId(id || null));
                setReady(true);
            }
        }

        initResume();
    }, []); // eslint-disable-line react-hooks/exhaustive-deps

    return ready ? (
        <I8nContext.Provider value={messages}>
            <div className="features smart-resume">
                <div className="overlay">
                    <LeftNav />
                    <Preview />
                </div>
                <ExperiencesForm useObserver={useObserver} />
            </div>
        </I8nContext.Provider>
    ) : Fragment;
};

SmartResume.propTypes = {
    useObserver: PropTypes.bool
};

export default SmartResume;