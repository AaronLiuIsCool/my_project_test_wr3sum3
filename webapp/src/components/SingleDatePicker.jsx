import React from 'react';
import PropTypes from 'prop-types';
import { Form } from 'react-bootstrap';
import { DatePicker, Space } from 'antd';
import moment from 'moment';
import { useI8n } from 'shell/i18n';


import './SingleDatePicker.scss';
import 'antd/dist/antd.css';


const SingleDatePickerWrapper = ({
    label, id, placeholder = "", value, onDateChange, displayFormat,
    readOnly = false, isInvalid, feedbackMessage, isValid
}) => {
    const messages = useI8n();
    const local = messages.datePicker;
    return (
        <Form.Group className={
            `form_item form-validation-wrapper ${isValid ? "form-validation-wrapper" : isInvalid ? "not-validated " : "form_item "}`}>
            <Form.Label htmlFor={id}>{label}</Form.Label>
            <Space direction="vertical">
                <DatePicker onChange={onDateChange} defaultValue={value ? moment(value, displayFormat) : ""} format={displayFormat} picker="month" locale={local} id={id} placeholder={placeholder} inputReadOnly={readOnly} />
            </Space>
            {isInvalid ? (<Form.Control.Feedback style={{ display: 'block' }} type="invalid">
                {feedbackMessage}
            </Form.Control.Feedback>) : null}
        </Form.Group >
    );
};

SingleDatePickerWrapper.propTypes = {
    label: PropTypes.string,
    id: PropTypes.string,
    placeholder: PropTypes.string,
    value: PropTypes.string,
    onDateChange: PropTypes.func,
    displayFormat: PropTypes.string,
    readOnly: PropTypes.bool,
    allowPastDatesOnly: PropTypes.bool,
    isInvalid: PropTypes.bool,
    feedbackMessage: PropTypes.string
}

export default SingleDatePickerWrapper;