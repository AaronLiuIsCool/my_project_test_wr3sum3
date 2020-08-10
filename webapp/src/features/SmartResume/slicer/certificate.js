import {
  validateDate,
  validateString,
  validateNonEmptyString
} from "utils/validator";

export function validateCertificate(data) {
  return Object.keys(data).every(key => validateCertificateEntry(key, data[key]));
}

export function validateCertificateEntry(name, value) {
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
    case "certificateIssuedDate":
    case "certificateEndDate":
      return typeof value === "string" && validateDate(value);
    default:
      return true;
  }
}

export const certificate = {
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
  addNewCertificate: state => {
    state.certificate.data.push({ ...certificate });
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
