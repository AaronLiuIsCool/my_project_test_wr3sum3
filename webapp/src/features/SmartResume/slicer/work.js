import {
    validateDate,
    validateNonEmptyString
} from "utils/validator";

import { detectChangesForAllItem } from "./common";

export function anyWorkChanges(works) {
    return detectChangesForAllItem(works, work);
}

export function validateWork(data) {
    return Object.keys(data).every(key => validateWorkEntry(key, data[key], data));
}

export function validateWorkEntry(name, value, data) {
    switch (name) {
        case "workDescription":
            if(value === '*') return false // because we added * before each line => to prevent the case whern user deletes everything
            // eslint-disable-next-line
        case "workName":
        case "workCompanyName":
        case "workCity":
        case "workCountry":
            if (validateNonEmptyString(value)) {
                return true;
            } else {
                return false;
            }
        case "currentWorkFlag":
            return (
                value === true ||
                value === false ||
                toString.call(value) === "[object Boolean]"
            );
        case "workStartDate":
            return typeof value === "string" && validateDate(value);
        case "workEndDate":
            if(!data?.currentWorkFlag) {
                return true;
            }
            
            return typeof value === "string" && validateDate(value);
        default:
            return true;
    }
}

export const work = {
    id: undefined,
    workName: "",
    currentWorkFlag: false,
    workCompanyName: "",
    workStartDate: "",
    workEndDate: "",
    workDescription: "",
    workCity: "",
    workCountry: ""
};

const initialState = {
    completed: false,
    data: [{ ...work }]
};

function updateField(state, idx, field, value) {
    state.work.data[idx][field] = value;
}

const reducers = {
    completeWork: (state) => {
        state.work.completed = true;
    },
    updateWorkFromResumeSDK: (state, action) => {
        updateField(state, action.payload.index, "workName", action.payload.data.job_position);
        updateField(state, action.payload.index, "workCompanyName", action.payload.data.job_cpy);
        updateField(state, action.payload.index, "workStartDate", action.payload.data.start_date);
        updateField(state, action.payload.index, "workEndDate", action.payload.data.end_date);
        updateField(state, action.payload.index, "workDescription", action.payload.data.job_content);
        if (action.payload.data.end_date) {
            updateField(state, action.payload.index, "currentWorkFlag", true);
        }
    },
    addNewWork: state => {
        state.work.data.push({ ...work });
    },
    updateWorkId: (state, action) => {
        updateField(state, action.payload.index, "id", action.payload.id);
    },
    updateWorkName: (state, action) => {
        updateField(state, action.payload.index, "workName", action.payload.value);
    },
    updateCurrentWorkFlag: (state, action) => {
        // convert string to boolean
        updateField(
            state,
            action.payload.index,
            "currentWorkFlag",
            action.payload.value === "true"
        );
    },
    updateWorkCompanyName: (state, action) => {
        updateField(
            state,
            action.payload.index,
            "workCompanyName",
            action.payload.value
        );
    },
    updateWorkStartDate: (state, action) => {
        updateField(
            state,
            action.payload.index,
            "workStartDate",
            action.payload.value
        );
    },
    updateWorkEndDate: (state, action) => {
        updateField(
            state,
            action.payload.index,
            "workEndDate",
            action.payload.value
        );
    },
    updateWorkCity: (state, action) => {
        updateField(state, action.payload.index, "workCity", action.payload.value);
    },
    updateWorkCountry: (state, action) => {
        updateField(state, action.payload.index, "workCountry", action.payload.value);
    },
    updateWorkDescription: (state, action) => {
        updateField(
            state,
            action.payload.index,
            "workDescription",
            action.payload.value
        );
    },
    appendWorkDescription: (state, action) => {
      const index = action.payload.index;
      const value = state.work.data[index].workDescription.length === 0 ?
        action.payload.value : `${state.work.data[index].workDescription}\n${action.payload.value}`;
      updateField( state, index, "workDescription", value);
    },
    removeWork: (state, action) => {
        const index = action.payload.index;
        if(state.work.data.length > 1) {
            state.work.data.splice(index, 1) 
        } else {
            state.work.data = [{
                ...work
            }]
        }        
    }
};

const selectors = {
    selectWork: ({ resume }) => resume.work
};

export default {
    initialState,
    reducers,
    selectors
};
