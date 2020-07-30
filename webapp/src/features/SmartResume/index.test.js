import React from 'react';
import { Provider } from 'react-redux';
import renderer from 'react-test-renderer';

import store from 'app/store';

import SmartResume from './index';


test('renders lazy loaded component for SmartResume', () => {
  const smartResume = renderer.create(
    <Provider store={store}>
      <SmartResume />
    </Provider>
  ).toJSON();

  expect(smartResume).toMatchSnapshot();
});
