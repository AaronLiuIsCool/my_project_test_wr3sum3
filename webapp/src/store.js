import { configureStore } from '@reduxjs/toolkit';

import appReducer from 'features/App/slicer';
import resumeReducer from 'features/SmartResume/slicer';

export default configureStore({
  reducer: {
    app: appReducer,
    resume: resumeReducer
  },
});
