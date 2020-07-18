import React from 'react';
import PropTypes from 'prop-types';
import { useSelector } from 'react-redux';

import {selectEmployers} from '../slicers/resumeSlicer';

import styles from '../styles/Experience.module.css';

const Experience = ({name, title}) => (
    <div className={styles.experienceContainer}>
        <p>{name}</p>
        <p>{title}</p>
    </div>
);

Experience.propTypes = {
    name: PropTypes.string,
    title: PropTypes.string
};

const WorkExperience = () => {
    const employers = useSelector(selectEmployers);

    return (
        <div>
            {employers.map(employer =>
                <Experience
                    name={employer.name}
                    title={employer.title}
                />
            )}
        </div>
    );
}

export default WorkExperience;
