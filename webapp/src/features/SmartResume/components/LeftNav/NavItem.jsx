import React from 'react';
import PropTypes from 'prop-types';
import { useDispatch } from 'react-redux';

import { HashLink } from 'components/HashLink';

import { actions } from '../../slicer';

import styles from '../../styles/LeftNav.module.css';

const NavItem = ({ index, path, name, selected = false }) => {
    const dispatch = useDispatch();
    const handleClick = () => {
        dispatch(actions.moveStep(index));
    };

    return (
        <HashLink smooth to={`#${path}`} onClick={handleClick}
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