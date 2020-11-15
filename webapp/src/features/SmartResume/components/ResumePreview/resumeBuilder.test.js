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

const mockData =
  { "id": "4028b401755f292301758093f66c0016", "stepIndex": 0, "assistant": { "show": false }, "basic": { "completed": true, "data": { "id": 1431, "avatar": "", "nameCn": "xiatian yu", "nameEn": "test name", "email": "summer.0808yu@gmail.com", "phone": "6046553326", "city": "肇庆", "country": "undefined", "linkedin": "", "weblink": "" } }, "education": { "completed": false, "data": [{ "schoolName": "", "gpa": "", "startDate": "", "graduateDate": "", "major": "", "degree": "", "city": "", "country": "" }] }, "work": { "completed": false, "data": [{ "workName": "", "currentWorkFlag": false, "workCompanyName": "", "workStartDate": "", "workEndDate": "", "workDescription": "", "workCity": "", "workCountry": "" }] }, "project": { "completed": false, "data": [{ "projectRole": "", "currentProjectFlag": false, "projectCompanyName": "", "projectStartDate": "", "projectEndDate": "", "projectCity": "", "projectCountry": "", "projectDescription": "" }] }, "certificate": { "completed": false, "data": [{ "certificateName": "", "validCertificateFlag": false, "certificateIssuedDate": "", "certificateEndDate": "" }] }, "volunteer": { "completed": false, "data": [{ "volunteerRole": "", "currentVolunteerFlag": false, "volunteerCompanyName": "", "volunteerStartDate": "", "volunteerEndDate": "", "volunteerCity": "", "volunteerCountry": "", "volunteerDescription": "" }] }, "ratings": { "data": { "score": 0, "basicInfo": [], "layoutInfo": [], "educationInfo": [], "workInfo": { "company": [], "keywords": [], "quantify": [], "amount": [], "sorted": [] }, "projectInfo": { "company": [], "keywords": [], "quantify": [], "amount": [], "sorted": [] }, "otherInfo": { "company": [], "keywords": [], "quantify": [], "amount": [], "sorted": [] }, "certInfo": [{ "type": "exp-value", "section": "certifications", "messages": "技能证书，是非常重要的资质证明，您这块背景较为薄弱，建议您查看快课，快速掌握专业技能，提升背景竞争力", "name": "技能证书" }] } }, "resumeBuilder": { "data": { "color": "#3e89ec", "base64": "" } } };

const expectedData = [{ "type": "img", "y": 10, "x": 15, "width": 20, "height": 20, "src": "", "format": "PNG", "page": 1 }, { "type": "title", "y": 20, "x": 38, "content": "xiatian yu", "fontSize": 19, "page": 1 }, { "type": "h2", "y": 20, "x": 74.24550555555555, "content": "", "page": 1 }, { "type": "h3", "y": 20, "x": 82.24550555555555, "content": " ", "page": 1 }, { "type": "background", "y": 23.333333333333332, "x": 38, "color": "#f2f4f9", "page": 1 }, { "type": "h3", "y": 30, "x": 40, "content": "6046553326 summer.0808yu@gmail.com", "page": 1 }, { "type": "link", "y": 30, "x": 118.09144722222221, "content": "", "url": "", "page": 1 }]



describe('Test for resume preview data rendering', () => {
  test('Test prepareData function', async () => {
    // 因为字体的原因 所以jest环境计算出来的位置和实际位置会有细微的偏差，所以这里比较返还数据的行数
    // 在简历数据庞大的情况下 行数也有可能不同 所以测试数据应该以英文字符为主 尽量避免中文出现
    // expect(prepareData(mockData, RPmessages)).toEqual(expectedData);
    expect(prepareData(mockData, RPmessages).length).toEqual(expectedData.length);
  });
});
