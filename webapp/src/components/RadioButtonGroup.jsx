import React from 'react';
import PropTypes from 'prop-types';
import { Form } from 'react-bootstrap';

const RadioButtonGroup = ({ label, id, values, value = false, onClickHandler}) => (
	<Form.Group className="form_item">
		<Form.Label htmlFor={id}>{label}</Form.Label>
		<div className="radioButton_container">
			{values.map((item, index) => {
				const className = `radioButton` + (item['value'].toString() === value.toString() ? " selected" : "");
				return (
					<button key={index} value={item['value']} className={className} onClick={onClickHandler}>
						{item['label']}
					</button>
				);
			})}
		</div>
	</Form.Group>
);

RadioButtonGroup.propTypes = {
	label: PropTypes.string,
	id: PropTypes.string,
	values: PropTypes.array,
	value: PropTypes.bool,
	onChange: PropTypes.object,
};

export default RadioButtonGroup;
