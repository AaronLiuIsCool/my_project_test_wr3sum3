import { createSlice } from '@reduxjs/toolkit';

import education from './education';
import basic from './basic';

export const resumeSlice = createSlice({
  name: 'resume',
  initialState: {
    stepIndex: 0,
    education: education.initialState,
    basic: basic.initialState
  },
  reducers: {
    moveStep: (state, action) => {
      state.stepIndex = action.payload;
    },
    ...education.reducers,
    ...basic.reducers,
  }
});

export const { actions } = resumeSlice;

export const selectStep = ({ resume }) => resume.stepIndex;
export const educationSelectors = education.selectors;
export const basicSelectors = basic.selectors;

export default resumeSlice.reducer;
