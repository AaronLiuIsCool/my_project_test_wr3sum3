import React from 'react';
import PropTypes from 'prop-types';

import JobsList from './JobsList';
import JobDetails from './JobDetails';


import styles from '../../styles/Jobs.module.css';

const Jobs = ({ resumeId, data, pageNumber = 0, 
    onPageChange, modalOpenHandler, selectedJob = 0, setSelectedJob }) => {
    return (
        <div className={styles.container}>
            <JobsList data={data} pageNumber={pageNumber} onPageChange={onPageChange}
                onSelect={setSelectedJob} selection={selectedJob} />
            <JobDetails resumeId={resumeId} data={data?.jobs?.[selectedJob]} modalOpenHandler={modalOpenHandler}/>
        </div>
    )
};

Jobs.propTypes = {
    resumeId: PropTypes.string.isRequired,
    data: PropTypes.object.isRequired,
    pageNumber: PropTypes.number,
    onPageChange: PropTypes.func,
    modalOpenHandler: PropTypes.func,
    selectedJob: PropTypes.number,
    setSelectedJob: PropTypes.func
}

export default Jobs;