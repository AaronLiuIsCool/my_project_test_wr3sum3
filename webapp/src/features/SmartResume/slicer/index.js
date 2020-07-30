import { createSlice } from '@reduxjs/toolkit';

import education from './education';

export const resumeSlice = createSlice({
  name: 'resume',
  initialState: {
    stepIndex: 0,
    education: education.initialState
  },
  reducers: {
    moveStep: (state, action) => {
      state.stepIndex = action.payload;
    },
    ...education.reducers
  }
});

export const { actions } = resumeSlice;

export const selectStep = ({ resume }) => resume.stepIndex;
export const educationSelectors = education.selectors;

export default resumeSlice.reducer;
