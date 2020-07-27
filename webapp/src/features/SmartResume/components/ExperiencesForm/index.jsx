import React from 'react';
import PropTypes from 'prop-types';

import ViewObserver from './ViewObserver';
import BasicExperience from './BasicExperience';
import EducationExperience from './EducationExperience';

import styles from '../../styles/ExperiencesForm.module.css';

const ExperiencesForm = ({ useObserver }) => (
    <div className={styles.container}>
        <p>TODO: update progress bar</p>
        <ViewObserver enabled={useObserver}>
            <BasicExperience />
            <EducationExperience />
        </ViewObserver>
    </div>
);

ExperiencesForm.propTypes = {
    useObserver: PropTypes.bool.isRequired
};

export default ExperiencesForm;