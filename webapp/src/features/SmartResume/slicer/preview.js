
export const preview = {
  header: 0,
  work: 0,
  volunteer: 0,
}

const initialState = {
  data: {
      ...preview
  }
};

function updateField(state, field, value) {
  state.preview.data[field] = value;
}

const reducers = {
  
  updatelineNum: (state, action) => {
      updateField(state, action.payload.section, action.payload.value);
  },
}

const selectors = {
  selectPreview: ({ resume }) => resume.preview
}

export default {
  initialState,
  reducers,
  selectors
};
