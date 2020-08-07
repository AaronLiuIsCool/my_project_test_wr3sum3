import React from 'react';

import styles from '../../../styles/JobsList.module.css';

const Job = ({ data, onClick, selected }) => (
    <div className={styles[selected ? "container-job-selected" : "container-job"]}
        onClick={onClick} >
        <div className={styles["job-title"]}>{data.title}</div>
        <div className={styles["job-company"]}>{data["company_name"]}</div>
        <div className={styles["job-salary"]}>年薪： 5万到7万</div>
        <div className={styles["job-post-time"]}>12小时前发布</div>
    </div>
);

export default Job;