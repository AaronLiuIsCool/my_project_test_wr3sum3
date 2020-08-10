import { validateDate, validateString, validateNonEmptyString } from 'utils/validator';

export function validateProject(data) {
  return Object.keys(data).every(key => validateProjectEntry(key, data[key]));
}

export function validateProjectEntry(name, value) {
  switch(name) {
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
    projectRole: "",
    currentProjectFlag: "false",
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
    {...project}
  ]
};


function updateField(state, idx, field, value) {
  state.project.data[idx][field] = value;
}

const reducers = {
  addNewProject: (state) => {
    state.project.data.push({...project});
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
}

const selectors = {
  selectProject : ({ resume }) => resume.project
}

export default {
  initialState,
  reducers,
  selectors
};
