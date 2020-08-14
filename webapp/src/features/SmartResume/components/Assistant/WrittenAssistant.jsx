import React, { useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';

import { Button, Form, InputGroup } from 'react-bootstrap';
import { Typeahead } from 'react-bootstrap-typeahead';
import WrittenTipCard from './WrittenTipCard';
import { ReactComponent as SearchIcon } from '../../assets/search.svg'

import { actions } from '../../slicer';
import ResumeServices from 'shell/services/ResumeServices';

import styles from '../../styles/Assistant.module.css';

const resumeServices = new ResumeServices();

function renderIndustries(industries) {
    return industries.map((industry, index) => 
        <option key={`${industry}-${index}`}>{industry}</option>);
}

function renderTips(tips, handleSelect) {
    return tips.map((tip, index) => <WrittenTipCard key={`tip-${index}`} tip={tip} onSelect={handleSelect}/>);
}

// TODO: Standardize this component to be used for more than just Work written assistant
const WrittenAssistant = ({ trigger, context }) => {
    const [industries, setIndustries] = useState([]);
    const [titles, setTitles] = useState([]);
    const [suggestions, setSuggestions] = useState([]);
    const dispatch = useDispatch();

    const getIndustries = async () => {
        const response = await resumeServices.getWrittenAssistant(trigger, 'industries');
        setIndustries(response);
    };

    const getTitles = async () => {
        const response = await resumeServices.getWrittenAssistant(trigger, 'titles');
        setTitles(response);
    };

    const getSuggestions = async () => {
        const response = await resumeServices.getWrittenAssistant(trigger, 'suggestions');
        setSuggestions(response);
    };

    useEffect(() => {
        getIndustries();
        getTitles();
        getSuggestions();
    }, []); // eslint-disable-line react-hooks/exhaustive-deps

    const handleSearch = () => {
        getSuggestions();
    }

    const handleTipSelect = (tip) => {
        switch (trigger) {
            case 'work':
                dispatch(actions.appendWorkDescription({index: context.index, value: tip}));
                break;
            case 'project':
                dispatch(actions.appendProjectDescription({index: context.index, value: tip}));
                break;
            case 'volunteer':
                dispatch(actions.appendVolunteerDescription({index: context.index, value: tip}));
                break;
            default:
                return;
        }
    }

    return (
        <div className={styles['inner-container']}>
            <div className='writtenAssistantWidgetHeader'>
                <InputGroup className="mb-3">
                    <InputGroup.Prepend>
                        <Form.Control as="select" custom>
                            {renderIndustries(industries)}
                        </Form.Control>
                    </InputGroup.Prepend>
                    <Typeahead id='jobTitle' labelKey='jobTitle' options={titles}
                        placeholder={'请输入岗位名称'} defaultValue={context && context.workName} />
                    <InputGroup.Append>
                        <InputGroup.Text>
                            <Button variant="light" onClick={handleSearch}>
                                <SearchIcon />
                            </Button>
                        </InputGroup.Text>
                    </InputGroup.Append>
                </InputGroup>
            </div>
            <div>
                {renderTips(suggestions, handleTipSelect)}
            </div>
        </div>
    );
}

export default WrittenAssistant;