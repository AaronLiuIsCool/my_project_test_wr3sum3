import { validateDate, validateString, validateNonEmptyString } from 'utils/validator';

export function validateEducation(data) {
  return Object.keys(data).every(key => validateEducationEntry(key, data[key]));
}

export function validateEducationEntry(name, value) {
  switch(name) {
    case 'schoolName':
    case 'major':
    case 'degree':
    case 'city':
    case 'country':
      if (validateNonEmptyString(value)) {
        return true;
      } else if (validateString(value)) {
        return undefined;
      } else {
        return false;
      }
    case 'gpa':
      try {
        const gpaValue = Number(value);
        return gpaValue <= 4 && gpaValue >= 0;
      } catch (e) {
        return false;
      }
    case 'startDate':
    case 'graduateDate':
      return typeof value === 'string' && validateDate(value);
    default:
      return true;
  }
}

export const education = {
    schoolName: undefined,
    gpa: undefined,
    startDate: undefined,
    graduateDate: undefined,
    major: undefined,
    degree: undefined,
    city: undefined,
    country: undefined,
    highestAward: undefined,
    otherAward: undefined
}

const initialState = {
  completed: false,
  data: [
    {...education}
  ]
};


function updateField(state, idx, field, value) {
  state.education.data[idx][field] = value;
}

const reducers = {
  addNewEducation: (state) => {
    state.education.data.push({...education});
  },
  updateSchoolName: (state, action) => {
    updateField(state, action.payload.index, "schoolName", action.payload.value);
  },
  updateGPA: (state, action) => {
    updateField(state, action.payload.index, "gpa", action.payload.value);
  },
  updateStartDate: (state, action) => {
    updateField(state, action.payload.index, "startDate", action.payload.value);
  },
  updateGraduateDate: (state, action) => {
    updateField(state, action.payload.index, "graduateDate", action.payload.value);
  },
  updateMajor: (state, action) => {
    updateField(state, action.payload.index, "major", action.payload.value);
  },
  updateDegree: (state, action) => {
    updateField(state, action.payload.index, "degree", action.payload.value);
  },
  updateCity: (state, action) => {
    updateField(state, action.payload.index, "city", action.payload.value);
  },
  updateCountry: (state, action) => {
    updateField(state, action.payload.index, "country", action.payload.value);
  },
  updateHighestAward: (state, action) => {
    updateField(state, action.payload.index, "highestAward", action.payload.value);
  },
  updateOtherAward: (state, action) => {
    updateField(state, action.payload.index, "otherAward", action.payload.value);
  }
}

const selectors = {
  selectEducation : ({ resume }) => resume.education
}

export default {
  initialState,
  reducers,
  selectors
};
