import React from 'react';
import PropTypes from 'prop-types';

import Button from 'react-bootstrap/Button';
import { Link } from 'react-router-dom'
import Grid from './Grid';
import Item from './Item';

import { useI8n } from 'shell/i18n';

import styles from '../../styles/Hub.module.css';

function renderGrid(resumes) {
    const resumeItems = resumes.map(resume => <Item resume={resume} key={resume.resumeId} />);
    return <Grid>{resumeItems}</Grid>;
}

const Hub = ({ resumes }) => {
    const messages = useI8n();
    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <span className={styles.title}>
                {messages['hub_title']}
            </span>
            <Button as={Link} variant="link" className={styles.action} to='/new'>
                {messages['hub_action']}
            </Button>
            </div>
            {renderGrid(resumes)}
        </div>
    );
}

Hub.propTypes = {
    resumes: PropTypes.arrayOf(PropTypes.shape({
        resumeId: PropTypes.string.isRequired,
        alias: PropTypes.string.isRequired,
        thumbnailUri: PropTypes.string.isRequired,
        createdAt: PropTypes.string.isRequired
    }))
};

export default Hub;