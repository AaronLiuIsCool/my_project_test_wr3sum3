import React from 'react';
import { timeSince } from '../../../utils/time';
import styles from '../../../styles/JobsList.module.css';

const Job = ({ data, onClick, selected }) => (
    <div className={styles[selected ? "container-job-selected" : "container-job"]}
        onClick={onClick} >
        <div className={styles["job-title"]}>{data.positionTitle}</div>
        <div className={styles["job-company"]}>{data.companyName}</div>
        <div className={styles["job-salary"]}>{`年薪： ${data.salaryMin} 到 ${data.salaryMax}`}</div>
        <div className={styles["job-post-time"]}>{timeSince(data.postDate)}</div>
    </div>
);

export default Job;