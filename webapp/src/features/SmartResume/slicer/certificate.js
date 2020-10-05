import {
    validateDate,
    validateString,
    validateNonEmptyString
} from "utils/validator";

export function validateCertificate(data) {
    return Object.keys(data).every(key => validateCertificateEntry(key, data[key], data));
}

export function validateCertificateEntry(name, value, data) {
    switch (name) {
        case "certificateName":
        case "certificateCity":
        case "certificateCountry":
        case "certificateDescription":
            if (validateNonEmptyString(value)) {
                return true;
            } else if (validateString(value)) {
                return undefined;
            } else {
                return false;
            }
        case "currentCertificateFlag":
            return (
                value === true ||
                value === false ||
                toString.call(value) === "[object Boolean]"
            );
        case "certificateEndDate":
            if(!data?.currentCertificateFlag) {
                return true;
            }
            return typeof value === "string" && validateDate(value);
        case "certificateIssuedDate":
            return typeof value === "string" && validateDate(value);
        default:
            return true;
    }
}

export const certificate = {
    id: undefined,
    certificateName: "",
    validCertificateFlag: false,
    certificateIssuedDate: "",
    certificateEndDate: "",
};

const initialState = {
    completed: false,
    data: [{ ...certificate }]
};

function updateField(state, idx, field, value) {
    state.certificate.data[idx][field] = value;
}

const reducers = {
    completeCertificates: (state) => {
        state.certificate.completed = true;
    },
    updateCertificateFromResumeSDK: (state, action) => {
        updateField(state, action.payload.index, "certificateName", action.payload.data.train_cert);
        updateField(state, action.payload.index, "certificateIssuedDate", action.payload.data.start_date);
        updateField(state, action.payload.index, "certificateEndDate", action.payload.data.end_date);
        if (action.payload.data.end_date) {
            updateField(state, action.payload.index, "validCertificateFlag", true);
        }
    },
    addNewCertificate: state => {
        state.certificate.data.push({ ...certificate });
    },
    updateCertificateId: (state, action) => {
        updateField(state, action.payload.index, "id", action.payload.id);
    },
    updateCertificateName: (state, action) => {
        updateField(state, action.payload.index, "certificateName", action.payload.value);
    },
    updateCurrentCertificateFlag: (state, action) => {
        // convert string to boolean
        updateField(
            state,
            action.payload.index,
            "validCertificateFlag",
            action.payload.value === "true"
        );
    },
    updateCertificateIssuedDate: (state, action) => {
        updateField(
            state,
            action.payload.index,
            "certificateIssuedDate",
            action.payload.value
        );
    },
    updateCertificateEndDate: (state, action) => {
        updateField(
            state,
            action.payload.index,
            "certificateEndDate",
            action.payload.value
        );
    }
};

const selectors = {
    selectCertificate: ({ resume }) => resume.certificate
};

export default {
    initialState,
    reducers,
    selectors
};
