import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import renderer from 'react-test-renderer';

import store from 'app/store';
import SmartResume from './SmartResume';

import 'react-dates/initialize';

describe('SmartResume tests', () => {
  test('renders Smart Resume', () => {
    const smartResumeApp = renderer.create(
      <Provider store={store}>
        <BrowserRouter>
          <SmartResume />
        </BrowserRouter>
      </Provider>
    ).toJSON();

    expect(smartResumeApp).toMatchSnapshot();
  });

});
