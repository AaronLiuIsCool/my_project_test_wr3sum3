import React from 'react';
import './JobCard.scss';
import { Button } from 'react-bootstrap';
import { useI8n } from 'shell/i18n';

const JobCard = ({ applied, title, company, date, location, id }) => {
    const messages = useI8n();

    const handleRemove = (event) => {
        // const { id } = event.currentTarget.dataset;
    };

    const handleApply = (event) => {
        // const { id } = event.currentTarget.dataset;
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
                            {messages['appliedDate']}：{date}
                        </span>
                        <span className="location">
                            {messages['jobLocation']}： {location}
                        </span>
                    </div>
                </div>
                <div className="right">
                    {!applied && (
                        <Button
                            data-id={id}
                            className="apply-btn"
                            onClick={handleApply}
                        >
                            {messages['applyJob']}
                        </Button>
                    )}
                </div>
            </div>
            <div data-id={id} className="remove" onClick={handleRemove}>
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
