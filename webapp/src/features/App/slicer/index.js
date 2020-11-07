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
    updateAuthInfo: (state, action) => {
      const loginBypass = localStorage.getItem('kdr-login-bypass') === 'true'; // TODO: Remove before we go out to prod
      const authInfo = action?.payload || {};
      state.authenticated = loginBypass || authInfo.success;
      state.iam = authInfo.iam;
    }
  },
});

export const { updateLanguage, updateAuthInfo } = appSlicers.actions;

export const selectLanguage = ({app}) => app.language;
export const selectAuthentication = ({app}) => app.authenticated;
export const selectUserId = ({app}) => app.iam && app.iam.userId;

export default appSlicers.reducer;
