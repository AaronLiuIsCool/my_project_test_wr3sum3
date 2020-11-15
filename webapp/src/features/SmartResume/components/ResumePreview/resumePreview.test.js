import React from 'react';
import { Provider } from 'react-redux';
import renderer from 'react-test-renderer';

import store from 'store';
import { I8nContext } from 'shell/i18n';

import ResumePreview from './index';
import { prepareData } from './resumeBuilder'

const RPmessages = {
  'RPreview': {
    'editThemeColor': "编辑主题色", "workExperience": "工作经历", "studentWorkAndVolunteer": "学生工作和志愿服务",
    "current": "现今",
  }
};

const ResumeBuilderProvider = (props) => (
  <Provider store={store}>
    <I8nContext.Provider value={RPmessages}>
      <ResumePreview {...props} />
    </I8nContext.Provider>
  </Provider>
);

describe('Resume Preview Snapshot test', () => {
  test('Snapshot with no data match', async () => {
    const searchHeader = renderer.create(
      <ResumeBuilderProvider data={{}} messages={{}} />
    );
    expect(searchHeader.toJSON()).toMatchSnapshot();
  });
});