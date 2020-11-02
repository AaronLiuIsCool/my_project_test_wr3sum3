import { validateDate, validateNonEmptyString } from 'utils/validator';
import { detectChangesForAllItem } from "./common";

export function anyProjectChanges(projects) {
    return detectChangesForAllItem(projects, project);
}

export function validateProject(data) {
    return Object.keys(data).every(key => validateProjectEntry(key, data[key], data));
}

export function validateProjectEntry(name, value, data) {
    switch (name) {
        case 'projectDescription':
            if(value === '*') return false // because we added * before each line => to prevent the case whern user deletes everything
            // eslint-disable-next-line
        case 'projectRole':
        case 'projectCompanyName':
        case 'projectCity':
        case 'projectCountry':
            if (validateNonEmptyString(value)) {
                return true;
            } else {
                return false;
            }
        case 'projectStartDate':
            return typeof value === 'string' && validateDate(value);
        case 'projectEndDate':
            if(!data?.currentProjectFlag) {
                return true
            }
            return typeof value === 'string' && validateDate(value);
        default:
            return true;
    }
}

export const project = {
    id: undefined,
    projectRole: "",
    currentProjectFlag: true,
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
    updateProjectFromResumeSDK: (state, action) => {
        updateField(state, action.payload.index, "projectRole", action.payload.data.proj_name);
        updateField(state, action.payload.index, "projectCompanyName", action.payload.data.proj_cpy);
        updateField(state, action.payload.index, "projectStartDate", action.payload.data.start_date);
        updateField(state, action.payload.index, "projectEndDate", action.payload.data.end_date);
        updateField(state, action.payload.index, "projectDescription", action.payload.data.proj_content);
        if (action.payload.data.end_date) {
            updateField(state, action.payload.index, "currentProjectFlag", true);
        }
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
    },
    removeProject: (state, action) => {
        const index = action.payload.index;
        if(state.project.data.length > 1) {
            state.project.data.splice(index, 1) 
        } else {
            state.project.data = [{
                ...project
            }]
        }        
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
