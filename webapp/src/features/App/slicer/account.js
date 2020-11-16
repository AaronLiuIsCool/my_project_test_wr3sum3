import { createSlice } from '@reduxjs/toolkit';

const accountSlicer = createSlice({
  name: 'account',
  initialState: {},
  reducers: {
    updateAccount: (state, action) => {
      const account = action.payload;
      return { ...state, ...account };
    },
    deleteResume: (state, action) => {
      const resumeIdForDelete = action.payload;
      state.resumes = state.resumes.filter(({ resumeId }) => resumeId !== resumeIdForDelete);
    }
  },
});

export const { updateAccount, deleteResume } = accountSlicer.actions;

export const selectResumes = ({ account }) => account.resumes;
export const selectAccountName = ({ account }) => account.name;
export const selectAccount = ({account}) => account;

export default accountSlicer.reducer;
