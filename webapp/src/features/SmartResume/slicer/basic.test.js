import {
  initialState,
  basic,
  validateBasic,
  validateBasicEntry,
} from "./basic";
import resumeReducer from ".";

describe("test basic form related functionalities", () => {
  test("it should validate basic object", () => {
    expect(validateBasic({ ...basic })).toBe.false;

    expect(
      validateBasic({
        nameCn: "test name",
        nameEn: "test name en",
        email: "kuaidao@gmail.com",
        phone: "1234567890",
        city: "Edmonton",
        linkedin: "",
        weblink: "",
      })
    ).toBe.true;
  });

  test("it should validate each entry of basic", () => {
    const tests = [
      // name, value, result
      ["nameCn", undefined, false],
      ["nameCn", 123, false],
      ["nameCn", "u of a", true],
      ["nameCn", "", false],

      ["nameEn", undefined, false],
      ["nameEn", 123, false],
      ["nameEn", "u of a", true],
      ["nameEn", "", false],

      ["email", undefined, false],
      ["email", 123, false],
      ["email", "123@gmail", false],
      ["email", "123@gmail.com", true],
      ["email", "", false],

      ["phone", undefined, false],
      ["phone", "123456", false],
      ["phone", "6041231234", true],
      ["phone", "12345678901", true],
      ["phone", "123456789012", false],
      ["phone", "", false],

      ["city", undefined, false],
      ["city", 123, false],
      ["city", "北京", true],
      ["city", "", false],

    ];

    tests.forEach((test, index) =>
      expect(validateBasicEntry(test[0], test[1])).toBe(test[2])
    );
  });

  it("test reducers", () => {
    const { basic: updateNameCn } = resumeReducer(initialState, {
      type: "resume/updateNameCn",
      payload: {
        value: "new name",
      },
    });
    expect(updateNameCn).toEqual({
      completed: false,
      data: { ...basic, nameCn: "new name" },
    });

    const { basic: updateNameEn } = resumeReducer(initialState, {
      type: "resume/updateNameEn",
      payload: {
        value: "new name",
      },
    });
    expect(updateNameEn).toEqual({
      completed: false,
      data: { ...basic, nameEn: "new name" },
    });

    const { basic: updateEmail } = resumeReducer(initialState, {
      type: "resume/updateEmail",
      payload: {
        value: "email@gmail.com",
      },
    });
    expect(updateEmail).toEqual({
      completed: false,
      data: { ...basic, email: "email@gmail.com" },
    });

    const { basic: updatePhone } = resumeReducer(initialState, {
      type: "resume/updatePhone",
      payload: {
        value: "12345678",
      },
    });
    expect(updatePhone).toEqual({
      completed: false,
      data: { ...basic, phone: "12345678" },
    });

    const { basic: updateCity } = resumeReducer(initialState, {
      type: "resume/updateCity",
      payload: {
        value: "CITY",
      },
    });
    expect(updateCity).toEqual({
      completed: false,
      data: { ...basic, city: "CITY" },
    });

    const { basic: updateLinkedin } = resumeReducer(initialState, {
      type: "resume/updateLinkedin",
      payload: {
        value: "http://123.com",
      },
    });
    expect(updateLinkedin).toEqual({
      completed: false,
      data: { ...basic, linkedin: "http://123.com" },
    });

    const { basic: updateWeblink } = resumeReducer(initialState, {
      type: "resume/updateWeblink",
      payload: {
        value: "http://123.com",
      },
    });
    expect(updateWeblink).toEqual({
      completed: false,
      data: { ...basic, weblink: "http://123.com" },
    });
  });
});
