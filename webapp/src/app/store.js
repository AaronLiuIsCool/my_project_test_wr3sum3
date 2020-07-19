import { configureStore } from '@reduxjs/toolkit';

import resumeReducer from 'features/SmartResume/slicers/resumeSlicer';
import counterReducer from 'features/CounterExample/counterSlice.js';
import appReducer from './appSlicer';

export default configureStore({
  reducer: {
    app: appReducer,
    counter: counterReducer,
    resume: resumeReducer
  },
});
