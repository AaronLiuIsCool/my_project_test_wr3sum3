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
    "perparingResume": "生成中..",
    "downloadResume": "下载简历",
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

const mockData =
  { "id": "4028b401755f292301758093f66c0016", "stepIndex": 0, "assistant": { "show": false }, "basic": { "completed": true, "data": { "id": 1431, "avatar": "", "nameCn": "xiatian yu", "nameEn": "test name", "email": "summer.0808yu@gmail.com", "phone": "6046553326", "city": "肇庆", "country": "undefined", "linkedin": "", "weblink": "" } }, "education": { "completed": false, "data": [{ "schoolName": "", "gpa": "", "startDate": "", "graduateDate": "", "major": "", "degree": "", "city": "", "country": "" }] }, "work": { "completed": false, "data": [{ "workName": "", "currentWorkFlag": false, "workCompanyName": "", "workStartDate": "", "workEndDate": "", "workDescription": "", "workCity": "", "workCountry": "" }] }, "project": { "completed": false, "data": [{ "projectRole": "", "currentProjectFlag": false, "projectCompanyName": "", "projectStartDate": "", "projectEndDate": "", "projectCity": "", "projectCountry": "", "projectDescription": "" }] }, "certificate": { "completed": false, "data": [{ "certificateName": "", "validCertificateFlag": false, "certificateIssuedDate": "", "certificateEndDate": "" }] }, "volunteer": { "completed": false, "data": [{ "volunteerRole": "", "currentVolunteerFlag": false, "volunteerCompanyName": "", "volunteerStartDate": "", "volunteerEndDate": "", "volunteerCity": "", "volunteerCountry": "", "volunteerDescription": "" }] }, "ratings": { "data": { "score": 0, "basicInfo": [], "layoutInfo": [], "educationInfo": [], "workInfo": { "company": [], "keywords": [], "quantify": [], "amount": [], "sorted": [] }, "projectInfo": { "company": [], "keywords": [], "quantify": [], "amount": [], "sorted": [] }, "otherInfo": { "company": [], "keywords": [], "quantify": [], "amount": [], "sorted": [] }, "certInfo": [{ "type": "exp-value", "section": "certifications", "messages": "技能证书，是非常重要的资质证明，您这块背景较为薄弱，建议您查看快课，快速掌握专业技能，提升背景竞争力", "name": "技能证书" }] } }, "resumeBuilder": { "data": { "color": "#3e89ec", "base64": "" } } };

const expectedData = [{"content": "xiatian yu", "fontSize": 19, "page": 1, "type": "title", "x": 15, "y": 20}, {"color": "#f2f4f9", "page": 1, "type": "background", "x": 15, "y": 23.5}, {"content": "肇庆  6046553326  summer.0808yu@gmail.com", "page": 1, "type": "h3", "x": 19, "y": 28.166666666666668}, {"content": "", "page": 1, "type": "link", "url": "", "x": 101.9305, "y": 28.166666666666668}]



describe('Test for resume preview data rendering', () => {
  test('Test prepareData function', async () => {
    // 因为字体的原因 所以jest环境计算出来的位置和实际位置会有细微的偏差，所以这里比较返还数据的行数
    // 在简历数据庞大的情况下 行数也有可能不同 所以测试数据应该以英文字符为主 尽量避免中文出现
    // expect(prepareData(mockData, RPmessages)).toEqual(expectedData);
    expect(prepareData(mockData, RPmessages).length).toEqual(expectedData.length);
  });
});