import React, { useEffect, useState } from 'react';
import { useHistory, withRouter, Link, useLocation } from 'react-router-dom';
import { useSelector } from 'react-redux';

import { Navbar, Nav } from 'react-bootstrap';
import Actions from './Actions';
import { ReactComponent as LogoZH } from '../assets/logo_zh.svg';
import { ReactComponent as LogoEN } from '../assets/logo_zh.svg';

import { useI8n } from 'shell/i18n';
import { selectLanguage } from '../slicer';

import styles from '../styles/Navigation.module.css';

const ROUTES = [{
    path: '/',
    messageKey: 'nav_item_resumehub'
}, {
    path: '/collections',
    messageKey: 'nav_item_jobcollction'
}];

const Navigation = ({ location }) => {
    const [showNav, setShowNav] = useState(true);
    const history = useHistory();
    const language = useSelector(selectLanguage);
    const messages = useI8n();
    const {pathname} = useLocation()

    useEffect(() => {
        const shouldShowNav = ROUTES.some(({path}) => path === location.pathname);
        if (showNav !== shouldShowNav) {
            setShowNav(shouldShowNav);
        }
    }, [showNav, location]);

    if (!showNav) {
        const regex = /(resume)\/\w+/
        if(pathname.match(regex)) {
           return <div className={styles.closeButton} 
                onClick={() => history.push('/')} />
        }
        return (
            <div className={styles.closeButton} 
                onClick={() => history.goBack()} />
        );
    }

    const renderRoutes = () => (
        ROUTES.map(({path, messageKey}) => (
            <Nav.Item as="li" key={messageKey}>
                <Nav.Link as={Link} to={path} eventKey={path}>
                    {messages[messageKey]}
                </Nav.Link>
            </Nav.Item>
        ))
    );

    return (
        <Navbar bg="white" expand="lg" fixed="top">
            <Navbar.Brand as={Link} to="/">
                {language === 'zh' ? <LogoZH /> : <LogoEN />}
            </Navbar.Brand>
            <Navbar.Toggle aria-controls="top-navigation-bar" />
            <Navbar.Collapse id="top-navigation-bar">
                <Nav className="mr-auto" activeKey={location.pathname} defaultActiveKey='/' as='ul'>
                    {renderRoutes()}
                </Nav>

                <Actions />
            </Navbar.Collapse>
        </Navbar>
    );
}

export default withRouter(Navigation);
