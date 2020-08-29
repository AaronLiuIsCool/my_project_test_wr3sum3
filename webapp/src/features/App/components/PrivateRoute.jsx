import React from 'react';
import { Route } from 'react-router-dom';
import { useSelector } from 'react-redux';

import { selectAuthentication } from '../slicer';
import configs from 'shell/configs';

function PrivateRoute({ component: Component, ...rest }) {
    const authenticated = useSelector(selectAuthentication);
    if (!authenticated) {
        window.location = configs['login-url'];
        return <React.Fragment />;
    }
    return (
        <Route  {...rest} 
            render={props => (<Component {...props} />)}
        />
    );
}

export default PrivateRoute;