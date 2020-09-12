import React from 'react';

import Button from 'react-bootstrap/Button';
import { ReactComponent as BookmarkIcon } from '../../../assets/bookmark.svg';
import { ReactComponent as ChatIcon } from '../../../assets/chat-message-sent.svg';

import { useI8n } from 'shell/i18n';
import { timeSince } from '../../../utils/time';

import styles from '../../../styles/JobDetails.module.css';

const DetailHeader = ({ data, showExtraInfo=true }) => {
    const messages = useI8n();

    return (
        <div className={styles["job-details-header"]}>
            <div className={styles["job-details-title"]}>
                {data.positionTitle}
                <span className={styles["job-details-title-off"]}>{timeSince(data.postDate)}</span>
            </div>
            <div className={styles["job-details-subtitle"]}>
                <span className={styles["job-details-subtitle-company"]}>{data.companyName}</span>
                <span className={styles["job-details-subtitle-location"]}>{`${data.location.country} ${data.location.city}`}</span>
                <span className={styles["job-details-subtitle-salary"]}>{`${data.salaryMin} ~ ${data.salaryMax}`}</span>
            </div>
            {showExtraInfo && (<div className={styles["job-details-actions"]}>
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
            </div>)}
        </div>
    );
};

export default DetailHeader;