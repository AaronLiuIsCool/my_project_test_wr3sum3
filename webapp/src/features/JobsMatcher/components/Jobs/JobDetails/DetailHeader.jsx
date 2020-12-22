import React, { useEffect, useState }  from 'react';
import { useDispatch, useSelector } from 'react-redux';

import Button from 'react-bootstrap/Button';
import { ReactComponent as BookmarkIconEmpty } from '../../../assets/bookmark.svg';
import { ReactComponent as BookmarkIconFilled } from '../../../assets/bookmarked.svg';
// import { ReactComponent as ChatIcon } from '../../../assets/chat-message-sent.svg';

import { timeSince } from '../../../utils/time';
import { useI8n } from 'shell/i18n';
import MatchingServices from 'shell/services/MatchingServices';
import { selectBookmarkedJobs, addBookmark, removeBookmark } from 'features/JobsMatcher/slicer';

import styles from '../../../styles/JobDetails.module.css';

const matchingServices = new MatchingServices();

const BookmarkIcon = ({ bookmarked = false }) => {
    if (bookmarked) {
        return <BookmarkIconFilled className={styles["job-details-action-icon"]} />;
    }
    return <BookmarkIconEmpty className={styles["job-details-action-icon"]} />;
}

const DetailHeader = ({ resumeId, data }) => {
    const dispatch = useDispatch();
    const bookmarkedJobs = useSelector(selectBookmarkedJobs);
    const [bookmarked, bookmarkJob] = useState(false);
    const messages = useI8n();

    useEffect(() => {
        bookmarkJob(bookmarkedJobs.indexOf(data?.uuid) > -1)
    }, [data, bookmarkedJobs]);

    const handleBookmark = async () => {
        const jobUuid = data?.uuid;
        
        if (!jobUuid) {
            return;
        }

        const response = bookmarked ? 
            await matchingServices.unbookmarkJob(resumeId, jobUuid) :
            await matchingServices.bookmarkJob(resumeId, jobUuid);;

        if (response?.success === true) {
            dispatch(bookmarked ? 
                removeBookmark(jobUuid) : addBookmark(jobUuid));
            bookmarkJob(!bookmarked);
        }
    }

    return (
        <div className={styles["job-details-header"]}>
            <div className={styles["job-details-title"]}>
                <span className={styles["job-details-title-content"]}>{data.positionTitle}</span>
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