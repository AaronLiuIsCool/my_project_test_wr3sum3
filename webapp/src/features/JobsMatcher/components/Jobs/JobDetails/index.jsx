import React from 'react';

import DetailHeader from './DetailHeader';
import DetailContent from './DetailContent';

import styles from '../../../styles/JobDetails.module.css';

const renderDetail = (data) => (
    <>
        <DetailHeader data={data} />
        <DetailContent data={data} />
    </>
);

const JobDetails = ({ data }) => (
    <div className={styles["container-details"]}>
        {Boolean(data) ? renderDetail(data) : null}
    </div>
);

export default JobDetails;