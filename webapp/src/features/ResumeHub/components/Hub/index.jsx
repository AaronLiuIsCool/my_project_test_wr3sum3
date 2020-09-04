import React from 'react';
import PropTypes from 'prop-types';

import Grid from './Grid';
import Item from './Item';

import styles from '../../styles/Hub.module.css';

function renderGrid(resumes) {
    const resumeItems = resumes.map(resume => <Item resume={resume} key={resume.resumeId} />);
    return <Grid>{resumeItems}</Grid>;
}

const Hub = ({ resumes }) => (
    <div className={styles.container}>
        <div className={styles.header}>
            <span className={styles.title}>
                我的简历
            </span>
            <a className={styles.action} href='/resumes/new'>
                + 添加新的简历
            </a>
        </div>
        {renderGrid(resumes)}
    </div>
);

Hub.propTypes = {
    resumes: PropTypes.arrayOf(PropTypes.shape({
        resumeId: PropTypes.string.isRequired,
        alias: PropTypes.string.isRequired,
        thumbnailUri: PropTypes.string.isRequired,
        createdAt: PropTypes.string.isRequired
    }))
};

export default Hub;