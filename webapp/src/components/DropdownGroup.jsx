import React, { useState } from 'react';
import { Form } from 'react-bootstrap';
import { Typeahead } from 'react-bootstrap-typeahead';

import 'react-bootstrap-typeahead/css/Typeahead.css';

// Searchable dropdown component
const DropdownGroup = ({ label, id, placeholder, searchKey, options }) => {
	const [singleSelections, setSingleSelections] = useState([]);
  return (
		<Form.Group className="form_item">
			<Form.Label htmlFor={id}>{label}</Form.Label>
			<Typeahead id={id} labelKey={searchKey} onChange={setSingleSelections} options={options} placeholder={placeholder} selected={singleSelections} />
		</Form.Group>
  );
};

export default DropdownGroup;
