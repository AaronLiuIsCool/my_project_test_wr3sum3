import React from 'react';
import PropTypes from 'prop-types';
import { Form } from 'react-bootstrap';

const TextArea = ({ label, id, placeholder, value, onChange, row = '3' }) => (
	<Form.Group className="form_item">
		<Form.Label htmlFor={id}>{label}</Form.Label>
		<Form.Control as="textarea" rows={row} placeholder={placeholder} onChange={onChange} value={value} />
	</Form.Group>
);

TextArea.propTypes = {
	label: PropTypes.string,
	id: PropTypes.string,
	placeholder: PropTypes.string.isRequired,
	type: PropTypes.string,
	value: PropTypes.string,
	isValid: PropTypes.bool,
	isInvalid: PropTypes.bool,
	feedbackMessage: PropTypes.string,
};

export default TextArea;
