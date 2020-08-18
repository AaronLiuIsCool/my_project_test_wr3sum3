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
    updatePath: (state, action) => {
      state.path = action.payload;
    }
  },
});

export const { updateLanguage, updatePath } = appSlicers.actions;

export const selectLanguage = ({app}) => app.language;
export const selectPath = ({app}) => app.path;

export default appSlicers.reducer;
