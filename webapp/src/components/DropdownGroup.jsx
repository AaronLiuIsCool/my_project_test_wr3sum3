import React from 'react';
import PropTypes from 'prop-types';
import { Form } from 'react-bootstrap';
import { Typeahead } from 'react-bootstrap-typeahead';

import 'react-bootstrap-typeahead/css/Typeahead.css';

// TODO: Update consumer to pass in select and handleChange
const DropdownGroup = ({ label, id, placeholder, searchKey, options, select, handleChange }) => (
	<Form.Group className="form_item">
		<Form.Label htmlFor={id}>{label}</Form.Label>
		<Typeahead id={id} labelKey={searchKey} options={options}
			placeholder={placeholder} selected={select}
			onChange={handleChange} />
	</Form.Group>
);


DropdownGroup.propTypes = {
	label: PropTypes.string,
	id: PropTypes.string,
	placeholder: PropTypes.string,
	searchKey: PropTypes.string,
	options: PropTypes.array,
	select: PropTypes.string,
	handleChange: PropTypes.func
};

export default DropdownGroup;
