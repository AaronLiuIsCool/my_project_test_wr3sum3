import countryBasedCityOptions from 'data/countryBasedCityOptions.json';
function traverseAndFlatten(obj, flattenObj, parentKey) {
  if (typeof obj === 'object') {
    Object.keys(obj).forEach(key => {
      const newKey = parentKey === undefined ? key : `${parentKey}.${key}`;
      traverseAndFlatten(obj[key], flattenObj, newKey)
    });
  } else if (typeof obj === 'string') {
    flattenObj[parentKey] = obj;
  }
}

export function flatten(resume = {}) {
  if (typeof resume !== 'object' || resume === null) {
    return {};
  }
  const flattenResume = {};
  traverseAndFlatten(resume, flattenResume);
  return flattenResume;
}

function isNumeric(value) {
  return /^\d+$/.test(value);
}

function insert(resume, keys, index, value) {
  let key = keys[index];
  key = isNumeric(key) ? Number(key) : key;

  if (index + 1 === keys.length) {
    if (Array.isArray(resume)) {
      resume.push(value);
    } else {
      resume[key] = value;
    }
    return;
  }

  resume[key] = resume[key] || (isNumeric(keys[index + 1]) ? [] : {});
  insert(resume[key], keys, index + 1, value);
}

function traverseAndUnFlatten(flattenResume, resume) {
  Object.keys(flattenResume).forEach(key => {
    const value = flattenResume[key];
    insert(resume, key.split('.'), 0, value);
  });
}

export function reconstruct(flattenResume) {
  if (typeof flattenResume !== 'object' || flattenResume === null) {
    return {};
  }

  const resume = {};
  traverseAndUnFlatten(flattenResume, resume);
  return resume;
}

/**
 * @param
 */
export const getRatingBlocks = () => {
  
}

export const generateBasicFormRatingBlock = (name, type,  message, selector, noNav = false) => {
  return {
    section: 'basicInfo',
    name,
    type,
    message,
    selector,
    noNav,
  };
};

export const generateBasicFormRating = ({ avatar, linkedin, weblink, messages }, completed) => {
    const basicArr = []
    if (!completed) {
      basicArr.push(
        generateBasicFormRatingBlock(
          messages.basicInfo,
          'info-complete',
          messages.basicInfoNotCompleted,
          '#basic-linkedin',
          true,
        ),
      );
      return basicArr;
    }
    if (linkedin === '') {
      basicArr.push(
        generateBasicFormRatingBlock(
          messages.linkedinAccount,
          'info-complete',
          messages.linkedinMessage,
          '#basic-linkedin',
        ),
      );
    }
    if (weblink === '') {
      basicArr.push(
        generateBasicFormRatingBlock(
          messages.personalWebsite,
          'info-complete',
          messages.websiteMessage,
          '#basic-weblink',
        ),
      );
    }
    if(avatar === '') {
      basicArr.push(
        generateBasicFormRatingBlock(
          messages.uploadPhoto,
          'layout',
          messages.photoMessage,
          '#upload-photo',
        ),
      );
    }
    return basicArr
}

export const generateLayoutRating = (result, messages) => {
  const layoutArr = [];

  const {page, y} = result[result.length - 1];
  if (page === 1) {
  } else if (page > 1 && y > 39) {
    layoutArr.push({
      section: 'basicInfo',
      prefix: messages.resume,
      name: messages.notAlign,
      type: 'layout',
      message: messages.layoutMessage,
      selector: undefined,
      noNav: true,
    });
  }
  return layoutArr;
};

const prepareExpArrMessage = (messages, section, length) => {
  switch (length) {
    case 0:
      return messages[`${section}LengthZero`]
    case 1:
      return messages[`${section}LengthOne`]
    case 2:
      return messages[`${section}LengthTwo`]
    default:
      return messages[`${section}LengthOverTwo`]
  }
};

export const prepareKeywordsArr = () => {
  // Positive and Negative feedback generation
}

