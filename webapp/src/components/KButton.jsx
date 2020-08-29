import React from 'react';
import Button from 'react-bootstrap/Button'

const KButton = (props) => (
    <Button {...props}>
      {props.children}
    </Button>
);

export default KButton;