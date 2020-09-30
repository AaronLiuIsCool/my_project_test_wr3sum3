import React, { useState } from 'react';

import JobsList from './JobsList';
import JobDetails from './JobDetails';


import styles from '../../styles/Jobs.module.css';

const Jobs = ({ data, pageNumber, onPageChange, modalOpenHandler }) => {
    const [selection, setSelection] = useState(0);
    return (
        <div className={styles.container}>
            <JobsList data={data} pageNumber={pageNumber} onPageChange={onPageChange}
                onSelect={setSelection} selection={selection} />
            <JobDetails data={data && data.jobs && data.jobs[selection]} />
        </div>
    )
};

export default Jobs;