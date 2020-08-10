import React from 'react';
import PropTypes from 'prop-types';

import ViewObserver from './ViewObserver';
import BasicExperience from './BasicExperience';
import EducationExperience from './EducationExperience';
import WorkExperience from './WorkExperience';
import ProjectExperience from './ProjectExperience';
import VolunteerExperience from './VolunteerExperience';
import CertificateExperience from './CertificateExperience';

import styles from '../../styles/ExperiencesForm.module.css';

const ExperiencesForm = ({ useObserver }) => (
    <div className={styles.container}>
        <p>TODO: update progress bar</p>
        <ViewObserver enabled={useObserver}>
            <BasicExperience />
            <EducationExperience />
            <WorkExperience />
            <ProjectExperience />
            <VolunteerExperience />
            <CertificateExperience />
        </ViewObserver>
    </div>
);

ExperiencesForm.propTypes = {
    useObserver: PropTypes.bool.isRequired
};

export default ExperiencesForm;