import { validateDate, validateNonEmptyString } from 'utils/validator';
import { detectChangesForAllItem } from "./common";

export function anyVolunteerChanges(volunteers) {
    return detectChangesForAllItem(volunteers, volunteer);
}
export function validateVolunteer(data) {
    return Object.keys(data).every(key => validateVolunteerEntry(key, data[key], data));
}

export function validateVolunteerEntry(name, value, data) {
    switch (name) {
        case 'volunteerDescription':
            if(value === '*') return false // because we added * before each line => to prevent the case whern user deletes everything
        // eslint-disable-next-line
        case 'volunteerRole':
        case 'volunteerCompanyName':
        case 'volunteerCity':
        case 'volunteerCountry':
            if (validateNonEmptyString(value)) {
                return true;
            } else {
                return false;
            }
        case 'volunteerStartDate':
            return typeof value === 'string' && validateDate(value);
        case 'volunteerEndDate':
            if(!data?.currentVolunteerFlag) {
                return true
            }
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
    appendVolunteerDescription: (state, action) => {
      const index = action.payload.index;
      const value = state.volunteer.data[index].volunteerDescription.length === 0 ?
        action.payload.value : `${state.volunteer.data[index].volunteerDescription}\n${action.payload.value}`;
      updateField( state, index, "volunteerDescription", value);
    }
}

const selectors = {
    selectVolunteer: ({ resume }) => resume.volunteer
}

export default {
    initialState,
    reducers,
    selectors
};
