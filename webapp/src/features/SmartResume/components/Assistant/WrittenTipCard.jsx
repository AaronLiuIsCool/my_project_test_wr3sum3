import React, { useState } from 'react';

import { ReactComponent as GoToIcon } from '../../assets/goto.svg';

import styles from '../../styles/Assistant.module.css';

const WrittenTipCard = ({ tip, onSelect }) => {
    const [animate, setAnimate] = useState(false);

    const handleClick = () => {
        setAnimate(true);
        setTimeout(() => setAnimate(false), 500);
        onSelect(tip);
    }

    return ( 
        <div className={animate ? styles.tipCardAnimation : styles.tipCard} onClick={handleClick}>
            <div className={styles.tipIcon}><GoToIcon /></div>
            <div className={styles.tipContent}>{tip}</div>
        </div>
    );
}

export default WrittenTipCard;