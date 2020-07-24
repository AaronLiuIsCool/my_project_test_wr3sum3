import React from 'react';
import { Provider } from 'react-redux';
import renderer from 'react-test-renderer';

import store from '../../../app/store';
import SmartResume from './SmartResume';

test('renders Smart Resume App', () => {
  const smartResumeApp = renderer.create(
    <Provider store={store}>
      <SmartResume />
    </Provider>
  ).toJSON();

  expect(smartResumeApp).toMatchSnapshot();
});