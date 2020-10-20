import React from 'react';
import styles from 'features/JobsMatcher/styles/JobDetails.module.css';

const JobDetailHeader = ({ data }) => {
    return (
        <div className={styles["job-details-header"]}>
            <div className={styles["job-details-title"]}>
                {data.positionTitle}
            </div>
            <div className={styles["job-details-subtitle"]}>
                <span className={styles["job-details-subtitle-company"]}>{data.companyName}</span>
                <span className={styles["job-details-subtitle-location"]}>{`${data?.location?.country} ${data?.location?.city}`}</span>
                {/* <span className={styles["job-details-subtitle-salary"]}>{`${data.salaryMin} ~ ${data.salaryMax}`}</span> */}
            </div>
        </div>
    );
};

export default JobDetailHeader;