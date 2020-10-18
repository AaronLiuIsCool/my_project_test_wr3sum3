import React from 'react';

import JobsList from './JobsList';
import JobDetails from './JobDetails';


import styles from '../../styles/Jobs.module.css';

const Jobs = ({ data, pageNumber, onPageChange, modalOpenHandler, selectedJob, setSelectedJob }) => {
    return (
        <div className={styles.container}>
            <JobsList data={data} pageNumber={pageNumber} onPageChange={onPageChange}
                onSelect={setSelectedJob} selection={selectedJob} />
            <JobDetails data={data && data.jobs && data.jobs[selectedJob]} modalOpenHandler={modalOpenHandler}/>
        </div>
    )
};

export default Jobs;