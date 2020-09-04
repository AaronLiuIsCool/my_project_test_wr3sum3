import React from 'react';

import Button from 'react-bootstrap/Button';
import { ReactComponent as CreateYourResumeIllustration } from '../assets/create_your_resume.svg';

import { useI8n } from 'shell/i18n';

import styles from '../styles/CallToAction.module.css';

const CallToAction = () => {
    const messages = useI8n();
    return (
        <div className={styles.container}>
            <div className={styles.illustration}>
                <CreateYourResumeIllustration />
            </div>
            <div className={styles.callToAction}>
                <p className={styles.description}>
                    {messages['new_resume_description']}
                </p>
                <Button className={styles.action} href='/resumes/new'>
                    {messages['new_resume_start']}
                </Button>
            </div>
        </div>
    );
};

export default CallToAction;