import React, { useEffect, useState } from 'react';

import { useSelector } from 'react-redux';
import { I8nContext } from 'shell/i18n';
import { selectLanguage, selectUserId } from 'features/App/slicer';
import zh from '../i18n/zh.json';
import en from '../i18n/en.json';
import moment from 'moment';

import JobCard from './JobCard';
import { ToggleButton, ToggleButtonGroup } from 'react-bootstrap';
import AccountServices from 'shell/services/AccountServices';
import MatchingServices from 'shell/services/MatchingServices';
import { getLogger } from 'shell/logger';

import './JobCollection.scss';

const accountServices = new AccountServices();
const matchingServices = new MatchingServices();
const JDBC_DATE_FORMAT = 'YYYY-MM-DD';
const logger = getLogger('JOBCOLLECTION');

// TODO: 这个可能会需要 因为可能一个有两个resume有同一个收藏的job
// const removeDuplicates = (arr, key) => {
//     const res = [];
//     const hash = {}
//     arr.forEach(item => {
//         if(hash[item[key]] === undefined) {
//             hash[item[key]] = true
//             res.push(item)
//         }
//     })
//     return res
// }
const formatLocation = (location = {}) => {
    return `${location.city} - ${location.country}`;
};
const getAllResumeIds = async (userId) => {
    try {
        const response = await accountServices.getAccountInfo(userId);
        if (!response) {
            logger.warn('No response from account service get');
        }
        const responseJson = await response.json();
        if (responseJson.success) {
            return responseJson.account.resumes.map(
                (resume) => resume.resumeId
            );
        } else {
            logger.error(response.message);
            return [];
        }
    } catch (exception) {
        logger.error(exception);
    }
};
const getAllBookmarkJobs = async (resumeIds) => {
    const bookmarkJobCalls = resumeIds.map(async (resumeId) => {
        const response = await matchingServices.findBookMarkJobs(resumeId);
        if (!response) {
            logger.warn('No response from account service get');
        }
        const responseJson = await response.json();
        return responseJson.jobList.jobs;
    });
    const results = (await Promise.all(bookmarkJobCalls)).reduce((acc, cur) => {
        return acc.concat(cur);
    }, []);
    return results;
};

const getAllTailorJobs = async (resumeIds) => {
    const tailorJobCalls = resumeIds.map(async (resumeId) => {
        const response = await matchingServices.findTailorJobs(resumeId);
        if (!response) {
            logger.warn('No response from account service get');
        }
        const responseJson = await response.json();
        return responseJson.jobDto === null ? [] : responseJson.jobDto;
    });
    const results = (await Promise.all(tailorJobCalls)).reduce((acc, cur) => {
        return acc.concat(cur);
    }, []);
    return results;
};
const tailoredJobs = [];
const bookmarkedJobs = [];
const JobCollection = () => {
    const userId = useSelector(selectUserId);

    const language = useSelector(selectLanguage);
    const messages = language === 'zh' ? zh : en;

    const [jobsToBeDisplayed, setJobsToBeDisplayed] = useState([]);

    const applyFilter = (bookmark) => {
        if (bookmark) {
            setJobsToBeDisplayed(bookmarkedJobs);
        } else {
            setJobsToBeDisplayed(tailoredJobs);
        }
    };

    useEffect(() => {
        getAllResumeIds(userId).then((res) => {
            if (res.length > 0) {
                getAllBookmarkJobs(res).then((allBookmarkJobs) => {
                    bookmarkedJobs.push(...allBookmarkJobs);
                    setJobsToBeDisplayed(bookmarkedJobs);
                });
                getAllTailorJobs(res).then((allTailorJobs) => {
                    tailoredJobs.push(...allTailorJobs);
                });
            }
        });
        // eslint-disable-next-line
    }, []);

    return (
        <I8nContext.Provider value={messages}>
            <div className="features padding-for-nav job-collection">
                <h1>{messages.heading}</h1>
                <div className="btn-container">
                    <ToggleButtonGroup
                        type="radio"
                        name="collectio-select"
                        defaultValue="bookmark"
                        onChange={(e) => {
                            applyFilter(e === 'bookmark');
                        }}
                    >
                        <ToggleButton className="bookmark" value={'bookmark'}>
                            {messages['bookmarkedJobs']}
                        </ToggleButton>
                        <ToggleButton className="tailor" value={'tailor'}>
                            {messages['tailoredJobs']}
                        </ToggleButton>
                    </ToggleButtonGroup>
                </div>
                <div>
                    {jobsToBeDisplayed.map((job, index) => (
                        <JobCard
                            key={job.jobUuid + index}
                            id={job.jobUuid}
                            applied={false}
                            title={job.title}
                            company={job.companyName}
                            date={moment(new Date(job.postDate)).format(
                                JDBC_DATE_FORMAT
                            )}
                            location={formatLocation(job.location)}
                        ></JobCard>
                    ))}
                </div>
            </div>
        </I8nContext.Provider>
    );
};

export default JobCollection;
