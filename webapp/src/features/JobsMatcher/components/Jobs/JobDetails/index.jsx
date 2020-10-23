import React, { useEffect, useState } from 'react';

import JobsServices from 'shell/services/JobsServices';

import DetailHeader from './DetailHeader';
import DetailContent from './DetailContent';

import styles from '../../../styles/JobDetails.module.css';

const jobServices = new JobsServices();

const renderDetail = (resumeId, data, modalOpenHandler) => (
    <>
        <DetailHeader resumeId={resumeId} data={data} />
        <DetailContent data={data} modalOpenHandler={modalOpenHandler}/>
    </>
);

const JobDetails = ({ resumeId, data = {}, modalOpenHandler }) => {
    const [job, setJob] = useState();
    

    useEffect(() => {
        if (!data.jobUuid) {
            setJob(undefined);
            return;
        }
        jobServices.getJob(data.jobUuid).then(responseData => {
            setJob(responseData);
        })
    }, [data]);

    return (
        <div className={styles["container-details"]}>
            {Boolean(job) ? renderDetail(resumeId, job, modalOpenHandler) : null}
        </div>
    );
};

export default JobDetails;