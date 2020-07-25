import React from 'react';
import PropTypes from 'prop-types';
import { Container } from 'react-bootstrap';

const Step = ({children}) => (
    <div className="form_body">
        <Container>
            {children}
        </Container>
    </div>
);

Step.propTypes = {
    children: PropTypes.node.isRequired
}

export default Step;