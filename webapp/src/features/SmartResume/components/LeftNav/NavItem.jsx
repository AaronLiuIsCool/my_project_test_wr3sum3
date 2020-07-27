import React from 'react';
import PropTypes from 'prop-types';
import { HashLink } from 'react-router-hash-link';
import { useDispatch } from 'react-redux';

import { moveStep } from '../../slicer';

import styles from '../../styles/LeftNav.module.css';

const NavItem = ({ index, path, name, selected = false }) => {
    const dispatch = useDispatch();
    return (
        <HashLink smooth to={`/#${path}`} onClick={() => dispatch(moveStep(index))}
            className={selected ? styles.navItemSelected : styles.navItem}>
            {name}
        </HashLink>
    );
}

NavItem.propTypes = {
    index: PropTypes.number.isRequired,
    path: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    selected: PropTypes.bool
};

export default NavItem;