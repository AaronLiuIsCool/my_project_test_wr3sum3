import {
    validateString,
    validateNonEmptyString
} from 'utils/validator';
import { detectChangesForSingleItem } from "./common";

export function anyBasicChanges(basicData) {
    return detectChangesForSingleItem(basicData, basic);
}


export function validateBasic(data) {
    return Object.keys(data).every(key => validateBasicEntry(key, data[key]));
}

const validateEmail = (email) => {
    const re = /\S+@\S+\.\S+/;
    return re.test(String(email).toLowerCase());
}

const validatePhone = (input) => {
    const re = /^\d{8,11}$/;
    return re.test(input);
}

const validateURL = (input) => {
    if (validateString(input) && !validateNonEmptyString(input)) {
      return true;
    }
    const re = /[(http(s)?)://(www.)?a-zA-Z0-9@:%._+~#=]{2,256}\.[a-z]{2,6}\b([-a-zA-Z0-9@:%_+.~#?&//=]*)/ig
    return re.test(input);
}

export function validateBasicEntry(name, value) {
    switch (name) {
        case 'nameEn':
        case 'nameCn':
        case 'city':
            if (validateNonEmptyString(value)) {
                return true;
            } else if (validateString(value)) {
                return false;
            } else {
                return false;
            }
        case 'email':
            if (validateEmail(value)) {
                return true;
            } else {
                return false;
            }
        case 'phone':
            if (validatePhone(value)) {
                return true;
            } else {
                return false;
            }
        case 'linkedin':
        case 'weblink':
            if (validateURL(value)) {
                return true;
            } else {
                return false;
            }
        default:
            return true;
    }
}

export const basic = {
    id: undefined,
    avatar: "",
    nameCn: "",
    nameEn: "",
    email: "",
    phone: "",
    city: "",
    linkedin: "",
    weblink: ""
}

const initialState = {
    completed: false,
    data: {
        ...basic
    }
};

function updateField(state, field, value) {
    state.basic.data[field] = value;
}

const reducers = {
    completeBasic: (state) => {
        state.basic.completed = true;
    },
    updateBasicFromResumeSDK: (state, action) => {
        updateField(state, "nameCn", action.payload.data.name);
        updateField(state, "email", action.payload.data.email);
        updateField(state, "phone", action.payload.data.phone);
        updateField(state, "city", action.payload.data.city);
        updateField(state, "weblink", action.payload.data.blog);
    },
    updateBasicsId: (state, action) => {
        updateField(state, "id", action.payload.id);
    },
    updateAvatar: (state, action) => {
        updateField(state, "avatar", action.payload.value);
    },
    updateNameCn: (state, action) => {
        updateField(state, "nameCn", action.payload.value);
    },
    updateNameEn: (state, action) => {
        updateField(state, "nameEn", action.payload.value);
    },
    updateEmail: (state, action) => {
        updateField(state, "email", action.payload.value);
    },
    updatePhone: (state, action) => {
        updateField(state, "phone", action.payload.value);
    },
    updateCity: (state, action) => {
        updateField(state, "city", action.payload.value);
    },
    updateLinkedin: (state, action) => {
        updateField(state, "linkedin", action.payload.value);
    },
    updateWeblink: (state, action) => {
        updateField(state, "weblink", action.payload.value);
    }
}

const selectors = {
    selectBasic: ({ resume }) => resume.basic
}

export default {
    initialState,
    reducers,
    selectors
};
