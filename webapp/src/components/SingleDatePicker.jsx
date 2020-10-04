import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { Form } from 'react-bootstrap';
import { SingleDatePicker, isInclusivelyBeforeDay } from 'react-dates';
import moment from 'moment';

import './SingleDatePicker.scss'

const SingleDatePickerWrapper = ({
    label, id, placeholder, value, onDateChange, monthFormat, displayFormat,
    readOnly = false, allowPastDatesOnly = false, isInvalid, feedbackMessage, isValid
}) => {
    const [focused, setFocused] = useState(false);
    return (
        <Form.Group className={isValid ? "form_item validated" : "form_item"}>
            <Form.Label htmlFor={id}>{label}</Form.Label>
            <SingleDatePicker
                isOutsideRange={day => (allowPastDatesOnly ? !isInclusivelyBeforeDay(day, moment()) : undefined)}
                date={value ? moment(value) : null} onDateChange={onDateChange}
                focused={focused} onFocusChange={({ focused }) => setFocused(focused)}
                id={id} placeholder={placeholder} noBorder={true} readOnly={readOnly}
                monthFormat={monthFormat} displayFormat={displayFormat}
            />
            {isInvalid ? (<Form.Control.Feedback style={{display: 'block'}} type="invalid">
                {feedbackMessage}
            </Form.Control.Feedback>) : null}
        </Form.Group >
    );
};

SingleDatePickerWrapper.propTypes = {
    label: PropTypes.string,
    id: PropTypes.string,
    placeholder: PropTypes.string.isRequired,
    value: PropTypes.string,
    onDateChange: PropTypes.func,
    monthFormat: PropTypes.string,
    displayFormat: PropTypes.string,
    readOnly: PropTypes.bool,
    allowPastDatesOnly: PropTypes.bool,
    isInvalid: PropTypes.bool,
    feedbackMessage: PropTypes.string
}

export default SingleDatePickerWrapper;