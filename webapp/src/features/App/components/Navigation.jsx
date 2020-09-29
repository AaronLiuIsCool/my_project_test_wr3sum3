import React, { useEffect } from 'react';
import { withRouter } from "react-router";
import { useSelector, useDispatch } from 'react-redux';

import { Navbar, Nav } from 'react-bootstrap';
import Actions from './Actions';
import { ReactComponent as LogoZH } from '../assets/logo_zh.svg';
import { ReactComponent as LogoEN } from '../assets/logo_zh.svg';

import { useI8n } from 'shell/i18n';
import { selectLanguage, updatePath } from '../slicer';

const Navigation = ({ location }) => {
    const language = useSelector(selectLanguage);
    const messages = useI8n();
    const dispatch = useDispatch();
    
    useEffect(() => {
        dispatch(updatePath(location));
    }, [dispatch, location]);

    return (
        <Navbar bg="white" expand="lg" fixed="top">
            <Navbar.Brand href="/">
                {language === 'zh' ? <LogoZH /> : <LogoEN />}
            </Navbar.Brand>
            <Navbar.Toggle aria-controls="top-navigation-bar" />
            <Navbar.Collapse id="top-navigation-bar">
                <Nav className="mr-auto" activeKey={location.pathname}>
                    <Nav.Link href='/'>{messages["nav_item_resumehub"]}</Nav.Link>
                    <Nav.Link href='/jobs'>{messages["nav_item_jobmatcher"]}</Nav.Link>
                    <Nav.Link href='/job-collection'>{messages["nav_item_jobcollction"]}</Nav.Link>
                </Nav>

                <Actions />
            </Navbar.Collapse>
        </Navbar>
    );
}

export default withRouter(Navigation);
