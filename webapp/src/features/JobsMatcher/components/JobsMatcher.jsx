import React, { useState } from 'react';
import { useSelector } from 'react-redux';

import JobsService from 'shell/services/JobsServices';
import { I8nContext } from 'shell/i18n';
import { selectLanguage } from 'features/App/slicer';

import SearchHeader from './SearchHeader';
import Jobs from './Jobs';

import zh from '../i18n/zh.json';
import en from '../i18n/en.json';

const jobServices = new JobsService();

const JobMatcher = () => {
    const language = useSelector(selectLanguage);
    const messages = language === 'zh' ? zh : en;

    const [searchResults, setSearchResults] = useState({});
    const [resultsPageNumber, setResultsPageNumber] = useState(0);
    const [query, setQuery] = useState();
    const [country, setCountry] = useState();
    const [city, setCity] = useState();

    const search = async(query, country, city, pageNumber) => {
        const data = await jobServices.getJobs(query, country, city, pageNumber);
        setSearchResults(data);
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

    return (
        <I8nContext.Provider value={messages}>
            <div className="features job-matcher">
                <SearchHeader onSearch={handleSearch} />
                <Jobs data={searchResults} pageNumber={resultsPageNumber}
                    onPageChange={handlePageChange} />
            </div>
        </I8nContext.Provider>
    );
};

export default JobMatcher;