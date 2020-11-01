import React from 'react';
import { Provider } from 'react-redux';
import renderer from 'react-test-renderer';

import store from 'store';

import ResumeHub from './ResumeHub';

describe('ResumeHub', () => {
    test('renders ResumeHub', () => {
        const resumeHub = renderer.create(
            <Provider store={store}>
                <ResumeHub />
            </Provider>
        ).toJSON();
    
        expect(resumeHub).toMatchSnapshot();
    });    
});


