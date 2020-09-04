import { configureStore } from '@reduxjs/toolkit';

import appReducer from 'features/App/slicer';
import accountReducer from 'features/App/slicer/account';
import resumeReducer from 'features/SmartResume/slicer';

export default configureStore({
  reducer: {
    app: appReducer,
    account: accountReducer,
    resume: resumeReducer
  },
});
