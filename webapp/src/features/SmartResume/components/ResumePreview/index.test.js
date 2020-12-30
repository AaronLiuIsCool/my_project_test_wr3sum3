import React from 'react';
import { mount } from 'enzyme';
import { Provider } from 'react-redux';
import { I8nContext } from 'shell/i18n';
import renderer from 'react-test-renderer';

import 'react-dates/initialize';

import store from 'store';
import ResumePreview from './index';

const ResumePreviewWithProvider = (props) => (
  <Provider store={store}>
    <I8nContext.Provider value={{
      'RPreview': { 'editThemeColor': "编辑主题色" }
    }}>
      <ResumePreview {...props} />
    </I8nContext.Provider>
  </Provider>
);

describe('Resume Preview Container', () => {
  test('should render Preview and 5 Widgets', () => {
    const wrapper = mount(
      <ResumePreviewWithProvider data={{}} messages={{}} />
    );
    expect(wrapper.find('#displayPDF').length).toEqual(1);
    expect(wrapper.find('#widgetsContainer button').length).toEqual(4);
  });

});
