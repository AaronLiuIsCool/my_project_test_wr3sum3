import React from 'react';

import { Button } from 'react-bootstrap';
import { useSelector } from 'react-redux';

import { selectAccountName } from '../slicer/account';

import { useI8n } from 'shell/i18n';
import configs from 'shell/configs';

import styles from '../styles/Actions.module.css';

export function generateInitials(name, defaultInitial) {
    if (typeof name !== 'string' || name.trim().length === 0) {
        return defaultInitial;
    }

    name = name.trim();
    const nameParts = name.split(' ');
    if (nameParts.length === 1) {
        return name.substring(0, Math.min(name.length, 2)).toUpperCase();
    }

    const initial = nameParts[0].charAt(0) + nameParts[1].charAt(0);
    return initial.toUpperCase();
}

const Actions = () => {
    const messages = useI8n();
    const accountName = useSelector(selectAccountName);

    return (
        <div className={styles.container}>
            <Button className={styles.logoutBtn} variant='link' href={configs['logout-url']}>
                {messages.logout}
            </Button>

            {/* TODO: Joseph, please add account management here */}
            <div className={styles.userAvatar}>
                {generateInitials(accountName, messages.defaultInitial)}
            </div>
        </div>
    );
}

export default Actions;