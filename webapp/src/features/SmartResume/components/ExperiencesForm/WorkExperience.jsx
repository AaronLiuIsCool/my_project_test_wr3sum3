import React from 'react';
import PropTypes from 'prop-types';
import { useSelector } from 'react-redux';

import { selectEmployers } from '../../slicer';

import styles from '../../styles/Experience.module.css';

const Experience = ({ name, title }) => (
    <div className={styles.container}>
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
                    key={`${employer.name}-${employer.title}`}
                    name={employer.name}
                    title={employer.title}
                />
            )}
        </div>
    );
}

export default WorkExperience;
