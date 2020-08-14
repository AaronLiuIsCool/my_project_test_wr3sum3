import React from 'react';
import Button from 'react-bootstrap/Button'

const Button = (props) => (
    <Button {...props}>
      {props.children}
    </Button>
);

export default Button;