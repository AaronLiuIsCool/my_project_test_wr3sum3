import React from 'react';
import PropTypes from 'prop-types';
import { InView } from 'react-intersection-observer'
import { useDispatch } from 'react-redux';

import { actions } from '../../slicer';

const wrapChildrenWithInView = (children, makeChangeHandler) => (
    children.map((child, index) => {
        const handleChange = makeChangeHandler(index);
        return (
            <InView as="div" key={`inView-${index}`}
                threshold={0.50} onChange={handleChange}>
                {child}
            </InView>
        );
    })
);

const ViewObserver = ({ enabled, children }) => {
    const dispatch = useDispatch();
    const makeChangeHandler = (index) => {
        return (inView,) => {
            if (!inView) {
                return;
            }
            dispatch(actions.moveStep(index))
        };
    };

    if (!enabled) {
        return children;
    }

    return (
        <>
            {wrapChildrenWithInView(children, makeChangeHandler)}
        </>
    )
};

ViewObserver.propTypes = {
    enabled: PropTypes.bool,
    children: PropTypes.arrayOf(PropTypes.node).isRequired
}

export default ViewObserver;