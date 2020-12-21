export const resumeBuilder = {
  color: "#3e89ec",
  base64: "",
  language: "zh"
};

const initialState = {
  data: {
    ...resumeBuilder,
  },
};
function updateField(state, field, value) {
  state.resumeBuilder.data[field] = value;
}

const reducers = {
  updateColor: (state, action) => {
    updateField(state, "color", action.payload.color);
  },
  updateResumeBase64Str: (state, action) => {
    updateField(state, "base64", action.payload.value);
  },
  updateResumeLanguage: (state, action) => {
    updateField(state, "language", action.payload.language);
  },
};

const selectors = {
  selectResumeBuilder: ({ resume }) => resume.resumeBuilder,
};

export default {
  initialState,
  reducers,
  selectors,
};
