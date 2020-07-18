import React from 'react';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import renderer from 'react-test-renderer';

import resumeReducer from './slicers/resumeSlicer';

import SmartResume from './SmartResume';

const store = configureStore({
  reducer: {
    resume: resumeReducer
  },
});

test('renders SmartResume', () => {
  const smartResume = renderer.create(
    <Provider store={store}>
      <SmartResume />
    </Provider>
  ).toJSON();

  expect(smartResume).toMatchSnapshot();
});
