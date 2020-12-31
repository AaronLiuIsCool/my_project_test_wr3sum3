import React from 'react';
import './JobCard.scss';
import { Button } from 'react-bootstrap';
import { useI8n } from 'shell/i18n';

import MatchingServices from 'shell/services/MatchingServices';
const matchingServices = new MatchingServices();

const JobCard = ({ applied, title, url, company, date, location, jobUuid, resumeId, removeBookmark }) => {
    const messages = useI8n();

    const handleRemove = async (event) => {
        const response = await matchingServices.unbookmarkJob(resumeId, jobUuid);

        if (response?.success === true) {
            removeBookmark(jobUuid);
        }
    };

    return (
        <div className="job-card">
            <div className="content">
                <div className="left">
                    <h2>
                        {title}
                        {applied && (
                            <span>
                                {messages['applied']}
                                <img
                                    width="12"
                                    src={require('../asset/Checkmark@3x.png')}
                                    alt="checkmark"
                                />
                            </span>
                        )}
                    </h2>
                    <div>
                        <span className="company">{company}</span>
                        <span className="date">
                            {messages['postedDate']}：{date}
                        </span>
                        <span className="location">
                            {messages['jobLocation']}： {location}
                        </span>
                    </div>
                </div>
                <div className="right">
                    {!applied && (
                        <Button
                            data-id={jobUuid}
                            className="apply-btn"
                            href={url}
                        >
                            {messages['applyJob']}
                        </Button>
                    )}
                </div>
            </div>
            <div data-id={jobUuid} className="remove" onClick={handleRemove}>
                <img
                    width="14"
                    src={require('../asset/icons8-delete-bin@3x.png')}
                    alt="remove"
                />
            </div>
        </div>
    );
};

export default JobCard;