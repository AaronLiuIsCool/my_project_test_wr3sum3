import React from 'react';
import { Provider } from 'react-redux';
import renderer from 'react-test-renderer';

import store from 'store';
import App from '.';

describe("App tests", () => {
  test('renders App', () => {
    const app = renderer.create(
      <Provider store={store}>
        <App waitForInit={false} />
      </Provider>
    ).toJSON();
  
    expect(app).toMatchSnapshot();
  });
  
});