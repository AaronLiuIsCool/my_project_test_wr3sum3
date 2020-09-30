import { validateDate, validateString, validateNonEmptyString } from 'utils/validator';

export function validateEducation(data) {
    return Object.keys(data).every(key => validateEducationEntry(key, data[key]));
}

export function validateEducationEntry(name, value) {
    switch (name) {
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
    id: undefined,
    schoolName: "",
    gpa: "",
    startDate: "",
    graduateDate: "",
    major: "",
    degree: "",
    city: "",
    country: "",
    highestAward: "",
    otherAward: ""
}

const initialState = {
    completed: false,
    data: [{
        ...education
    }]
};

function updateField(state, idx, field, value) {
    state.education.data[idx][field] = value;
}

const reducers = {
    completeEducation: (state) => {
        state.education.completed = true;
    },
    updateEducationFromResumeSDK: (state, action) => {
        updateField(state, action.payload.index, "schoolName", action.payload.data.edu_college);
        updateField(state, action.payload.index, "startDate", action.payload.data.start_date);
        updateField(state, action.payload.index, "graduateDate", action.payload.data.end_date);
        updateField(state, action.payload.index, "gpa", action.payload.data.edu_gpa);
        updateField(state, action.payload.index, "major", action.payload.data.edu_major);
        updateField(state, action.payload.index, "degree", action.payload.data.edu_degree);
    },
    addNewEducation: (state) => {
        state.education.data.push({...education});
    },
    updateEducationId: (state, action) => {
        updateField(state, action.payload.index, "id", action.payload.id);
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
    updateEduCity: (state, action) => {
        updateField(state, action.payload.index, "city", action.payload.value);
    },
    updateEduCountry: (state, action) => {
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
    selectEducation: ({ resume }) => resume.education
}

export default {
    initialState,
    reducers,
    selectors
};
