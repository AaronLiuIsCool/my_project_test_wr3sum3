import { createSlice } from '@reduxjs/toolkit';

export const resumeSlice = createSlice({
  name: 'resume',
  initialState: {
    stepIndex: 0,
    employers: [{
      name: 'Kuaidao',
      title: 'Intern'
    }, {
      name: 'Kuaidao',
      title: 'Software Engineer'
    }] // default should be empty [], here is for demo purposes
  },
  reducers: {
    moveStep: (state, action) => {
      state.stepIndex = action.payload;
    },
    addEmployer: (state, action) => {
      state.employers.push(action.payload)
    }
  }
});

export const { addEmployer, moveStep} = resumeSlice.actions;

export const selectEmployers = ({ resume }) => resume.employers;
export const selectStep = ({ resume }) => resume.stepIndex;

export default resumeSlice.reducer;
