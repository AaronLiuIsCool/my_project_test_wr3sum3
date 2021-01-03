import React, { useEffect, useState, useRef } from 'react';

import WrittenTipCard from './WrittenTipCard';

import ResumeServices from 'shell/services/ResumeServices';
import JobServices from 'shell/services/JobsServices'
import styles from '../../styles/Assistant.module.css';
import DropdownGroup from './Dropdown/DropdownGroup';
import LanguageToggle from './LanguageToggle';

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

const WrittenAssistant = ({ trigger, context }) => {
    const [industries, setIndustries] = useState([]);
    const [titles, setTitles] = useState({});
    const [suggestions, setSuggestions] = useState([]);

    const loaded = useRef(false);
    const getIndustries = async (en = '') => {
        const response = await resumeServices.getWrittenAssistant(
            trigger,
            `industries${en}`
        );
        setIndustries(response);
        return response
    };

    const getTitles = async (en = '') => {
        const response = await resumeServices.getWrittenAssistant(
            trigger,
            `titles${en}`
        );
        setTitles(response);
        return response
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
      if(industries && titles && industries.length > 0 && titles[industries[0]] && !loaded.current) {
        getSuggestions(industries[0], titles[industries[0]][0]);
        loaded.current = true;
      }
    }, [industries, titles]) // eslint-disable-line

    const updateLanguage = async (lang) => {
        const defaultIndustry = await getIndustries(lang);
        const defaultTitles = await getTitles(lang);
        getSuggestions(defaultIndustry[0], defaultTitles[defaultIndustry[0]][0]);
    }
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
                    if(keywords[i] === '') continue
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
            <div className="tips-container">
                {renderTips(suggestions, handleTipSelect)}
            </div>
            <LanguageToggle updateLanguage={updateLanguage} style={{position:"absolute", bottom: "20px", right: "20px"}}/>
        </div>
    );
};

export default WrittenAssistant;
