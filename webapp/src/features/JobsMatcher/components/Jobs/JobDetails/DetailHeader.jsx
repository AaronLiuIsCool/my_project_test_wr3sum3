import React from 'react';

import Button from 'react-bootstrap/Button';
import { ReactComponent as BookmarkIcon } from '../../../assets/bookmark.svg';
import { ReactComponent as ChatIcon } from '../../../assets/chat-message-sent.svg';

import { useI8n } from 'shell/i18n';

import styles from '../../../styles/JobDetails.module.css';

const DetailHeader = ({ data }) => {
    const messages = useI8n();

    return (
        <div className={styles["job-details-header"]}>
            <div className={styles["job-details-title"]}>
                {data.title}
                <span className={styles["job-details-title-off"]}>刚刚发布</span>
            </div>
            <div className={styles["job-details-subtitle"]}>
                <span className={styles["job-details-subtitle-company"]}>{data.company_name}</span>
                <span className={styles["job-details-subtitle-location"]}>{data.location}</span>
                <span className={styles["job-details-subtitle-salary"]}>5万~6万</span>
            </div>
            <div className={styles["job-details-actions"]}>
                <div className={styles["job-details-actions-left"]}>
                    <span className={styles["job-details-action"]}>
                        <BookmarkIcon className={styles["job-details-action-icon"]} />
                        {messages["job-details-save"]}
                    </span>
                    <span className={styles["job-details-action"]}>
                        <ChatIcon className={styles["job-details-action-icon"]} />
                        {messages["job-details-referral"]}
                    </span>
                </div>
                <div className={styles["job-details-actions-right"]}>
                    <Button className={styles["job-details-action-apply"]}>
                        {messages["job-details-apply"]}
                    </Button>
                </div>
            </div>
        </div>
    );
};

export default DetailHeader;