import {
  initialState,
  photoReference,
} from "./photoReference";
import resumeReducer from ".";

describe("test basic form related functionalities", () => {

  it("test reducers", () => {
    const { photoReference: updatePhotoReference } = resumeReducer(initialState, {
      type: "resume/updatePhotoReference",
      payload: {
        value: "new new url",
      },
    });
    expect(updatePhotoReference).toEqual({
      url: "new new url",
    });
  });
});
