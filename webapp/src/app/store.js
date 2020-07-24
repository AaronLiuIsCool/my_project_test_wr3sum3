import { configureStore } from '@reduxjs/toolkit';

import resumeReducer from 'features/SmartResume/slicer';
import counterReducer from 'features/CounterExample/counterSlice';
import appReducer from './slicer';

export default configureStore({
  reducer: {
    app: appReducer,
    counter: counterReducer,
    resume: resumeReducer
  },
});
