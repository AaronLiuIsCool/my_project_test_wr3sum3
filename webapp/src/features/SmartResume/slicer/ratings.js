const ratings = {
  score: 0,
  basicInfo: [],
};
const initialState = {
  data: {
    ...ratings,
  },
};
const reducers = {
  updateBasicInfoRating: (state, action) => {
    state.ratings.data.basicInfo = action.payload;
    console.log(action);
    console.log(state.ratings);
  },
  updateLayoutRating: (state, action) => {
      
  }
};
const selectors = {
  selectRatings: ({ resume }) => resume.ratings,
};

export default {
  initialState,
  reducers,
  selectors,
};
