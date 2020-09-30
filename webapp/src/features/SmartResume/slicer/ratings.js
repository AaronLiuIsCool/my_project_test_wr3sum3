const ratings = {
  score: 0,
  basicInfo: [],
  layoutInfo: [],
  educationInfo: [],
  workInfo: {
    company: [],
    keywords: [],
    quantify: [],
    amount: [],
    sorted: [],
  },
  projectInfo: {
    company: [],
    keywords: [],
    quantify: [],
    amount: [],
    sorted: [],
  },
  otherInfo: {
    company: [],
    keywords: [],
    quantify: [],
    amount: [],
    sorted: [],
  },
  certInfo: [],
};
const initialState = {
  data: {
    ...ratings,
  },
};
function updateField(state, field, value) {
  state.ratings.data[field] = value;
}
function updateFieldByIndex(state, field, idx, value) {
  state.ratings.data[field][idx] = value;
}
function updateExpFields(state, field, subfield, value) {
  state.ratings.data[field][subfield] = value;
}
const reducers = {
  updateBasicInfoRating: (state, action) => {
    updateField(state, "basicInfo", action.payload);
    // console.log(JSON.stringify(action, undefined, 2));
    // console.log(JSON.stringify(state, undefined, 2));
  },
  updateLayoutRating: (state, action) => {
    updateField(state, "layoutInfo", action.payload);
  },

  updateEducationRating: (state, action) => {
    const { index, details } = action.payload;
    updateFieldByIndex(state, "educationInfo", index, details);
  },

  updateCertificateRating: (state, action) => {
    const { details } = action.payload;
    updateField(state, "certInfo", details);
  },

  updateWorkRating: (state, action) => {
    const payload = action.payload;
    for(const key in payload) {
        updateExpFields(state, "workInfo", key, payload[key]);
    }
    // const { field, data } = action.payload;
    // updateExpFields(state, "workInfo", field, data);
  },

  updateProjectRating: (state, action) => {
    const payload = action.payload;
    for(const key in payload) {
        updateExpFields(state, "projectInfo", key, payload[key]);
    }
    // const { field, data } = action.payload;
    // updateExpFields(state, "projectInfo", field, data);
  },
  updateVolunteerRating: (state, action) => {
    const payload = action.payload;
    for(const key in payload) {
        updateExpFields(state, "otherInfo", key, payload[key]);
    }
  },
};
const selectors = {
  selectRatings: ({ resume }) => resume.ratings,
};

export default {
  initialState,
  reducers,
  selectors,
};
