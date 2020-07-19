import { createSlice } from '@reduxjs/toolkit';

export const appSlicers = createSlice({
  name: 'app',
  initialState: {
    language: 'zh'
  },
  reducers: {
    updateLocale: (state, action) => {
      state.locale += action.payload;
    },
  },
});

export const { updateLocale } = appSlicers.actions;

export const selectLanguage = ({app}) => app.language;

export default appSlicers.reducer;
