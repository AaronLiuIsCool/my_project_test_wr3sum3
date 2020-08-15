import { validateDate, validateString, validateNonEmptyString } from 'utils/validator';
import { detectChangesForAllItem } from "./common";

export function anyProjectChanges(projects) {
    return detectChangesForAllItem(projects, project);
}

export function validateProject(data) {
    return Object.keys(data).every(key => validateProjectEntry(key, data[key]));
}

export function validateProjectEntry(name, value) {
    switch (name) {
        case 'projectRole':
        case 'projectCompanyName':
        case 'projectCity':
        case 'projectCountry':
        case 'projectDescription':
            if (validateNonEmptyString(value)) {
                return true;
            } else if (validateString(value)) {
                return undefined;
            } else {
                return false;
            }
        case 'projectStartDate':
        case 'projectEndDate':
            return typeof value === 'string' && validateDate(value);
        default:
            return true;
    }
}

export const project = {
    id: undefined,
    projectRole: "",
    currentProjectFlag: false,
    projectCompanyName: "",
    projectStartDate: "",
    projectEndDate: "",
    projectCity: "",
    projectCountry: "",
    projectDescription: "",
}

const initialState = {
    completed: false,
    data: [
        { ...project }
    ]
};


function updateField(state, idx, field, value) {
    state.project.data[idx][field] = value;
}

const reducers = {
    completeProject: (state) => {
        state.project.completed = true;
    },
    addNewProject: (state) => {
        state.project.data.push({ ...project });
    },
    updateProjectId: (state, action) => {
        updateField(state, action.payload.index, "id", action.payload.id);
    },
    updateProjectRole: (state, action) => {
        updateField(state, action.payload.index, "projectRole", action.payload.value);
    },
    updateCurrentProjectFlag: (state, action) => {
        // convert string to boolean
        updateField(state, action.payload.index, "currentProjectFlag", action.payload.value === "true");
    },
    updateProjectCompanyName: (state, action) => {
        updateField(state, action.payload.index, "projectCompanyName", action.payload.value);
    },
    updateProjectStartDate: (state, action) => {
        updateField(state, action.payload.index, "projectStartDate", action.payload.value);
    },
    updateProjectEndDate: (state, action) => {
        updateField(state, action.payload.index, "projectEndDate", action.payload.value);
    },
    updateProjectCity: (state, action) => {
        updateField(state, action.payload.index, "projectCity", action.payload.value);
    },
    updateProjectCountry: (state, action) => {
        updateField(state, action.payload.index, "projectCountry", action.payload.value);
    },
    updateProjectDescription: (state, action) => {
        updateField(state, action.payload.index, "projectDescription", action.payload.value);
    },
    appendProjectDescription: (state, action) => {
      const index = action.payload.index;
      const value = state.project.data[index].projectDescription.length === 0 ?
        action.payload.value : `${state.project.data[index].projectDescription}\n${action.payload.value}`;
      updateField( state, index, "projectDescription", value);
    }
}

const selectors = {
    selectProject: ({ resume }) => resume.project
}

export default {
    initialState,
    reducers,
    selectors
};
