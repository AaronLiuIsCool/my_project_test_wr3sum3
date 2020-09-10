import React, { useState } from 'react';
import PropTypes from 'prop-types';

import ViewObserver from './ViewObserver';
import BasicExperience from './BasicExperience';
import EducationExperience from './EducationExperience';
import WorkExperience from './WorkExperience';
import ProjectExperience from './ProjectExperience';
import VolunteerExperience from './VolunteerExperience';
import CertificateExperience from './CertificateExperience';
import RatingDetail from '../RatingDetail/RatingDetail';

import styles from '../../styles/ExperiencesForm.module.css';

const ExperiencesForm = ({ useObserver }) => {
    const [visible, setVisible] = useState(false);
    return (
        <div className={styles.container}>
            <p>
                TODO: update progress bar{' '}
                <button
                    className="toggle-rating"
                    onClick={() => {
                        setVisible(!visible);
                    }}
                >
                    toggle rating
                </button>
            </p>
            <ViewObserver enabled={useObserver}>
                <BasicExperience />
                <EducationExperience />
                <WorkExperience />
                <ProjectExperience />
                <VolunteerExperience />
                <CertificateExperience />
            </ViewObserver>
            <RatingDetail visible={visible} />
        </div>
    );
};

ExperiencesForm.propTypes = {
    useObserver: PropTypes.bool.isRequired
};

export default ExperiencesForm;
