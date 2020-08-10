import { createSlice } from "@reduxjs/toolkit";

import education from "./education";
import basic from "./basic";
import work from "./work";
import project from "./project";
import volunteer from "./volunteer";
import certificate from "./certificate";

export const resumeSlice = createSlice({
  name: "resume",
  initialState: {
    stepIndex: 0,
    education: education.initialState,
    basic: basic.initialState,
    work: work.initialState,
    project: project.initialState,
    certificate: certificate.initialState,
    volunteer: volunteer.initialState
  },
  reducers: {
    moveStep: (state, action) => {
      state.stepIndex = action.payload;
    },
    ...education.reducers,
    ...basic.reducers,
    ...work.reducers,
    ...project.reducers,
    ...volunteer.reducers,
    ...certificate.reducers,
  }
});

export const { actions } = resumeSlice;

export const selectStep = ({ resume }) => resume.stepIndex;
export const educationSelectors = education.selectors;
export const basicSelectors = basic.selectors;
export const workSelectors = work.selectors;
export const projectSelectors = project.selectors;
export const volunteerSelectors = volunteer.selectors;
export const certificateSelectors = certificate.selectors;

export default resumeSlice.reducer;
