import React, { useEffect, useState }  from 'react';

import Button from 'react-bootstrap/Button';
import { ReactComponent as BookmarkIconEmpty } from '../../../assets/bookmark.svg';
import { ReactComponent as BookmarkIconFilled } from '../../../assets/bookmarked.svg';
// import { ReactComponent as ChatIcon } from '../../../assets/chat-message-sent.svg';

import { timeSince } from '../../../utils/time';
import { useI8n } from 'shell/i18n';
import MatchingServices from 'shell/services/MatchingServices';

import styles from '../../../styles/JobDetails.module.css';

const matchingServices = new MatchingServices();

const BookmarkIcon = ({ bookmarked = false }) => {
    if (bookmarked) {
        return <BookmarkIconFilled className={styles["job-details-action-icon"]} />;
    }
    return <BookmarkIconEmpty className={styles["job-details-action-icon"]} />;
}

const DetailHeader = ({ resumeId, data }) => {
    const [bookmarked, bookmarkJob] = useState(false);
    const messages = useI8n();
    
    useEffect(() => {
        // TODO: check if this job has been bookmarked
    }, [data]);

    const handleBookmark = async () => {
        const response = await matchingServices.bookmarkJob(resumeId, data?.uuid);
        if (response?.success === true) {
            bookmarkJob(!bookmarked);
        }
    }

    return (
        <div className={styles["job-details-header"]}>
            <div className={styles["job-details-title"]}>
                {data.positionTitle}
                <span className={styles["job-details-title-off"]}>{timeSince(data?.postDate)}</span>
            </div>
            <div className={styles["job-details-subtitle"]}>
                <span className={styles["job-details-subtitle-company"]}>{data?.companyName}</span>
                <span className={styles["job-details-subtitle-location"]}>{`${data?.location?.country || ''} ${data?.location?.city || ''}`}</span>
                {/* <span className={styles["job-details-subtitle-salary"]}>{`${data.salaryMin} ~ ${data.salaryMax}`}</span> */}
            </div>
            <div className={styles["job-details-actions"]}>
                <div className={styles["job-details-actions-left"]}>
                    <span className={styles["job-details-action"]} onClick={handleBookmark}>
                        <BookmarkIcon bookmarked={bookmarked} />
                        {messages["job-details-save"]}
                    </span>
                    {/* <span className={styles["job-details-action"]}>
                        <ChatIcon className={styles["job-details-action-icon"]} />
                        {messages["job-details-referral"]}
                    </span> */}
                </div>
                <div className={styles["job-details-actions-right"]}>
                    <Button className={styles["job-details-action-apply"]} 
                        href={data?.url} target='_blank'>
                        {messages["job-details-apply"]}
                    </Button>
                </div>
            </div>
        </div>
    );
};

export default DetailHeader;