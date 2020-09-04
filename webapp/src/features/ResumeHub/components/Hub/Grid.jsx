import React from 'react';
import PropTypes from 'prop-types';

import styles from '../../styles/Hub.module.css';

const Grid = ({ children }) => (
    <div className={styles.grid}>
        {children}
    </div>
);

Grid.propTypes = {
    children: PropTypes.arrayOf(PropTypes.element)
};

export default Grid;