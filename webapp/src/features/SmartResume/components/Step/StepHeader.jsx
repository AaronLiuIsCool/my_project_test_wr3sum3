import React from 'react';
import PropTypes from 'prop-types';

const StepHeader = ({title, subtitle}) => (
    <div className="form_h1">
        <div>
            {/* todo: need an icon  */}
            <h1>{title}</h1>
        </div>
        <p>{subtitle}</p>
    </div>
);

StepHeader.propTypes = {
    title: PropTypes.string.isRequired,
    subtitle: PropTypes.string.isRequired
}

export default StepHeader;