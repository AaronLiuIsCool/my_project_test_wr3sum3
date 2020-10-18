import React, { useEffect, useState, useRef } from 'react';

import WrittenTipCard from './WrittenTipCard';

import ResumeServices from 'shell/services/ResumeServices';
import JobServices from 'shell/services/JobsServices'
import styles from '../../styles/Assistant.module.css';
import DropdownGroup from './Dropdown/DropdownGroup';

const resumeServices = new ResumeServices();
const jobServices = new JobServices();

function renderTips(tips, handleSelect) {
    return tips.map((tip, index) => (
        <WrittenTipCard
            key={`tip-${index}`}
            tip={tip}
            onSelect={handleSelect}
        />
    ));
}

// TODO: Standardize this component to be used for more than just Work written assistant
const WrittenAssistant = ({ trigger, context }) => {
    const [industries, setIndustries] = useState([]);
    const [titles, setTitles] = useState({});
    const [suggestions, setSuggestions] = useState([]);

    const loaded = useRef(false);
    const getIndustries = async () => {
        const response = await resumeServices.getWrittenAssistant(
            trigger,
            'industries'
        );
        setIndustries(response);
    };

    const getTitles = async () => {
        const response = await resumeServices.getWrittenAssistant(
            trigger,
            'titles'
        );
        setTitles(response);
    };

    const getSuggestions = async (industry, title) => {
        const response = await jobServices.getSuggestions(industry, title);
        const { suggestions } = response;
        setSuggestions(parseSuggestions(suggestions));
    };

    useEffect(() => {
        getIndustries();
        getTitles();
    }, []); // eslint-disable-line react-hooks/exhaustive-deps
    useEffect(() => {
      if(industries && titles && industries.length > 0 && titles[industries[0]]) {
        if(!loaded.current) {
          getSuggestions(industries[0], titles[industries[0]][0]);
          loaded.current = true;
        }
      }
    }, [industries, titles]) // eslint-disable-line


    const handleTipSelect = (tip) => {
        switch (trigger) {
            case 'work':
                notifyEditor('work', context.index, {
                    data: {
                        offsets: tip.offsets,
                        text: tip.text
                    },
                    type: 'highlight-keyword'
                });
                break;
            case 'project':
                notifyEditor('project', context.index, {
                    data: {
                        offsets: tip.offsets,
                        text: tip.text
                    },
                    type: 'highlight-keyword'
                });
                break;
            case 'volunteer':
                notifyEditor('volunteer', context.index, {
                    data: {
                        offsets: tip.offsets,
                        text: tip.text
                    },
                    type: 'highlight-keyword'
                });
                break;
            default:
                return;
        }
    };

    const notifyEditor = (type, index, detail) => {
        const eventName = `${type}-${index}`;
        const event = new CustomEvent(eventName, {
            detail
        });
        window.dispatchEvent(event);
    };

    const parseSuggestions = (suggestions = []) => {
        return suggestions.map((suggestion) => {
            let keywords = suggestion.suggestionKeywords.split(',');
            let html = suggestion.texts;
            let offsets = [];
            if (keywords.length > 0) {
                for (let i = 0; i < keywords.length; i++) {
                    const regex = new RegExp(keywords[i], 'g');
                    html = html.replace(regex, function (match) {
                        return `<span class="hightlight-blue">${match}</span>`;
                    });
                    offsets = offsets.concat(
                        calculateOffset(suggestion.texts, keywords[i])
                    );
                }
                html = html.replace(
                    /(<\/span>)\s?(<span class="hightlight-blue">)/g,
                    ''
                );
            }
            return {
                text: suggestion.texts,
                html,
                offsets
            };
        });
    };
    const calculateOffset = (suggestion, keyword) => {
        let start = 0;
        const increment = keyword.length;
        const res = [];

        while (suggestion.indexOf(keyword, start) !== -1) {
            start = suggestion.indexOf(keyword, start);
            res.push({
                offset: start,
                length: increment,
                style: 'green'
            });
            start += increment;
        }
        return res;
    };
    return (
        <div className={styles['inner-container']}>
            <DropdownGroup industries={industries} titles={titles} onSelect={getSuggestions} />
            {/* <div className="writtenAssistantWidgetHeader">
                <InputGroup className="mb-3">
                    <InputGroup.Prepend>
                        <Form.Control
                            defaultValue="default"
                            as="select"
                            custom
                            onChange={(e) => {
                                setIndustry(e.currentTarget.value);
                            }}
                        >
                            <option disabled value="default">
                                选择行业
                            </option>
                            {renderIndustries(industries)}
                        </Form.Control>
                    </InputGroup.Prepend>
                    <Typeahead
                        id="jobTitle"
                        labelKey="jobTitle"
                        options={
                            industry === ''
                                ? []
                                : titles
                                ? titles[industry]
                                : []
                        }
                        onInputChange={(e) => {
                            setTitle(e);
                        }}
                        onChange={(e) => {
                            e.length > 0 ? setTitle(e[0]) : setTitle('');
                        }}
                        placeholder={'请输入岗位名称'}
                        defaultValue={context && context.workName}
                    />
                    <InputGroup.Append>
                        <InputGroup.Text>
                            <Button variant="light" onClick={handleSearch}>
                                <SearchIcon />
                            </Button>
                        </InputGroup.Text>
                    </InputGroup.Append>
                </InputGroup>
            </div> */}
            <div className="tips-container">
                {renderTips(suggestions, handleTipSelect)}
            </div>
        </div>
    );
};

export default WrittenAssistant;
