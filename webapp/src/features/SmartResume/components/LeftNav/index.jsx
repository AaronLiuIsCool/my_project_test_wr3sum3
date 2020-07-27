import React from 'react';
import { useSelector } from 'react-redux';

import NavItem from './NavItem';
import { selectStep } from '../../slicer';

import { useI8n } from 'shell/i18n';

import nav from './nav.json';
import styles from '../../styles/LeftNav.module.css';

const getItems = (messages, stepIndex) => (
    nav.map((name, index) => (
        <NavItem
            index={index}
            selected={index === stepIndex }
            name={messages[name]}
            path={name}
            key={name} />
    ))
);

const LeftNav = () => {
    const messages = useI8n();
    const stepIndex = useSelector(selectStep);

    return (
        <div className={styles.container}>
            <div className={styles.content}>
                {getItems(messages, stepIndex)}
            </div>
        </div>
    );
}

export default LeftNav;