import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Link } from 'react-router-dom';

import { Navbar, Nav, ToggleButtonGroup, ToggleButton } from 'react-bootstrap'

import { useI8n } from 'shell/i18n';
import { selectLanguage, updateLanguage } from '../slicer';

const Navigation = () => {
    const language = useSelector(selectLanguage);
    const messages = useI8n();
    const dispatch = useDispatch();

    return (
        <Navbar bg="light" expand="lg" fixed="top">
            <Navbar.Brand href="/">{messages.brand}</Navbar.Brand>
            <Navbar.Toggle aria-controls="top-navigation-bar" />
            <Navbar.Collapse id="top-navigation-bar">
                <Nav className="mr-auto">
                    <Nav.Link href="#">
                        <Link to="/">{messages["nav_item_smartresume"]}</Link>
                    </Nav.Link>
                    <Nav.Link href="#">
                        <Link to="/example">{messages["nav_item_example"]}</Link>
                    </Nav.Link>
                </Nav>

                <ToggleButtonGroup type="radio" name="languageSelect" defaultValue={language}
                    onChange={value => dispatch(updateLanguage(value))}>
                    <ToggleButton value={'zh'}>{messages["language_toggle_zh"]}</ToggleButton>
                    <ToggleButton value={'en'}>{messages["language_toggle_en"]}</ToggleButton>
                </ToggleButtonGroup>
            </Navbar.Collapse>
        </Navbar>
    );
}

export default Navigation;
