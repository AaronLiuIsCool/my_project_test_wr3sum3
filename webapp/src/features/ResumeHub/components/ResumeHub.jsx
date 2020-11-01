import React, { useEffect, useState, Fragment } from 'react';
import { useSelector, useDispatch } from 'react-redux';

import CallToAction from './CallToAction';
import Hub from './Hub';

import { selectLanguage, selectUserId } from 'features/App/slicer';
import { selectResumes, updateAccount } from 'features/App/slicer/account';
import { getLogger } from 'shell/logger';
import { I8nContext } from 'shell/i18n';
import AccountServices from 'shell/services/AccountServices';

import zh from '../i18n/zh.json';
import en from '../i18n/en.json';

const logger = getLogger('ResumeHub');
const accountServices = new AccountServices();

async function getAccountInfo(userId, dispatch) {
    try {
        const responseJson = await accountServices.getAccountInfo(userId);
        if (responseJson.success) {
            dispatch(updateAccount(responseJson.account));
        } else {
            logger.error(responseJson.message);
        }
    } catch (exception) {
        logger.error(exception);
    }
}

const renderHub = (resumes = []) => {
    if (resumes.length === 0) {
        return <CallToAction />;
    } else {
        return <Hub resumes={resumes} />;
    }
}

const ResumeHub = () => {
    const dispatch = useDispatch();
    const [ready, setReady] = useState(false);
    const userId = useSelector(selectUserId);
    const resumes = useSelector(selectResumes);
    const language = useSelector(selectLanguage);
    const messages = language === 'zh' ? zh : en;

    useEffect(() => {
        getAccountInfo(userId, dispatch).then(() => setReady(true));
    }, []); // eslint-disable-line

    if (!ready) {
        return <Fragment />;
    }

    return (
        <I8nContext.Provider value={messages}>
            <div className='features padding-for-nav resumeHub'>
                {renderHub(resumes)}
            </div>
        </I8nContext.Provider>
    );
};

export default ResumeHub;