import { createSlice } from '@reduxjs/toolkit';

export const resumeSlice = createSlice({
  name: 'resume',
  initialState: {
    employers: [{
      name: 'Kuaidao',
      title: 'Intern'
    }, {
      name: 'Kuaidao',
      title: 'Software Engineer'
    }] // default should be empty [], here is for demo purposes
  },
  reducers: {
    addEmployer: (state, action) => {
      state.employers.push(action.payload)
    }
  }
});

export const { addEmployer } = resumeSlice.actions;

export const selectEmployers = ({ resume }) => resume.employers;

export default resumeSlice.reducer;
