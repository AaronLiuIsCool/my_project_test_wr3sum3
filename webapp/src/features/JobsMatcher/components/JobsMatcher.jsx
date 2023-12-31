import React, { Fragment, useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';

import { getLogger } from 'shell/logger';
import ResumeServices from 'shell/services/ResumeServices';
import MatchingServices from 'shell/services/MatchingServices';
import { I8nContext } from 'shell/i18n';
import { selectLanguage, selectUserId } from 'features/App/slicer';
import { setBookmarkedList } from 'features/JobsMatcher/slicer';

import JobRefinementModal from './Jobs/JobRefinementModal';
import SearchHeader from './SearchHeader';
import Jobs from './Jobs';

import zh from '../i18n/zh.json';
import en from '../i18n/en.json';

const logger = getLogger('JobsMatcher');
const matchingServices = new MatchingServices();
const resumeServices = new ResumeServices();

async function getResumeMatchingInfo(resumeId) {
    let resumeDto = {};
    try {
        resumeDto = await resumeServices.getResumeMatchingInfo(resumeId);
    } catch (exception) {
        logger.error(exception)
    } finally {
        return resumeDto;
    }
}

async function findMatchingJobs(query, country, city, pageNumber) {
    if (!query && !country && !city) {
        return await matchingServices.findMatchingJobs(0);
    }
    return await matchingServices.searchJobs(query, country, city, pageNumber);
}

function getQuery(resumeDto = {}) {
    const {keywords = [], majors = []} = resumeDto;
    if (majors.length > 0) {
        return majors[0];
    } else if (keywords.length > 0) {
        return keywords[0];
    }
    return '';
}

function getCountry(resumeDto = {}) {
    const {location} = resumeDto;
    const country = location?.country || '';
    return country !== 'undefined' ? country : '';
}

function getCity(resumeDto = {}) {
    const {location} = resumeDto;
    const city = location?.city || '';
    return city !== 'undefined' ? city : '';
}

const JobMatcher = ({ resumeId }) => {
    const language = useSelector(selectLanguage);
    const messages = language === 'zh' ? zh : en;

    const dispatch = useDispatch();
    const userId = useSelector(selectUserId);
    const [searchResults, setSearchResults] = useState({});
    const [resultsPageNumber, setResultsPageNumber] = useState(0);
    const [query, setQuery] = useState();
    const [country, setCountry] = useState();
    const [city, setCity] = useState();
    const [ready, setReady] = useState(false);

    const [selectedJob, setSelectedJob] = useState(0);

    const [modalOpened, setModalOpened] = useState(false);

    const search = async (query, country, city, pageNumber) => {
        try {
            const data = await findMatchingJobs(query, country, city, pageNumber);
            const jobList = data.success ? data.jobList : {};
            setSearchResults(jobList);
        } catch (exception) {
            logger.error(exception);
        }
    }

    const handleSearch = (query, country, city) => {
        setSearchResults({});
        setResultsPageNumber(0);
        setQuery(query);
        setCountry(country);
        setCity(city);

        if (query){
            search(query, country, city, 0);
        }
    };

    const handlePageChange = (pageNumber) => {
        setResultsPageNumber(pageNumber);
        search(query, country, city, pageNumber);
    }


    useEffect(() => {
        getResumeMatchingInfo(resumeId).then((resumeDto) => {
            matchingServices.setContext({...resumeDto, userId, resumeUuid: resumeId});
            const query = getQuery(resumeDto);
            const country = getCountry(resumeDto);
            const city = getCity(resumeDto);
            handleSearch(query, country, city);
            setReady(true);
        });
        matchingServices.findBookMarkJobs(resumeId).then((bookmarkedJobs) => {
            if (bookmarkedJobs?.success) {
                const jobs = bookmarkedJobs?.jobList?.jobs?.map(job => job.jobUuid) || [];
                dispatch(setBookmarkedList(jobs));
            }
        });
    }, []); // eslint-disable-line

    if (!ready) {
        return Fragment;
    }

    return (
        <I8nContext.Provider value={messages}>
            <div className="features job-matcher">
                <SearchHeader onSearch={handleSearch} initial={{query, country, city}} />
                <Jobs data={searchResults} pageNumber={resultsPageNumber} resumeId={resumeId}
                    onPageChange={handlePageChange} modalOpenHandler={setModalOpened} selectedJob={selectedJob} setSelectedJob={setSelectedJob}/>
                {modalOpened && <JobRefinementModal data={searchResults} modalOpenHandler={setModalOpened} selectedJob={selectedJob}/>}
            </div>
        </I8nContext.Provider>
    );
};

JobMatcher.propTypes = {
    resumeId: PropTypes.string,
    page: PropTypes.number
};

export default JobMatcher;