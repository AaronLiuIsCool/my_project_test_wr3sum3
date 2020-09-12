import React, { Fragment, useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { useSelector } from 'react-redux';

import { getLogger } from 'shell/logger';
import ResumeServices from 'shell/services/ResumeServices';
import JobsServices from 'shell/services/JobsServices';
import { I8nContext } from 'shell/i18n';
import { selectLanguage } from 'features/App/slicer';

import JobRefinement from './Jobs/JobRefinement';
import SearchHeader from './SearchHeader';
import Jobs from './Jobs';

import zh from '../i18n/zh.json';
import en from '../i18n/en.json';

const logger = getLogger('JobsMatcher');
const jobsServices = new JobsServices();
const resumeServices = new ResumeServices();

async function getResumeInfo(resumeId) {
    let resumeData = {};
    try {
        const response = await resumeServices.getResume(resumeId);
        resumeData = await response.json();
    } catch (exception) {
        logger.error(exception)
    } finally {
        return resumeData;
    }
}

const JobMatcher = ({ resume }) => {
    const language = useSelector(selectLanguage);
    const messages = language === 'zh' ? zh : en;

    const [searchResults, setSearchResults] = useState({});
    const [resultsPageNumber, setResultsPageNumber] = useState(0);
    const [query, setQuery] = useState('');
    const [country, setCountry] = useState('');
    const [city, setCity] = useState('');
    const [ready, setReady] = useState(false);

    const [modalOpened, setModalOpened] = useState(false); 

    const search = async (query, country, city, pageNumber) => {
        try {
            const response = await jobsServices.getJobs(query, country, city, pageNumber);
            const data = await response.json();
            
            setSearchResults(data);
        } catch (exception) {
            logger.error(exception);
        }
    }

    const handleSearch = (query = '', country = '', city = '') => {
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
        if (!resume) {
            handleSearch();
            setReady(true);
            return;
        }

        getResumeInfo(resume).then(({ educations = [], workExperiences = [] }) => {
            if (workExperiences.length > 0) {
                const work = workExperiences[0];
                handleSearch(work.role);
            } else if (educations.length > 0) {
                const education = educations[0];
                handleSearch(education.major);
            } else {
                handleSearch();
            }
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