// helper function of generateSuggestions
export const prepareExpArr = (section, messages, expLength) => {
  return [
    {
      name:
        section === 'workXp'
          ? messages.workExp
          : section === 'otherXp'
          ? messages.otherExp
          : messages.projectExp,
      type: 'exp-value',
      green: expLength > 1,
      message: prepareExpArrMessage(messages, section, expLength),
    },
  ];
};

// helper function of generateSuggestions
export const prepareNotSortedArr = (section, messages) => {
  return {
    name: messages.sorted,
    type: 'layout',
    section,
    message: messages.sortedMessage,
  };
};

export const prepareCompanyArr = (section, rating, orgnization, messages) => {
  return {
    type: 'exp-value',
    message: `${messages.employedAt1}${orgnization}${messages.employedAt2}${
      rating === 'GREAT' ? messages.employedAt3 : messages.employedAt4
    }${messages.employedAt5}`,
    // TODO: Restructure this with new wording in the future ^
    section,
    selector: '',
    name:
      rating === 'GREAT'
        ? messages.nationalInfluence
        : messages.globalInfluence,
  };
};

export const prepareNegativeExpressionArr = (
  eventType,
  content,
  section,
  companyName,
  position,
  index,
  aspect,
  expression,
) => {
  return {
    eventType,
    content,
    name: '',
    type: 'expression',
    section,
    company: companyName,
    position,
    index,
    [aspect]: expression,
  };
};

const preparePositiveExpressionArr = (
  eventType,
  content,
  section,
  companyName,
  position,
  index,
  aspect,
  expression,
  messages,
) => {
  const feedback = expression.flat();
  return {
    eventType,
    content,
    name: '',
    type: 'expression',
    section,
    company: companyName,
    position,
    index,
    feedback,
    [aspect]: expression,
  }
}

export const generateSuggestions = (experience=[], section, eventType, sorted, messages) => {  
    const keywordsArr = [];
    const quantifyArr = [];
    const companyArr = [];
    const sortedArr = [];
    const expLength = experience.length;
    
    // generate message based on number of experiences user entered
    const expArr = prepareExpArr(section, messages, expLength);
    
    // generate sorted message, if any
    if (!sorted) {
      sortedArr.push(prepareNotSortedArr(section, messages));
    }
    
    experience.forEach((exp, index) => {
        const ratingDetail = exp.bullets || [];
        const position = exp.role;
        const companyName = exp.organization;
        const content = ratingDetail.map((item) => item.bullet);
        const keywords = ratingDetail.map((item) => item.keywords);
        const quantify = ratingDetail.map((item) => item.numericWords);
        
        if(exp.rating !== 'MEDIOCRE') {
            companyArr.push(
              prepareCompanyArr(section, exp.rating, exp.organization, messages)
            );
        }
        
        
        const keywordsSuggestions =
            keywords.filter((e) => e.length > 0).length / keywords.length <
            0.5;

        const quantifySuggestions =
            quantify.filter((e) => e.length > 0).length / quantify.length <
            0.5;

        if (keywordsSuggestions) {
          keywordsArr.push(
            prepareNegativeExpressionArr(
              eventType,
              content,
              section,
              companyName,
              position,
              index,
              'keywords',
              keywords,
            ),
          );
        } else {
          keywordsArr.push(
            preparePositiveExpressionArr(
              eventType,
              content,
              section,
              companyName,
              position,
              index,
              'keywords',
              keywords,
              messages,
            ),
          );
        }
        if (quantifySuggestions) {
          quantifyArr.push(
            prepareNegativeExpressionArr(
              eventType,
              content,
              section,
              companyName,
              position,
              index,
              'quantify',
              quantify,
            ),
          );
        } else {
          quantifyArr.push(
            preparePositiveExpressionArr(
              eventType,
              content,
              section,
              companyName,
              position,
              index,
              'quantify',
              quantify,
              messages,
            ),
          );
        }
    });
    
    return {
        companyArr,
        expArr,
        keywordsArr,
        quantifyArr,
        sortedArr
    };
};

