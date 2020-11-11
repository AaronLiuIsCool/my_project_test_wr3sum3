import React from 'react';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux';
import renderer from 'react-test-renderer';
import { mount } from 'enzyme';
import store from 'store';
import SmartResume from './SmartResume';

import 'react-dates/initialize';

describe('SmartResume tests', () => {
  // Aavatar uploader has dynamic id attribute, need to exclusive this 
  test.skip('renders Smart Resume', () => {
    const smartResumeApp = renderer.create(
      <Provider store={store}>
        <BrowserRouter>
          <SmartResume />
        </BrowserRouter>
      </Provider>
    ).toJSON();
    expect(smartResumeApp).toMatchSnapshot();
  });

  test('renders Smart Resume by checking each components', () => {
    const smartResumeApp = mount(
      <Provider store={store}>
        <BrowserRouter>
          <SmartResume />
        </BrowserRouter>
      </Provider>
    );
    expect(smartResumeApp.find('.widgetContainer').length).toEqual(1);
    expect(smartResumeApp.find('.progressBar.progress').length).toEqual(1);
    expect(smartResumeApp.find('#basicInfo').length).toEqual(2);
    expect(smartResumeApp.find('#education').length).toEqual(2);
    expect(smartResumeApp.find('#workXp').length).toEqual(2);
    expect(smartResumeApp.find('#otherXp').length).toEqual(2);
    expect(smartResumeApp.find('#certifications').length).toEqual(2);
  });

});
