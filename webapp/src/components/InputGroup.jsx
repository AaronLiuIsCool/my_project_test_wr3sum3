import React from 'react';
import { Form } from 'react-bootstrap';

const InputGroup = (props) => {
  const {label, id, placeholder, type="text"} = props;
  return (
    <Form.Group className="form_item">
      <Form.Label htmlFor={id}>{label}</Form.Label>
      <Form.Control type={type} id={id} placeholder={placeholder} />
    </Form.Group>
);
}

export default InputGroup;