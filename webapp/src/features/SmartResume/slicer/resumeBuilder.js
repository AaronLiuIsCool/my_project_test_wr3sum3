export const resumeBuilder = {
  color: "#3e89ec"
};

const initialState = {
  data: {
      ...resumeBuilder
  }
};
function updateField(state, field, value) {
  state.resumeBuilder.data[field] = value;
}

const reducers = {
  updateColor: (state, action) => {
    updateField(state, "color", action.payload.color);
},
};

const selectors = {
  selectresumeBuilder: ({ resume }) => resume.resumeBuilder
};

export default {
  initialState,
  reducers,
  selectors
};
