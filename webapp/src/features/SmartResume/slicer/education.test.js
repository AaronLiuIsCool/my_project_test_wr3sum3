import moment from "moment";

import {
  initialState,
  education,
  validateEducation,
  validateEducationEntry,
} from "./education";
import { basic } from "./basic";
import resumeReducer, { educationSelectors } from ".";

describe("test education related functionalities", () => {
  test("it should validate education object", () => {
    expect(validateEducation({ ...education })).toBe.false;

    expect(
      validateEducation({
        schoolName: "test school",
        gpa: 3.88,
        startDate: moment().add(-4, "year"),
        graduateDate: moment(),
        major: "Computing Science",
        degree: "Bachelor",
        city: "Edmonton",
        country: "Canada",
        highestAward: undefined,
        otherAward: undefined,
      })
    ).toBe.true;
  });

  test("it should validate each entry of education", () => {
    const tests = [
      // name, value, result
      ["schoolName", undefined, false],
      ["schoolName", 123, false],
      ["schoolName", "u of a", true],
      ["schoolName", "", undefined],

      ["gpa", undefined, false],
      ["gpa", 5, false],
      ["gpa", -1, false],
      ["gpa", 4.1, false],
      ["gpa", 3.82, true],
      ["gpa", 4.0, true],
      ["gpa", 1.0, true],

      ["startDate", undefined, false],
      ["startDate", "test", false],
      ["startDate", Date.now(), false],
      ["startDate", new Date(), false],
      ["startDate", new Date().toISOString(), true],
      ["startDate", "2012", true],

      ["graduateDate", undefined, false],
      ["graduateDate", "test", false],
      ["graduateDate", Date.now(), false],
      ["graduateDate", new Date(), false],
      ["graduateDate", new Date().toISOString(), true],
      ["graduateDate", "2012", true],

      ["major", undefined, false],
      ["major", 123, false],
      ["major", "cs", true],
      ["major", "", undefined],

      ["degree", undefined, false],
      ["degree", 123, false],
      ["degree", "master", true],
      ["degree", "", undefined],

      ["city", undefined, false],
      ["city", 123, false],
      ["city", "北京", true],
      ["city", "", undefined],

      ["country", undefined, false],
      ["country", 123, false],
      ["country", "中国", true],
      ["country", "", undefined],
    ];

    tests.forEach((test, index) =>
      expect(validateEducationEntry(test[0], test[1])).toBe(test[2])
    );
  });

  it("test reducers", () => {
    const { education: addNewEducation } = resumeReducer(initialState, {
      type: "resume/addNewEducation",
    });
    expect(addNewEducation).toEqual({
      completed: false,
      data: [{ ...education }, { ...education }],
    });

    const { education: updateSchoolName } = resumeReducer(initialState, {
      type: "resume/updateSchoolName",
      payload: {
        value: "school",
        index: 0,
      },
    });
    expect(updateSchoolName).toEqual({
      completed: false,
      data: [{ ...education, schoolName: "school" }],
    });

    const { education: updateGPA } = resumeReducer(initialState, {
      type: "resume/updateGPA",
      payload: {
        value: "3.85",
        index: 0,
      },
    });
    expect(updateGPA).toEqual({
      completed: false,
      data: [{ ...education, gpa: "3.85" }],
    });

    const dateStr = new Date().toISOString();
    const { education: updateStartDate } = resumeReducer(initialState, {
      type: "resume/updateStartDate",
      payload: {
        value: dateStr,
        index: 0,
      },
    });
    expect(updateStartDate).toEqual({
      completed: false,
      data: [{ ...education, startDate: dateStr }],
    });

    const { education: updateGraduateDate } = resumeReducer(initialState, {
      type: "resume/updateGraduateDate",
      payload: {
        value: dateStr,
        index: 0,
      },
    });
    expect(updateGraduateDate).toEqual({
      completed: false,
      data: [{ ...education, graduateDate: dateStr }],
    });

    const { education: updateMajor } = resumeReducer(initialState, {
      type: "resume/updateMajor",
      payload: {
        value: "test major",
        index: 0,
      },
    });
    expect(updateMajor).toEqual({
      completed: false,
      data: [{ ...education, major: "test major" }],
    });

    const { education: updateDegree } = resumeReducer(initialState, {
      type: "resume/updateDegree",
      payload: {
        value: "test degree",
        index: 0,
      },
    });
    expect(updateDegree).toEqual({
      completed: false,
      data: [{ ...education, degree: "test degree" }],
    });

    const {education:updateEduCity} = resumeReducer(initialState, {
      "type": "resume/updateEduCity",
      "payload": {
        "value": "CITY",
        "index": 0
      }
    });
    expect(updateEduCity).toEqual({
        completed: false,
        data: [
          { ...education, "city": "CITY" },
        ]
    });

    const { education: updateCountry } = resumeReducer(initialState, {
      type: "resume/updateEduCountry",
      payload: {
        value: "CANADA",
        index: 0,
      },
    });
    expect(updateCountry).toEqual({
      completed: false,
      data: [{ ...education, country: "CANADA" }],
    });

    const { education: updateHighestAward } = resumeReducer(initialState, {
      type: "resume/updateHighestAward",
      payload: {
        value: "highest award",
        index: 0,
      },
    });
    expect(updateHighestAward).toEqual({
      completed: false,
      data: [{ ...education, highestAward: "highest award" }],
    });

    const { education: updateOtherAward } = resumeReducer(initialState, {
      type: "resume/updateOtherAward",
      payload: {
        value: "other award",
        index: 0,
      },
    });
    expect(updateOtherAward).toEqual({
      completed: false,
      data: [{ ...education, otherAward: "other award" }],
    });
  });
});
