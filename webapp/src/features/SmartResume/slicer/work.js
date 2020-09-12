import {
    validateDate,
    validateString,
    validateNonEmptyString
} from "utils/validator";

import { detectChangesForAllItem } from "./common";

export function anyWorkChanges(works) {
    return detectChangesForAllItem(works, work);
}

export function validateWork(data) {
    return Object.keys(data).every(key => validateWorkEntry(key, data[key]));
}

export function validateWorkEntry(name, value) {
    switch (name) {
        case "workName":
        case "workCompanyName":
        case "workCity":
        case "workCountry":
        case "workDescription":
            if (validateNonEmptyString(value)) {
                return true;
            } else if (validateString(value)) {
                return undefined;
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
        case "workEndDate":
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
