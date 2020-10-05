import React, { useEffect } from 'react';
import { useSelector } from 'react-redux';

import WrittenAssistant from './WrittenAssistant';

import { assistantSelectors } from '../../slicer';

import styles from '../../styles/Assistant.module.css';
import '../../styles/assistant.scss';

import RatingDetail from '../RatingDetail/RatingDetail.jsx'

let FIRST_RENDER = true;

const renderAssistant = (trigger, context) => {
    // TODO: Add if back when we add more variation to assist
    if (trigger === 'work') {
        return <WrittenAssistant trigger={trigger} context={context} />;
    } else if (trigger === 'project') {
        return <WrittenAssistant trigger={trigger} context={context} />;
    } else if (trigger === 'volunteer') {
        return <WrittenAssistant trigger={trigger} context={context} />;
    } else if (trigger === 'rating') {
        return <RatingDetail />
    }
}

const Assistant = () => {
    const { selectShow, selectTrigger, selectAssistantContext } = assistantSelectors;
    const show = useSelector(selectShow);
    const trigger = useSelector(selectTrigger);
    const context = useSelector(selectAssistantContext);

    useEffect(() => { // This is a hack to make sure the animation doesn't show on initial render
        if (show === true) {
            FIRST_RENDER = false;
        }
    }, [show]);

    if (!show && FIRST_RENDER) {
        return <React.Fragment />;
    }

    return  (
        <div className={show ? styles.container : styles.containerHide}>
            {renderAssistant(trigger, context)}
        </div>
    )
}

export default Assistant;