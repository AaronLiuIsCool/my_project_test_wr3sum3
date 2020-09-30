import React, { useState, useEffect, useRef } from 'react';
import './Dropdown.scss';

const downArrow = require('../../assets/downarrow.png');
const upArrow = require('../../assets/uparrow.png');

const Dropdown = ({ options, onSelect, data, messages }) => {
    const [selectedOption, setSelectedOption] = useState(options[0].value || '');

    const [showSelection, setShowSelection] = useState(false);
    const dropdownRef = useRef(null);

    useEffect(() => {
        document.addEventListener('click', ({ target }) => {
            if (dropdownRef.current && !dropdownRef.current.contains(target)) {
                setShowSelection(false);
            }
        });
    }, [showSelection]);
    
    useEffect(() => {
        setSelectedOption(messages.all)
    }, [data, messages.all])
    
    return (
        <div className="dropdown-wrapper" ref={dropdownRef}>
            <div
                className="selected-option"
                onClick={() => {
                    setShowSelection(!showSelection);
                }}
            >
                <span className="title">{selectedOption}</span>
                <span>
                    <img
                        width="30"
                        alt="down_arrow"
                        src={showSelection ? upArrow : downArrow}
                    />
                </span>
            </div>
            {showSelection && (
                <div className="dropdown-items">
                    {
                        options.map((option, index) => {
                        return <div key={index} className="dropdown-item" onClick={() => {
                            setShowSelection(false)
                            setSelectedOption(option.value)
                            onSelect(option.key)
                        }}>{option.value}</div>;
                    })
                }
                </div>
            )}
        </div>
    );
};

export default Dropdown;
