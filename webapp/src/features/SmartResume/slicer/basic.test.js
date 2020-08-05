
import { initialState, basic, validateBasic, validateBasicEntry } from './basic';
import { education } from './education';
import resumeReducer from '.';

describe('test basic form related functionalities', () => {
  test('it should validate basic object', () => {
    expect(validateBasic({ ...basic })).toBe.false;

    expect(validateBasic({
      nameCn: 'test name',
      nameEn: 'test name en',
      email: 'kuaidao@gmail.com',
      phone: '1234567890',
      city: 'Edmonton',
      linkedin: "",
      weblink: ""
    })).toBe.true;
  });

  test('it should validate each entry of basic', () => {
    const tests = [
      // name, value, result
      ['nameCn', undefined, false],
      ['nameCn', 123, false],
      ['nameCn', 'u of a', true],
      ['nameCn', '', undefined],

      ['nameEn', undefined, false],
      ['nameEn', 123, false],
      ['nameEn', 'u of a', true],
      ['nameEn', '', undefined],

     

      ['email', undefined, false],
      ['email', 123, false],
      ['email', '123@gmail', false],
      ['email', '123@gmail.com', true],
      ['email', '', false],


      ['phone', undefined, false],
      ['phone', '123456', false],
      ['phone', '6041231234', true],
      ['phone', '12345678901', true],
      ['phone', '123456789012', false],
      ['phone', '', false],

      
      ['city', undefined, false],
      ['city', 123, false],
      ['city', '北京', true],
      ['city', '', undefined],
      
      ['linkedin', undefined, false],
      ['linkedin', 123, false],
      ['linkedin', 'http://123', false],
      ['linkedin', 'http://123.com', true],
      ['linkedin', '', false],

      ['weblink', undefined, false],
      ['weblink', 123, false],
      ['weblink', 'http://123', false],
      ['weblink', 'http://123.com', true],
      ['weblink', '', false],
    ];

    tests.forEach((test, index) => expect(validateBasicEntry(test[0], test[1])).toBe(test[2]));
  });

  it('test reducers', () => {

    expect(resumeReducer(initialState, {
      "type": "resume/updateNameCn",
      "payload": {
        "value": "new name",
      }
    })).toEqual({
      stepIndex: 0,
      basic: {
        completed: false,
        data:  { ...basic, "nameCn": "new name" }
      },
      education: {
        completed: false,
        data: [
          { ...education },
        ]
      }
    });

    // expect(resumeReducer(initialState, {
    //   "type": "resume/updateNameEn",
    //   "payload": {
    //     "value": "new name",
    //   }
    // })).toEqual({
    //   stepIndex: 0,
    //   basic: {
    //     completed: false,
    //     data: [
    //       { ...basic, "nameEn": "new name" },
    //     ]
    //   }
    // });


    // expect(resumeReducer(initialState, {
    //   "type": "resume/updateEmail",
    //   "payload": {
    //     "value": "email@gmail.com",
    //   }
    // })).toEqual({
    //   stepIndex: 0,
    //   basic: {
    //     completed: false,
    //     data: [
    //       { ...basic, "email": "email@gmail.com" },
    //     ]
    //   }
    // });

    // expect(resumeReducer(initialState, {
    //   "type": "resume/updatePhone",
    //   "payload": {
    //     "value": "12345678",
    //   }
    // })).toEqual({
    //   stepIndex: 0,
    //   basic: {
    //     completed: false,
    //     data: [
    //       { ...basic, "phone": "12345678" },
    //     ]
    //   }
    // });

    // expect(resumeReducer(initialState, {
    //   "type": "resume/updateCity",
    //   "payload": {
    //     "value": "CITY",
    //   }
    // })).toEqual({
    //   stepIndex: 0,
    //   basic: {
    //     completed: false,
    //     data: [
    //       { ...basic, "city": "CITY" },
    //     ]
    //   }
    // });


    // expect(resumeReducer(initialState, {
    //   "type": "resume/updateLinkedin",
    //   "payload": {
    //     "value": "http://123.com",
    //   }
    // })).toEqual({
    //   stepIndex: 0,
    //   basic: {
    //     completed: false,
    //     data: [
    //       { ...basic, "linkedin": "http://123.com" },
    //     ]
    //   }
    // });

    // expect(resumeReducer(initialState, {
    //   "type": "resume/updateWeblink",
    //   "payload": {
    //     "value": "http://123.com",
    //   }
    // })).toEqual({
    //   stepIndex: 0,
    //   basic: {
    //     completed: false,
    //     data: [
    //       { ...basic, "weblink": "http://123.com" },
    //     ]
    //   }
    // });
  });
})
