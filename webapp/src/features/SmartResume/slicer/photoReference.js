const initialState = {
  url: ""
};

const reducers = {
  updatePhotoReference: (state, action) => {
    state.photoReference.url = action.payload.value;
  }
};

const selectors = {
  selectPhotoReference: ({ resume }) => resume.photoReference
}

export default {
  initialState,
  reducers,
  selectors
};
