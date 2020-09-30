import React, { Fragment, useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { useSelector } from 'react-redux';

import { getLogger } from 'shell/logger';
import ResumeServices from 'shell/services/ResumeServices';
import MatchingServices from 'shell/services/MatchingServices';
import { I8nContext } from 'shell/i18n';
import { selectLanguage, selectUserId } from 'features/App/slicer';

import JobRefinement from './Jobs/JobRefinement';
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
        const response = await resumeServices.getResumeMatchingInfo(resumeId);
        resumeDto = await response.json();
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

const JobMatcher = ({ resume }) => {
    const language = useSelector(selectLanguage);
    const messages = language === 'zh' ? zh : en;

    const userId = useSelector(selectUserId);
    const [searchResults, setSearchResults] = useState({});
    const [resultsPageNumber, setResultsPageNumber] = useState(0);
    const [query, setQuery] = useState();
    const [country, setCountry] = useState();
    const [city, setCity] = useState();
    const [ready, setReady] = useState(false);

    const [modalOpened, setModalOpened] = useState(false); 

    const search = async (query, country, city, pageNumber) => {
        try {
            const response = await findMatchingJobs(query, country, city, pageNumber);
            const data = await response.json();
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

        search(query, country, city, 0);
    };

    const handlePageChange = (pageNumber) => {
        setResultsPageNumber(pageNumber);
        search(query, country, city, pageNumber);
    }

    useEffect(() => {
        getResumeMatchingInfo(resume).then((resumeDto) => {
            matchingServices.setContext({...resumeDto, userId});
            handleSearch();
            setReady(true);
        });
    }, []); // eslint-disable-line

    if (!ready) {
        return Fragment;
    }

    return (
        <I8nContext.Provider value={messages}>
            <div className="features job-matcher">
                <SearchHeader onSearch={handleSearch} initial={{query, country, city}} />
                <Jobs data={searchResults} pageNumber={resultsPageNumber}
                    onPageChange={handlePageChange} modalOpenHandler={setModalOpened}/>
                {modalOpened && <JobRefinement data={searchResults} modalOpenHandler={setModalOpened} />}
            </div>
        </I8nContext.Provider>
    );
};

JobMatcher.propTypes = {
    resume: PropTypes.string
};

export default JobMatcher;