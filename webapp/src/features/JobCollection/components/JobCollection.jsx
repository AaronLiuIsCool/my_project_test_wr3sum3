import React, { useEffect, useState } from 'react';

import { useSelector } from 'react-redux';
import { I8nContext } from 'shell/i18n';
import { selectLanguage, selectUserId } from 'features/App/slicer';
import zh from '../i18n/zh.json';
import en from '../i18n/en.json';
import moment from 'moment';

import JobCard from './JobCard';
import { ToggleButton, ToggleButtonGroup, Dropdown } from 'react-bootstrap';
import AccountServices from 'shell/services/AccountServices';
import MatchingServices from 'shell/services/MatchingServices';
import { getLogger } from 'shell/logger';

import './JobCollection.scss';

const accountServices = new AccountServices();
const matchingServices = new MatchingServices();
const JDBC_DATE_FORMAT = 'YYYY-MM-DD';
const logger = getLogger('JOBCOLLECTION');
const options = [0, 1];


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
        const responseJson = await accountServices.getAccountInfo(userId);
        if (responseJson.success) {
            return responseJson.account.resumes.map(
                (resume) => resume.resumeId
            );
        } else {
            logger.error(responseJson.message);
            return [];
        }
    } catch (exception) {
        logger.error(exception);
    }
};
const getAllBookmarkJobs = async (resumeIds) => {
    const bookmarkJobCalls = resumeIds.map(async (resumeId) => {
        const responseJson = await matchingServices.findBookMarkJobs(resumeId);
        return responseJson?.jobList?.jobs;
    });
    const results = (await Promise.all(bookmarkJobCalls)).reduce((acc, cur) => {
        return acc.concat(cur);
    }, []);
    return results;
};

const getAllTailorJobs = async (resumeIds) => {
    const tailorJobCalls = resumeIds.map(async (resumeId) => {
        const responseJson = await matchingServices.findTailorJobs(resumeId);
        return responseJson.jobDto === null ? [] : responseJson.jobDto;
    });
    const results = (await Promise.all(tailorJobCalls)).reduce((acc, cur) => {
        return acc.concat(cur);
    }, []);
    return results;
};
const jobStore = {};
const tailoredJobs = new Set();
const bookmarkedJobs = new Set();

const JobCollection = () => {
    const userId = useSelector(selectUserId);

    const language = useSelector(selectLanguage);
    const messages = language === 'zh' ? zh : en;

    const [jobsToBeDisplayed, setJobsToBeDisplayed] = useState([]);
    const [chosenValue, setChosenValue] = useState(options[0]);

    ///let chosenValue = options[0];
const OnSelect = (eventKey, event) =>{ // user can change the "default value"
    setChosenValue(eventKey)
    jobsToBeDisplayed.sort((a, b) => (
        chosenValue === null || chosenValue === 0  ?  new Date(b.postDate).getTime() - new Date(a.postDate).getTime() :
        new Date(a.postDate).getTime() - new Date(b.postDate).getTime()
        ))
    setJobsToBeDisplayed(jobsToBeDisplayed)
    console.log(jobsToBeDisplayed)
}

    const applyFilter = (bookmark) => {
        if (bookmark) {
            setJobsToBeDisplayed(Array.from(bookmarkedJobs).map(id => jobStore[id]));
        } else {
            setJobsToBeDisplayed(Array.from(tailoredJobs).map(id => jobStore[id]));
        }
    };

    useEffect(() => {
        getAllResumeIds(userId).then((res) => {
            if (res.length > 0) {
                getAllBookmarkJobs(res).then((allBookmarkJobs) => {
                    allBookmarkJobs.forEach((aJob) => (jobStore[aJob.jobUuid] = aJob))
                    allBookmarkJobs.forEach((aJob) => (bookmarkedJobs.add(aJob.jobUuid)))
                    setJobsToBeDisplayed(Array.from(bookmarkedJobs).map(id => jobStore[id]));
                });
                getAllTailorJobs(res).then((allTailorJobs) => {
                    allTailorJobs.forEach((aJob) => (tailoredJobs.add(aJob.jobUuid)))
                });
            }
        });
        // eslint-disable-next-line
    }, []);

    return (
        <I8nContext.Provider value={messages}>
            <div className="features padding-for-nav job-collection">
            <div>
                {/* <h1>{messages.heading}</h1> */}
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
                        {/* <ToggleButton className="tailor" value={'tailor'}>
                            {messages['tailoredJobs']}
                        </ToggleButton> */}
                    </ToggleButtonGroup>
                </div>
                <div className="sort-container">
                    <p className="sort-container header">排序</p>
                    <Dropdown onSelect={OnSelect} className="sort-container dropdown">
                        <Dropdown.Toggle className="sort-container dropdown" >
                        {chosenValue === null || chosenValue === 0  ?  messages.sortByTimestampAscending : messages.sortByTimestampDecending }
                    </Dropdown.Toggle>
                        <Dropdown.Menu className="sort-container dropdown">
                        {options.map(option => (
                            <Dropdown.Item className="sort-container dropdown"
                            eventKey={option}
                            key={option}>
                            {option === 0  ?  messages.sortByTimestampAscending : messages.sortByTimestampDecending }
                            </Dropdown.Item>
                        ))}
                    </Dropdown.Menu>
                    </Dropdown>
                </div>
            </div>
                <div className="jobCards all">
                {jobsToBeDisplayed.map((job, index) => (
                        <JobCard
                            key={job.jobUuid + index}
                            id={job.jobUuid}
                            applied={false}
                            title={job.title}
                            url={job.url}
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
