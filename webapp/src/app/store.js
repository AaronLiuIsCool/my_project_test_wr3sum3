import { configureStore } from '@reduxjs/toolkit';
import resumeReducer from 'features/SmartResume/slicers/resumeSlicer';
import counterReducer from 'features/CounterExample/counterSlice.js';

export default configureStore({
  reducer: {
    counter: counterReducer,
    resume: resumeReducer
  },
});
