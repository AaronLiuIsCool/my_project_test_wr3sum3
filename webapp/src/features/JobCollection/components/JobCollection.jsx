import React, { useState } from 'react';

import { useSelector } from 'react-redux';
import { I8nContext } from 'shell/i18n';
import { selectLanguage } from 'features/App/slicer';
import zh from '../i18n/zh.json';
import en from '../i18n/en.json';

import './JobCollection.scss';

import JobCard from './JobCard';
import { ToggleButton, ToggleButtonGroup } from 'react-bootstrap';

const DUMMY_DATA = [
    {
        id: 'ID1',
        applied: false,
        title: '金融客户经理',
        company: '中国托业银行',
        date: '2018/10月/30号',
        location: '北京'
    },
    {
        id: 'ID2',
        applied: true,
        title: '大堂经理',
        company: '中国兴业银行',
        date: '2019/10月/30号',
        location: '上海'
    },
    {
        id: 'ID3',
        applied: false,
        title: '客服经理',
        company: '阿里巴巴',
        date: '2020/10月/30号',
        location: '杭州'
    }
];

const JobCollection = () => {
    const language = useSelector(selectLanguage);
    const messages = language === 'zh' ? zh : en;

    const [jobsToBeDisplayed, setJobsToBeDisplayed] = useState(DUMMY_DATA);

    const applyFilter = (applied) => {
        if (applied) {
            const res = jobsToBeDisplayed.filter((job) => job.applied === true);
            setJobsToBeDisplayed(res);
        } else {
            setJobsToBeDisplayed(DUMMY_DATA);
        }
    };

    return (
        <I8nContext.Provider value={messages}>
            <div className="features job-collection">
                <h1>{messages.heading}</h1>
                <div className="btn-container">
                    <ToggleButtonGroup
                        type="radio"
                        name="collectio-select"
                        defaultValue="all"
                        onChange={(e) => {
                            applyFilter(e === 'all');
                        }}
                    >
                        <ToggleButton className="all" value={'all'}>
                            {messages['all']}
                        </ToggleButton>
                        <ToggleButton className="applied" value={'applied'}>
                            {messages['applied']}
                        </ToggleButton>
                    </ToggleButtonGroup>
                </div>

                {jobsToBeDisplayed.map((job) => (
                    <JobCard
                        key={job.id}
                        id={job.id}
                        applied={job.applied}
                        title={job.title}
                        company={job.company}
                        date={job.date}
                        location={job.location}
                    ></JobCard>
                ))}
            </div>
        </I8nContext.Provider>
    );
};

export default JobCollection;
