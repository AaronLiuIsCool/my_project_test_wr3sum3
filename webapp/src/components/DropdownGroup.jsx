import React from 'react';
import PropTypes from 'prop-types';
import { Form } from 'react-bootstrap';
import { Typeahead } from 'react-bootstrap-typeahead';

import 'react-bootstrap-typeahead/css/Typeahead.css';

// TODO: Update consumer to pass in select and handleChange
const DropdownGroup = ({ label, id, placeholder, options, value, onChange, isValid, isInvalid, feedbackMessage }) => {
	const defaultSelected = options.filter((option) => option.data === value);

	return (
		<Form.Group className='form_item'>
			<Form.Label htmlFor={id}>{label}</Form.Label>
			<Form.Label htmlFor={id}>{label}</Form.Label>
			<Typeahead
				id={id}
				labelKey='data'
				options={options}
				placeholder={placeholder}
				defaultSelected={defaultSelected}
				onChange={onChange}
				isValid={isValid}
				isInvalid={isInvalid}
			/>

			{isInvalid ? <Form.Control.Feedback type='invalid'>{feedbackMessage}</Form.Control.Feedback> : null}
		</Form.Group>
	);
};

DropdownGroup.propTypes = {
	label: PropTypes.string,
	id: PropTypes.string,
	placeholder: PropTypes.string,
	searchKey: PropTypes.string,
	options: PropTypes.array,
	value: PropTypes.string,
	onChange: PropTypes.func,
	isValid: PropTypes.bool,
	isInvalid: PropTypes.bool,
};

export default DropdownGroup;
