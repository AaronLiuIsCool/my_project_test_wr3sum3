import { validateDate, validateString, validateNonEmptyString } from 'utils/validator';
import { detectChangesForAllItem } from "./common";

export function anyVolunteerChanges(volunteers) {
    return detectChangesForAllItem(volunteers, volunteer);
}
export function validateVolunteer(data) {
    return Object.keys(data).every(key => validateVolunteerEntry(key, data[key]));
}

export function validateVolunteerEntry(name, value) {
    switch (name) {
        case 'volunteerRole':
        case 'volunteerCompanyName':
        case 'volunteerCity':
        case 'volunteerCountry':
        case 'volunteerDescription':
            if (validateNonEmptyString(value)) {
                return true;
            } else if (validateString(value)) {
                return undefined;
            } else {
                return false;
            }
        case 'volunteerStartDate':
        case 'volunteerEndDate':
            return typeof value === 'string' && validateDate(value);
        default:
            return true;
    }
}

export const volunteer = {
    id: undefined,
    volunteerRole: "",
    currentVolunteerFlag: false,
    volunteerCompanyName: "",
    volunteerStartDate: "",
    volunteerEndDate: "",
    volunteerCity: "",
    volunteerCountry: "",
    volunteerDescription: "",
}

const initialState = {
    completed: false,
    data: [
        { ...volunteer }
    ]
};


function updateField(state, idx, field, value) {
    state.volunteer.data[idx][field] = value;
}

const reducers = {
    completeVolunteer: (state) => {
        state.volunteer.completed = true;
    },
    addNewVolunteer: (state) => {
        state.volunteer.data.push({ ...volunteer });
    },
    updateVolunteerId: (state, action) => {
        updateField(state, action.payload.index, "id", action.payload.id);
    },
    updateVolunteerRole: (state, action) => {
        updateField(state, action.payload.index, "volunteerRole", action.payload.value);
    },
    updateCurrentVolunteerFlag: (state, action) => {
        // convert string to boolean
        updateField(state, action.payload.index, "currentVolunteerFlag", action.payload.value === "true");
    },
    updateVolunteerCompanyName: (state, action) => {
        updateField(state, action.payload.index, "volunteerCompanyName", action.payload.value);
    },
    updateVolunteerStartDate: (state, action) => {
        updateField(state, action.payload.index, "volunteerStartDate", action.payload.value);
    },
    updateVolunteerEndDate: (state, action) => {
        updateField(state, action.payload.index, "volunteerEndDate", action.payload.value);
    },
    updateVolunteerCity: (state, action) => {
        updateField(state, action.payload.index, "volunteerCity", action.payload.value);
    },
    updateVolunteerCountry: (state, action) => {
        updateField(state, action.payload.index, "volunteerCountry", action.payload.value);
    },
    updateVolunteerDescription: (state, action) => {
        updateField(state, action.payload.index, "volunteerDescription", action.payload.value);
    },
}

const selectors = {
    selectVolunteer: ({ resume }) => resume.volunteer
}

export default {
    initialState,
    reducers,
    selectors
};
