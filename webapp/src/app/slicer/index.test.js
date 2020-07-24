import appReducer, { updateLanguage} from '.';

describe("appSlicer tests", () => {
  test("it should return correct initial state", () => {
    expect(appReducer(undefined, {})).toEqual({
      "language": 'zh'
    });
  });

  test("it should get correct action for updateLanguage", () => {
    expect(updateLanguage("test")).toEqual({
      "payload": "test",
      "type": "app/updateLanguage"
    });
  });

  test("it should update language correctly", () => {
    expect(appReducer({}, {
      "payload": "test",
      "type": "app/updateLanguage"
    })).toEqual({
      "language": "test"
    });
  });
});
