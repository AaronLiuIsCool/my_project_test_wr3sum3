import React from 'react';
import PropTypes from 'prop-types';

import AddMoreForm from 'components/AddMoreForm';

import StepHeader from './StepHeader';
import StepContainer from './StepContainer';

const AddMore = ({ addMore }) => {
    if (!addMore) {
        return null;
    }
    return <AddMoreForm>添加新经历</AddMoreForm>
}

const Step = ({ id, title, subtitle, children, addMore = false }) => (
    <div id={id}>
        <StepHeader
            title={title}
            subtitle={subtitle}
        />
        <StepContainer>
            {children}
        </StepContainer>
        <AddMore addMore={addMore} />
    </div>
);

Step.propTypes = {
    id: PropTypes.string.isRequired,
    title: PropTypes.string.isRequired,
    subtitle: PropTypes.string.isRequired,
    children: PropTypes.node.isRequired,
    addMore: PropTypes.bool
}

export default Step;