export const generateEducationRatings = (educations, schools, messages, completed) => {
  const resArr = []
  if(!completed || schools.length < 1) {
    const obj = {
      section: "education",
      name: messages.education,
      type: "info-complete",
      message: messages.EduInfoNotCompleted,
      noNav: true,
      selector: '',
    };
    resArr.push(obj);
    return resArr
  }
  educations.forEach((education, index) => {
    resArr.push(generateEducationRating(
      {
        ...education,
        schools,
        index
      }, messages
    ))
  })
  
  return resArr
}


export const generateEducationRating = (
  { gpa, highestAward, otherAward, schools, index },
  messages
) => {
  const educationArr = [];
  if (!highestAward) {
    const obj = {
      section: "education",
      name: messages.highestAwards,
      type: "info-complete",
      message: messages.highestAwardsMessage,
      selector: `.edu-${index} #education-highest-award`,
    };
    educationArr.push(obj);
  }
  if (!otherAward) {
    const obj = {
      section: "education",
      name: messages.otherAward,
      type: "info-complete",
      message: messages.otherAwardMessage,
      selector: `.edu-${index} #education-other-award`,
    };
    educationArr.push(obj);
  }
  if (gpa !== "") {
    const gpaNum = Number(gpa);
    const gpaObj = {
      section: "education",
      name: messages.graduateGPA,
      type: "exp-value",
      message: messages.gpaMessageOne,
      selector: `.edu-${index} #education-gpa`,
      green: true,
    };
    if (gpaNum > 3.5) {
      educationArr.push(gpaObj);
    } else if (gpaNum < 3) {
      gpaObj.green = false;
      gpaObj.message = messages.gpaMessageTwo;
      educationArr.push(gpaObj);
    } else {
      gpaObj.message = messages.gpaMessageThree;
      educationArr.push(gpaObj);
    }
  }

  if (schools.length > index) {
    const res = prepareSchoolPopularity(schools[index], messages);
    if (res) {
      educationArr.push(res);
    }
  }
  return educationArr
};

const prepareSchoolPopularity = (school, messages) => {
  const rating = school.rating;
  const RATINGS_MAP = {
    'GREAT': messages.schoolRating1,
    'GOOD': messages.schoolRating2,
    'MEDIOCRE': messages.schoolRating3,
  };
  const str = `${messages.graduatedFrom.replace(/{school}/g, school.institution)}${RATINGS_MAP[rating]}`;
  const obj = {
    school: true,
    name: `${messages.schoolPopularity}${
      rating > 4
        ? messages.extremegood
        : rating > 2
        ? messages.verygood
        : messages.good
    }`,
    message: str,
    green: true,
    type: "exp-value",
  };
  return obj;
};

export const isDescending = (arr) => {
  return arr.every(function (x, i) {
    return i === 0 || x <= arr[i - 1];
  });
};

export const extractDate = (arr, label) => {
  return arr.map((item) => new Date(item[label]).getTime());
};

export const generateCertificeRating = (numberOfCertificate, messages) => {
  const certArr = [];

  if (numberOfCertificate < 2) {
    certArr.push({
      type: "exp-value",
      section: "certifications",
      message: messages.certificationsMessage,
      name: messages.certifications,
    });
  }

  return certArr;
};

export const dispatchUpdates = (type) => {
  const eventName = type;
  const event = new CustomEvent(eventName, {
      detail: {}
  });
  window.dispatchEvent(event);
};

const supportedCountries = {
  中国: "cn",
  加拿大: "ca",
  美国: "us",
  澳大利亚: "au",
  日本: "jp",
  英国: "uk",
  印度: "in",
  韩国: "kr",
};

export const updateCityOptions = (country, setter) => {
  if(supportedCountries[country]) {
    setter(countryBasedCityOptions[supportedCountries[country]])
  } else {
    setter(countryBasedCityOptions['cn'])
  }
}

export const updateRating = () => {
  const event = new CustomEvent('update-rating', {
    detail: {},
  });
  window.dispatchEvent(event);
};