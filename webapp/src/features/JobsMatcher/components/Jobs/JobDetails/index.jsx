import React, { useEffect, useState } from 'react';

import JobsServices from 'shell/services/JobsServices';

import DetailHeader from './DetailHeader';
import DetailContent from './DetailContent';

import styles from '../../../styles/JobDetails.module.css';

const jobServices = new JobsServices();

const renderDetail = (data) => (
    <>
        <DetailHeader data={data} />
        <DetailContent data={data} />
    </>
);

const JobDetails = ({ data = {} }) => {
    const [job, setJob] = useState();

    useEffect(() => {
        if (!data.jobUuid) {
            return;
        }

        jobServices.getJob(data.jobUuid).then(responseData => {
            setJob(responseData);
        })
    }, [data]);

    return (
        <div className={styles["container-details"]}>
            {Boolean(job) ? renderDetail(job) : null}
        </div>
    );
};

export default JobDetails;