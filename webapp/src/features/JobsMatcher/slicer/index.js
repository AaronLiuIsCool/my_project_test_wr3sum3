import { createSlice } from '@reduxjs/toolkit';

const jobsMatcherSlicers = createSlice({
  name: 'jobs',
  initialState: {
    bookmarked: []
  },
  reducers: {
    addBookmark: (state, action) => {
      const jobUUID = action.payload;
      if (!jobUUID) {
        return;
      }
      state.bookmarked.indexOf(jobUUID) === -1 && state.bookmarked.push(jobUUID);
    },
    removeBookmark: (state, action) => {
      const forRemoval = action.payload;
      if (!forRemoval) {
        return;
      }
      state.bookmarked = state.bookmarked.filter(jobUUID => jobUUID !== forRemoval);
    },
    setBookmarkedList: (state, action) => {
      state.bookmarked = action?.payload || [];
    }
  },
});

export const { addBookmark, removeBookmark, setBookmarkedList } = jobsMatcherSlicers.actions;

export const selectBookmarkedJobs = ({jobs}) => jobs.bookmarked;

export default jobsMatcherSlicers.reducer;
