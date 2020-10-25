import React, {useState} from 'react';
import PropTypes from 'prop-types';

import ViewObserver from './ViewObserver';
import BasicExperience from './BasicExperience';
import EducationExperience from './EducationExperience';
import WorkExperience from './WorkExperience';
import ProjectExperience from './ProjectExperience';
import VolunteerExperience from './VolunteerExperience';
import CertificateExperience from './CertificateExperience';
import ProgressHeader from '../ProgressHeader/index';


import styles from '../../styles/ExperiencesForm.module.css';

const ExperiencesForm = ({ useObserver, resumeName }) => {
    const [scoreVisible, setScoreVisible] = useState(false);
    return (
        <div className={styles.container}>
            <ProgressHeader setScoreVisible={setScoreVisible} scoreVisible={scoreVisible}/>
            <h1 className={styles.resumeName}>
              {resumeName}
            </h1>
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
};

ExperiencesForm.propTypes = {
    useObserver: PropTypes.bool.isRequired
};

export default ExperiencesForm;
