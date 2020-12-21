import {
  initialState,
  resumeBuilder,
} from "./resumeBuilder";
import resumeReducer from ".";

describe("test resume Builder related functionalities", () => {

  it("test reducers", () => {
    const { resumeBuilder: updateColor } = resumeReducer(initialState, {
      type: "resume/updateColor",
      payload: {
        color: "new color",
      },
    });
    expect(updateColor).toEqual({
      data: { ...resumeBuilder, color: "new color" },
    });
  });

  it("test updateResumeBase64Str", () => {
    const { resumeBuilder: updateResumeBase64Str } = resumeReducer(initialState, {
      type: "resume/updateResumeBase64Str",
      payload: {
        value: "new base64 string",
      },
    });
    expect(updateResumeBase64Str).toEqual({
      data: { ...resumeBuilder, base64: "new base64 string" },
    });
  });
  
  it("test updateResumeLanguage", () => {
    const { resumeBuilder: updateResumeLanguage } = resumeReducer(initialState, {
      type: "resume/updateResumeLanguage",
      payload: {
        language: "en",
      },
    });
    expect(updateResumeLanguage).toEqual({
      data: { ...resumeBuilder, language: "en" },
    });
  });
});
