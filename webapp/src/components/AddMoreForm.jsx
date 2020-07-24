import React from 'react';
import { Form } from 'react-bootstrap';


const AddMoreForm = (props) => {
  return (
    <div className="addMoreForm">
      {/* todo: need the icon  */}
      <span>+</span>
      <p>{props.children}</p>
    </div>
);
}

export default AddMoreForm;