import React from 'react';
import PropTypes from 'prop-types';

import AddMoreForm from 'components/AddMoreForm';

import StepHeader from './StepHeader';
import StepContainer from './StepContainer';

const getAddMoreForm = (enabled, message, handleAddMore) => {
    if (!enabled) {
        return null;
    }
    return (
        <AddMoreForm onAdd={handleAddMore} message={message} />
    )
}

const Step = ({ id, title, subtitle, children, addMore, icon = false, addMoreMessage, handleAddMore }) => (
    <div id={id}>
        <StepHeader
            title={title}
            subtitle={subtitle}
            icon={icon}
        />
        <StepContainer>
            {children}
        </StepContainer>
        {getAddMoreForm(addMore, addMoreMessage, handleAddMore)}
    </div>
);

Step.propTypes = {
    id: PropTypes.string.isRequired,
    title: PropTypes.string.isRequired,
    subtitle: PropTypes.string.isRequired,
    children: PropTypes.node.isRequired,
    addMore: PropTypes.bool,
    addMoreMessage: PropTypes.string,
    handleAddMore: PropTypes.func
}

export default Step;