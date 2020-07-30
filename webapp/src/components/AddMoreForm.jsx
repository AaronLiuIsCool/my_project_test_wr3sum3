import React from 'react';
import PropTypes from 'prop-types';

const AddMoreForm = ({ message, onAdd}) => {
  return (
    <div className="addMoreForm" onClick={onAdd}>
      {/* todo: need the icon  */}
      <span>+</span>
      <p>{message}</p>
    </div>
);
}

AddMoreForm.propTypes = {
    message: PropTypes.string,
    onAdd: PropTypes.func
}

export default AddMoreForm;