import React from 'react';
import './RatingDetail.scss';
import Dropdown from './Dropdown';

import { useI8n } from 'shell/i18n';
import { actions } from '../../slicer';
import { useDispatch } from 'react-redux';

import { useSelector } from 'react-redux';
import { ratingsSelectors } from '../../slicer/index';
import { useEffect } from 'react';
import { useState } from 'react';
import { ExpBlocks } from './ExpBlocks';

const sectionToIndexMapping = {
    basicInfo: 0,
    education: 1,
    workXp: 2,
    projectXp: 3,
    otherXp: 4,
    certifications: 5
};

const EXP_DATA_DEFAULT_SATE = {
    company: [],
    keywords: [],
    quantify: [],
    amount: [],
    sorted: []
};
const RatingDetail = () => {
    const messages = useI8n();
    const labelMappings = {
        'info-complete': messages.infoComplete,
        layout: messages.layout,
        'exp-value': messages.expValue
    };
    const options = [
        {
            value: messages.all,
            key: 'all'
        },
        {
            value: messages.infoComplete,
            key: 'info-complete'
        },
        {
            value: messages.expression,
            key: 'expression'
        },
        {
            value: messages.expValue,
            key: 'exp-value'
        },
        {
            key: 'layout',
            value: messages.layout
        }
    ];
    const { data } = useSelector(ratingsSelectors.selectRatings);
    const dispatch = useDispatch();


    const [basicInfo, setBasicInfo] = useState([]);
    const [certInfo, setCertInfo] = useState([]);
    const [layoutInfo, setLayoutInfo] = useState([]);
    const [educationInfo, setEducationInfo] = useState([]);
    const [workInfo, setWorkInfo] = useState(EXP_DATA_DEFAULT_SATE);
    const [projectInfo, setProjectInfo] = useState(EXP_DATA_DEFAULT_SATE);
    const [otherInfo, setOtherInfo] = useState(EXP_DATA_DEFAULT_SATE);

    useEffect(() => {
        setBasicInfo(data.basicInfo);
        setLayoutInfo(data.layoutInfo);
        setEducationInfo(process2DArray(data.educationInfo));
        setWorkInfo(data.workInfo);
        setProjectInfo(data.projectInfo);
        setOtherInfo(data.otherInfo);
        setCertInfo(data.certInfo);
    }, [data]);

    const applyFilter = (filter = 'all') => {
        if (filter === 'all') {
            setBasicInfo(data.basicInfo);
            setLayoutInfo(data.layoutInfo);
            setEducationInfo(process2DArray(data.educationInfo));
            setWorkInfo(data.workInfo);
            setProjectInfo(data.projectInfo);
            setOtherInfo(data.otherInfo);
            setCertInfo(data.certInfo);
            return;
        }
        setLayoutInfo(data.layoutInfo.filter((item) => item.type === filter));
        setBasicInfo(data.basicInfo.filter((item) => item.type === filter));
        setEducationInfo(
            process2DArray(data.educationInfo).filter(
                (item) => item.type === filter
            )
        );
        setCertInfo(data.certInfo.filter((item) => item.type === filter));

        // This is for all three experience sections
        if (filter === 'expression') {
            setWorkInfo({
                ...data.workInfo,
                company: [],
                amount: [],
                sorted: []
            });
            setProjectInfo({
                ...data.projectInfo,
                company: [],
                amount: [],
                sorted: []
            });
            setOtherInfo({
                ...data.otherInfo,
                company: [],
                amount: [],
                sorted: []
            });
        } else if (filter === 'exp-value') {
            setWorkInfo({
                ...data.workInfo,
                keywords: [],
                quantify: [],
                sorted: []
            });
            setProjectInfo({
                ...data.projectInfo,
                keywords: [],
                quantify: [],
                sorted: []
            });
            setOtherInfo({
                ...data.otherInfo,
                keywords: [],
                quantify: [],
                sorted: []
            });
        } else if (filter === 'layout') {
            setWorkInfo({
                ...data.workInfo,
                keywords: [],
                quantify: [],
                company: [],
                amount: []
            });
            setProjectInfo({
                ...data.projectInfo,
                keywords: [],
                quantify: [],
                company: [],
                amount: []
            });
            setOtherInfo({
                ...data.otherInfo,
                keywords: [],
                quantify: [],
                company: [],
                amount: []
            });
        } else {
            setWorkInfo(EXP_DATA_DEFAULT_SATE);
            setProjectInfo(EXP_DATA_DEFAULT_SATE);
            setOtherInfo(EXP_DATA_DEFAULT_SATE);
        }
    };

    const process2DArray = (data) => {
        return data.reduce((acc, cur) => {
            if (!!cur) {
                return acc.concat(cur);
            } else {
                return acc;
            }
        }, []);
    };

    const handleNavigate = (e) => {
        const { dataset } = e.target;
        const { section, selector } = dataset;
        jumpTo(section);
        setTimeout(() => {
            document.querySelector(selector).focus();
        }, 0)
    };
    const handleSelect = (option) => {
        applyFilter(option);
    };

    const jumpTo = (section) => {
        const elem = document.querySelector(`#${section}`);
        Array.from(document.querySelector(`#${section}`).querySelectorAll('.collapsed-section .toggle-up-arrow')).forEach(el => {
            el.click()
        })
        elem.scrollIntoView();
        dispatch(actions.moveStep(sectionToIndexMapping[section]));
    };

    const handleSpecialNavigate = (section, obj) => {
        const elem = document.querySelector(`#${section}`);
        Array.from(document.querySelector(`#${section}`).querySelectorAll('.collapsed-section .toggle-up-arrow')).forEach(el => {
            el.click()
        })
        elem.scrollIntoView();
        setTimeout(() => {
            notifyEditor(obj.eventType, obj.index, {
                data: {
                    content: obj.content,
                    keywords: obj.keywords,
                    quantify: obj.quantify
                },
                type: 'update-content'
            });
        }, 0)
        
    };
    const notifyEditor = (type, index, detail) => {
        const eventName = `${type}-${index}`;
        const event = new CustomEvent(eventName, {
            detail
        });
        window.dispatchEvent(event);
    };
    return (
        <div className="rating-detail">
            <div className="fading-container">
                <div className="sticky-section">
                    <h3>{messages.ratingDetail}</h3>
                    <Dropdown
                        messages={messages}
                        data={data}
                        options={options}
                        onSelect={handleSelect}
                    />
                </div>
                <div className="content-section">
                    {basicInfo?.length > 0 && (
                        <div className="blocks">
                            <h3>
                                {messages.basicInfo}{' '}
                                <span>({basicInfo.length})</span>
                            </h3>
                            {basicInfo.map((item, index) => (
                                <div
                                    key={index}
                                    className="action-item warning"
                                >
                                    <div className="action-details">
                                        <h4>
                                            {messages.notEnter}
                                            <span>{item.name}</span>
                                        </h4>
                                        <p>{item.message}</p>
                                        <div className="tag">
                                            <span>
                                                {labelMappings[item.type]}
                                            </span>
                                            <span className="tag-arrow"></span>
                                        </div>
                                    </div>
                                    <div className="nav-btn">
                                        { !item.noNav && <button
                                            data-selector={item.selector}
                                            data-section={item.section}
                                            onClick={handleNavigate}
                                        >
                                            {messages.jumpTo}
                                        </button>}
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                    {educationInfo?.length > 0 && (
                        <div className="blocks">
                            <h3>
                                {messages.education}{' '}
                                <span>({educationInfo.length})</span>
                            </h3>
                            {educationInfo.map((item, index) =>
                                item.type === 'info-complete' ? (
                                    <div
                                        key={index}
                                        className="action-item warning"
                                    >
                                        <div className="action-details">
                                            <h4>
                                                {messages.notEnter}
                                                <span>{item.name}</span>
                                            </h4>
                                            <p>{item.message}</p>
                                            <div className="tag">
                                                <span>
                                                    {labelMappings[item.type]}
                                                </span>
                                                <span className="tag-arrow"></span>
                                            </div>
                                        </div>
                                        <div className="nav-btn">
                                        { 
                                            !item.noNav && <button
                                                data-selector={item.selector}
                                                data-section={item.section}
                                                onClick={handleNavigate}
                                            >
                                                {messages.jumpTo}
                                            </button>
                                        }
                                        </div>
                                    </div>
                                ) : item.school ? (
                                    <div
                                        key={index}
                                        className="action-item pass"
                                    >
                                        <div className="action-details">
                                            <h4>{item.name}</h4>
                                            <p>{item.message}</p>
                                            <div className="tag">
                                                <span>{messages.expVal}</span>
                                                <span className="tag-arrow"></span>
                                            </div>
                                        </div>
                                        <div className="nav-btn">
                                            <button>{messages.jumpTo}</button>
                                        </div>
                                    </div>
                                ) : (
                                    <div
                                        key={index}
                                        className={`action-item ${
                                            item.green ? 'pass' : 'warning'
                                        }`}
                                    >
                                        <div className="action-details">
                                            <h4>{item.name}</h4>
                                            <p>{item.message}</p>
                                            <div className="tag">
                                                <span>{messages.expVal}</span>
                                                <span className="tag-arrow"></span>
                                            </div>
                                        </div>
                                        <div className="nav-btn">
                                            <button
                                                data-selector={item.selector}
                                                data-section={item.section}
                                                onClick={handleNavigate}
                                            >
                                                {messages.jumpTo}
                                            </button>
                                        </div>
                                    </div>
                                )
                            )}
                        </div>
                    )}

                    <ExpBlocks
                        messages={messages}
                        info={workInfo}
                        handleSpecialNavigate={handleSpecialNavigate}
                        jumpTo={jumpTo}
                        type={'workXp'}
                    />
                    <ExpBlocks
                        messages={messages}
                        info={projectInfo}
                        handleSpecialNavigate={handleSpecialNavigate}
                        jumpTo={jumpTo}
                        type={'projectXp'}
                    />
                    <ExpBlocks
                        messages={messages}
                        info={otherInfo}
                        handleSpecialNavigate={handleSpecialNavigate}
                        jumpTo={jumpTo}
                        type={'otherXp'}
                    />
                    {certInfo.length > 0 &&
                        certInfo.map((item, index) => (
                            <div className="blocks" key={index}>
                                <h3>
                                    {messages.certifications}{' '}
                                    <span>({certInfo.length})</span>
                                </h3>
                                <div
                                    key={index}
                                    className="action-item warning"
                                >
                                    <div className="action-details">
                                        <h4>
                                            {item.name}
                                            <span>{messages.tooLess}</span>
                                        </h4>
                                        <p>{item.message}</p>
                                        <div className="tag">
                                            <span>
                                                {labelMappings[item.type]}
                                            </span>
                                            <span className="tag-arrow"></span>
                                        </div>
                                    </div>
                                    <div className="nav-btn">
                                        <button
                                            onClick={() => {
                                                jumpTo(item.section);
                                            }}
                                        >
                                            {messages.jumpTo}
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))}

                    {layoutInfo.length > 0 &&
                        layoutInfo.map((item, index) => (
                            <div className="blocks" key={index}>
                                <h3>
                                    {messages.entireLayout} <span>(1)</span>
                                </h3>
                                <div
                                    className="action-item warning"
                                >
                                    <div className="action-details">
                                        <h4>
                                            {messages.resume}
                                            <span>{item.name}</span>
                                        </h4>
                                        {
                                            item.noNav ? <p dangerouslySetInnerHTML={{
                                                __html: item.message
                                            }}></p> : <p>{item.message}</p>
                                        }
                                        <div className="tag">
                                            <span>
                                                {labelMappings[item.type]}
                                            </span>
                                            <span className="tag-arrow"></span>
                                        </div>
                                    </div>
                                    <div className="nav-btn">
                                        { 
                                            !item.noNav && <button
                                                onClick={() => {
                                                    jumpTo(item.section);
                                                }}
                                            >
                                                {messages.jumpTo}
                                            </button>
                                        }
                                    </div>
                                </div>
                            </div>
                        ))}
                </div>
            </div>
        </div>
    );
};

export default RatingDetail;
