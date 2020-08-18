import React from 'react';
import { useSelector } from 'react-redux';

import CallToAction from './CallToAction';

import { selectLanguage, selectPath } from 'features/App/slicer';
// import { getLogger } from 'shell/logger';
import { I8nContext } from 'shell/i18n';
// import ResumeServices from 'shell/services/ResumeServices';

import zh from '../i18n/zh.json';
import en from '../i18n/en.json';

// const logger = getLogger('ResumeHub');
// const resumeServices = new ResumeServices();

const renderHub = (path = {}) => {
    switch (path.search) {
        case '?hub':
            return (
                <div>show a list of resumes</div>
            );
        default:
            return <CallToAction />;
    };
}

const ResumeHub = () => {
    // const dispatch = useDispatch();
    // const [ready, setReady] = useState(false);
    const path = useSelector(selectPath);
    const language = useSelector(selectLanguage);
    const messages = language === 'zh' ? zh : en;

    return (
        <I8nContext.Provider value={messages}>
            <div className='features resumeHub'>
                {renderHub(path)}
            </div>
        </I8nContext.Provider>
    );
};

export default ResumeHub;