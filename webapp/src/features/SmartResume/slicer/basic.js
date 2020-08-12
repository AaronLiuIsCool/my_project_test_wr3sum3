import {
    validateString,
    validateNonEmptyString
} from 'utils/validator';

export function validateBasic(data) {
    return Object.keys(data).every(key => validateBasicEntry(key, data[key]));
}

const validateEmail = (email) => {
    const re = /\S+@\S+\.\S+/;
    return re.test(String(email).toLowerCase());
}

const validatePhone = (input) => {
    var re = /^\d{8,11}$/;
    return re.test(input);
}
const validateURL = (input) => {
    var re = /(https?:\/\/(?:[a-z0-9](?:[a-z0-9-]{0,61}[a-z0-9])?\.)+[a-z0-9][a-z0-9-]{0,61}[a-z0-9])(:?\d*)\/?([a-z_/0-9\-#.]*)\??([a-z_/0-9\-#=&]*)/g
    return re.test(input);
}

export function validateBasicEntry(name, value) {
    // todo: merge photo avatar
    switch (name) {
        case 'nameEn':
        case 'nameCn':
        case 'city':
            if (validateNonEmptyString(value)) {
                return true;
            } else if (validateString(value)) {
                return undefined;
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
    updateBasicsId: (state, action) => {
        updateField(state, "id", action.payload.id);
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
