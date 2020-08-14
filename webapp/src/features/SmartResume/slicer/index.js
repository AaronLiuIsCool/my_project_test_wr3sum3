import { createSlice } from "@reduxjs/toolkit";

import education from "./education";
import basic from "./basic";
import work from "./work";
import project from "./project";
import volunteer from "./volunteer";
import certificate from "./certificate";
import preview from "./preview";

export const resumeSlice = createSlice({
    name: "resume",
    initialState: {
        id: undefined,
        stepIndex: 0,

        education: education.initialState,
        basic: basic.initialState,
        work: work.initialState,
        project: project.initialState,
        certificate: certificate.initialState,
        volunteer: volunteer.initialState,
        preview: preview.initialState
    },
    reducers: {
        setId: (state, action) => {
            state.id = action.payload;
        },

        moveStep: (state, action) => {
            state.stepIndex = action.payload;
        },

        ...education.reducers,
        ...basic.reducers,
        ...work.reducers,
        ...project.reducers,
        ...volunteer.reducers,
        ...certificate.reducers,
        ...preview.reducers,
    }
});

export const { actions } = resumeSlice;

export const selectId = ({ resume }) => resume.id;
export const selectStep = ({ resume }) => resume.stepIndex;
export const selectResume = ({ resume }) => resume;

export const educationSelectors = education.selectors;
export const basicSelectors = basic.selectors;
export const workSelectors = work.selectors;
export const projectSelectors = project.selectors;
export const volunteerSelectors = volunteer.selectors;
export const certificateSelectors = certificate.selectors;
export const previewSelectors = preview.selectors;

export default resumeSlice.reducer;
