import React from 'react';
import { Provider } from 'react-redux';
import renderer from 'react-test-renderer';

import store from 'store';
import { I8nContext } from 'shell/i18n';

import ResumePreview from './index';
import { prepareData } from './resumeBuilder'

const RPmessages = {
  'RPreview': {
    "linkedinLink": "领英链接",
    "githubLink": "Github链接",
    "educationBackground": "教育背景",
    "workExperience": "工作经历",
    "projectExperience": "项目经历",
    "studentWorkAndVolunteer": "志愿服务",
    "certificateAndAward": "技能证书 & 获得荣誉",
    "awards": "获得荣誉",
    "certificate": "技能证书",
    "validForever": "永久有效",
    "expiredAt": "到期",
    "current": "现今",
    "editThemeColor": "编辑主题色",
    "smartTranslation": "智能翻译",
    "oneClickWholePage": "一键整页",
    "downloadResume": "下载简历",
    "perparingResume": "生成中..",
    "downLoadResume": "下载简历",
    "linkedIn": "领英链接",
    "weblink": "项目集链接"
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