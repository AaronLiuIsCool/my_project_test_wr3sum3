import React from 'react';
import { Provider } from 'react-redux';
import renderer from 'react-test-renderer';

import store from 'store';

import JobsMatcher from './JobsMatcher';


test('renders JobsMatcher', () => {
    const jobsMatcher = renderer.create(
        <Provider store={store}>
            <JobsMatcher />
        </Provider>
    ).toJSON();

    expect(jobsMatcher).toMatchSnapshot();
});
