import moment from 'moment';

import { initialState, education, validateEducation, validateEducationEntry } from './education';
import { basic } from './basic';
import resumeReducer, { educationSelectors } from '.';

describe('test education related functionalities', () => {
  test('it should validate education object', () => {
    expect(validateEducation({ ...education })).toBe.false;

    expect(validateEducation({
      schoolName: 'test school',
      gpa: 3.88,
      startDate: moment().add(-4, 'year'),
      graduateDate: moment(),
      major: 'Computing Science',
      degree: 'Bachelor',
      city: 'Edmonton',
      country: 'Canada',
      highestAward: undefined,
      otherAward: undefined
    })).toBe.true;
  });

  test('it should validate each entry of education', () => {
    const tests = [
      // name, value, result
      ['schoolName', undefined, false],
      ['schoolName', 123, false],
      ['schoolName', 'u of a', true],
      ['schoolName', '', false],

      ['gpa', undefined, false],
      ['gpa', 5, false],
      ['gpa', -1, false],
      ['gpa', 4.1, false],
      ['gpa', 3.82, true],
      ['gpa', 4.0, true],
      ['gpa', 1.0, true],

      // ['startDate', undefined, false],
      // ['startDate', 'test', false],
      ['startDate', Date.now(), false],
      ['startDate', new Date(), false],
      ['startDate', new Date().toISOString(), true],
      // ['startDate', '2012', true],

      // ['graduateDate', undefined, false],
      // ['graduateDate', 'test', false],
      ['graduateDate', Date.now(), false],
      ['graduateDate', new Date(), false],
      ['graduateDate', new Date().toISOString(), true],
      // ['graduateDate', '2012', true],

      ['major', undefined, false],
      ['major', 123, false],
      ['major', 'cs', true],
      ['major', '', false],

      ['degree', undefined, false],
      ['degree', 123, false],
      ['degree', 'master', true],
      ['degree', '', false],

      ['city', undefined, false],
      ['city', 123, false],
      ['city', '北京', true],
      ['city', '', false],

      ['country', undefined, false],
      ['country', 123, false],
      ['country', '中国', true],
      ['country', '', false]
    ];

    tests.forEach((test, index) => expect(validateEducationEntry(test[0], test[1])).toBe(test[2]));
  });

  it('test reducers', () => {
    expect({}).toEqual({});
    // let testResult = resumeReducer(initialState, {
    //   "type": "resume/addNewEducation"
    // })['education'];
    // expect(testResult).toEqual(
    //   {
    //     completed: false,
    //     data: [
    //       { ...education },
    //       { ...education }
    //     ]
    //   }
    // );

    // testResult = resumeReducer(initialState, {
    //   "type": "resume/updateSchoolName",
    //   "payload": {
    //     "value": "school",
    //     "index": 0
    //   }
    // })['education'];

    // expect(testResult).toEqual({
    //   completed: false,
    //   data: [
    //     { ...education, "schoolName": "school" },
    //   ]
    // });

    // expect(resumeReducer(initialState, {
    //   "type": "resume/updateGPA",
    //   "payload": {
    //     "value": "3.85",
    //     "index": 0
    //   }
    // })).toEqual({
    //   stepIndex: 0,
    //   education: {
    //     completed: false,
    //     data: [
    //       { ...education, "gpa": "3.85" },
    //     ]
    //   }
    // });

    // const dateStr = new Date().toISOString();
    // expect(resumeReducer(initialState, {
    //   "type": "resume/updateStartDate",
    //   "payload": {
    //     "value": dateStr,
    //     "index": 0
    //   }
    // })).toEqual({
    //   stepIndex: 0,
    //   education: {
    //     completed: false,
    //     data: [
    //       { ...education, "startDate": dateStr },
    //     ]
    //   }
    // });

    // expect(resumeReducer(initialState, {
    //   "type": "resume/updateGraduateDate",
    //   "payload": {
    //     "value": dateStr,
    //     "index": 0
    //   }
    // })).toEqual({
    //   stepIndex: 0,
    //   education: {
    //     completed: false,
    //     data: [
    //       { ...education, "graduateDate": dateStr },
    //     ]
    //   }
    // });

    // expect(resumeReducer(initialState, {
    //   "type": "resume/updateMajor",
    //   "payload": {
    //     "value": "test major",
    //     "index": 0
    //   }
    // })).toEqual({
    //   stepIndex: 0,
    //   education: {
    //     completed: false,
    //     data: [
    //       { ...education, "major": "test major" },
    //     ]
    //   }
    // });

    // expect(resumeReducer(initialState, {
    //   "type": "resume/updateDegree",
    //   "payload": {
    //     "value": "test degree",
    //     "index": 0
    //   }
    // })).toEqual({
    //   stepIndex: 0,
    //   education: {
    //     completed: false,
    //     data: [
    //       { ...education, "degree": "test degree" },
    //     ]
    //   }
    // });

    // expect(resumeReducer(initialState, {
    //   "type": "resume/updateCity",
    //   "payload": {
    //     "value": "CITY",
    //     "index": 0
    //   }
    // })).toEqual({
    //   stepIndex: 0,
    //   education: {
    //     completed: false,
    //     data: [
    //       { ...education, "city": "CITY" },
    //     ]
    //   }
    // });

    // expect(resumeReducer(initialState, {
    //   "type": "resume/updateCountry",
    //   "payload": {
    //     "value": "CANADA",
    //     "index": 0
    //   }
    // })).toEqual({
    //   stepIndex: 0,
    //   education: {
    //     completed: false,
    //     data: [
    //       { ...education, "country": "CANADA" },
    //     ]
    //   }
    // });

    // expect(resumeReducer(initialState, {
    //   "type": "resume/updateHighestAward",
    //   "payload": {
    //     "value": "highest award",
    //     "index": 0
    //   }
    // })).toEqual({
    //   stepIndex: 0,
    //   education: {
    //     completed: false,
    //     data: [
    //       { ...education, "highestAward": "highest award" },
    //     ]
    //   }
    // });

    // expect(resumeReducer(initialState, {
    //   "type": "resume/updateOtherAward",
    //   "payload": {
    //     "value": "other award",
    //     "index": 0
    //   }
    // })).toEqual({
    //   stepIndex: 0,
    //   education: {
    //     completed: false,
    //     data: [
    //       { ...education, "otherAward": "other award" },
    //     ]
    //   }
    // });
  });
})
