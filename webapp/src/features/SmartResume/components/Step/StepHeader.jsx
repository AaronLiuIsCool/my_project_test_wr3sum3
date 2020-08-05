import React from 'react';
import PropTypes from 'prop-types';

const StepHeader = ({title, subtitle, icon}) => (
    <div className="form_h1">
        <div>
            <img src={`${process.env.PUBLIC_URL}/images/${icon}`} alt={title + " Icon"} />
            <h1>{title}</h1>
        </div>
        <p>{subtitle}</p>
    </div>
);

StepHeader.propTypes = {
    title: PropTypes.string.isRequired,
    subtitle: PropTypes.string.isRequired,
    icon: PropTypes.string.isRequired
}

export default StepHeader;