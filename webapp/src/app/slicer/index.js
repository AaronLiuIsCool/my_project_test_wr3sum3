import { createSlice } from '@reduxjs/toolkit';

const appSlicers = createSlice({
  name: 'app',
  initialState: {
    language: 'zh'
  },
  reducers: {
    updateLanguage: (state, action) => {
      state.language = action.payload;
    },
  },
});

export const { updateLanguage } = appSlicers.actions;

export const selectLanguage = ({app}) => app.language;

export default appSlicers.reducer;
