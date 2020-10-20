import React from 'react';
import { Spinner } from 'react-bootstrap';
import './Loader.scss';
const Loader = () => {
  return (
    <div id="loading-wrapper">
      <Spinner animation="border" />
    </div>
  );
};

export default Loader;
