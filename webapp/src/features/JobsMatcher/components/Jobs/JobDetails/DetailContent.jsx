import React, { useEffect, useState } from 'react';

import MatchScore from './MatchScore';

import JobsService from 'shell/services/JobsServices';
import { useI8n } from 'shell/i18n';

import styles from '../../../styles/JobDetails.module.css';

const jobService = new JobsService();

const DetailContent = ({ data }) => {
    const [matchingData, setMatchingData] = useState({});
    const messages = useI8n();

    useEffect(() => {
        async function getMatchingScore() {
            const matchingData = await jobService.getJobMatchingScore();
            setMatchingData(matchingData);
        }
        getMatchingScore();
    }, []);

    return (
        <div className={styles['job-details-content']}>
            <MatchScore score={matchingData.score} description={matchingData.description} />
            <div className={styles['job-details-description']}>
                <div className={styles['job-details-description-label']}>
                    {messages['job_details_description']}
                </div>
                {data.description.split('\n').map(text => <p>{text}</p>)}
            </div>
        </div>
    );
};

export default DetailContent;