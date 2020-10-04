import React from 'react';
import PropTypes from 'prop-types';
import { Form } from 'react-bootstrap';
import '../features/SmartResume/styles/validation.scss'
const InputGroup = ({ label, id, placeholder, type = 'text', value, onChange, isValid, isInvalid, feedbackMessage }) => (
	<Form.Group className="form_item form-validation-wrapper">
		<Form.Label htmlFor={id}>{label}</Form.Label>
		<Form.Control type={type} id={id} placeholder={placeholder} value={value} onChange={onChange} isValid={isValid} isInvalid={isInvalid} />
		{isInvalid && <Form.Control.Feedback type="invalid">{feedbackMessage}</Form.Control.Feedback>}
	</Form.Group>
);

InputGroup.propTypes = {
	label: PropTypes.string,
	id: PropTypes.string,
	placeholder: PropTypes.string.isRequired,
	type: PropTypes.string,
	value: PropTypes.string,
	isValid: PropTypes.bool,
	isInvalid: PropTypes.bool,
	feedbackMessage: PropTypes.string,
};

export default InputGroup;
