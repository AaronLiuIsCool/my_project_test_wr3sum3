import { createSlice } from "@reduxjs/toolkit";

import assistant from './assistant';
import photoReference from "./photoReference";
import basic, {validateBasic} from "./basic";
import education, {validateEducation} from "./education";
import work, {validateWork} from "./work";
import project, {validateProject} from "./project";
import volunteer, {validateVolunteer} from "./volunteer";
import certificate, {validateCertificate} from "./certificate";
import ratings from "./ratings";
import resumeBuilder from "./resumeBuilder";
import { resumeAdaptor } from "../utils/slicerAdaptor";

export const resumeSlice = createSlice({
    name: "resume",
    initialState: {
        id: undefined,
        stepIndex: 0,

        assistant: assistant.initialState,
        photoReference: photoReference.initialState,
        basic: basic.initialState,
        education: education.initialState,
        work: work.initialState,
        project: project.initialState,
        certificate: certificate.initialState,
        volunteer: volunteer.initialState,
        ratings: ratings.initialState,
        resumeBuilder: resumeBuilder.initialState,
    },
    reducers: {
        setId: (state, action) => {
            state.id = action.payload;
        },

        setResume: (state, action) => {
            const resume = action.payload;
            return { ...state, ...resumeAdaptor(resume) };
        },

        moveStep: (state, action) => {
            state.stepIndex = action.payload;
        },

        ...assistant.reducers,
        ...photoReference.reducers,
        ...basic.reducers,
        ...education.reducers,
        ...work.reducers,
        ...project.reducers,
        ...volunteer.reducers,
        ...certificate.reducers,
        ...ratings.reducers,
        ...resumeBuilder.reducers,
    }
});

export const { actions } = resumeSlice;

export const selectId = ({ resume }) => resume.id;
export const selectStep = ({ resume }) => resume.stepIndex;
export const selectResume = ({ resume }) => resume;
export const selectValidatedResume = ({ resume }) => {
  const {photoReference, basic, education, work,
    project, volunteer, certificate} = resume;

  return {
    photoReference,
    basic: validateBasic(basic) ? basic : undefined,
    education: {...education, data: education.data.filter(validateEducation)},
    work: {...work, data: work.data.filter(validateWork)},
    project: {...project, data: project.data.filter(validateProject)},
    volunteer: {...volunteer, data: volunteer.data.filter(validateVolunteer)},
    certificate: {...certificate, data: certificate.data.filter(validateCertificate)},
  };
};

export const assistantSelectors = assistant.selectors;
export const photoReferenceSelectors = photoReference.selectors;
export const basicSelectors = basic.selectors;
export const educationSelectors = education.selectors;
export const workSelectors = work.selectors;
export const projectSelectors = project.selectors;
export const volunteerSelectors = volunteer.selectors;
export const certificateSelectors = certificate.selectors;
export const ratingsSelectors = ratings.selectors;
export const resumeBuilderSelectors = resumeBuilder.selectors;

export default resumeSlice.reducer;
