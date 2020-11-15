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

export const generateBasicFormRating = ({ avatar, linkedin, weblink, messages }, completed) => {
    const basicArr = []
    
    if(!completed) {
      const obj = {
        section: 'basicInfo',
        name: messages.basicInfo,
        type: 'info-complete',
        message: messages.basicInfoNotCompleted,
        selector: '#basic-linkedin',
        noNav: true
      };
      basicArr.push(obj);
      return basicArr
    }
    
    if (linkedin === '') {
        const obj = {
            section: 'basicInfo',
            name: messages.linkedinAccount,
            type: 'info-complete',
            message: messages.linkedinMessage,
            selector: '#basic-linkedin'
        };
        basicArr.push(obj);
    }
    if (weblink === '') {
        const obj = {
            section: 'basicInfo',
            name: messages.personalWebsite,
            type: 'info-complete',
            message: messages.websiteMessage,
            selector: '#basic-weblink'
        };
        basicArr.push(obj);
    }
    if(avatar === '') {
        const obj = {
            section: 'basicInfo',
            name: messages.uploadPhoto,
            type: 'layout',
            message: messages.photoMessage,
            selector: '#upload-photo'
        }
        basicArr.push(obj)
        
    }
    return basicArr
}

export const generateLayoutRating = (result, messages) => {
    const layoutArr = []
    
  const { page, y } = result[result.length - 1];
  if (page === 1) {
  } else if (page > 1 && y > 39) {
    layoutArr.push({
      section: 'basicInfo',
      prefix: messages.resume,
      name: messages.notAlign,
      type: "layout",
      message: messages.layoutMessage,
      selector: undefined,
      noNav: true
    });
  }
  
  return layoutArr
  
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

export const generateSuggestions = (experience=[], section, eventType, sorted, messages) => {
    const keywordsArr = [];
    const quantifyArr = [];
    const companyArr = [];
    const sortedArr = [];
    const len = experience.length
    const expArr = [{
        name: section === 'workXp' ? messages.workExp : section === 'otherXp' ? messages.otherExp : messages.projectExp,
        type: 'exp-value',
        green: len > 1,
        message: prepareExpArrMessage(messages, section, len)
            // len > 2
            //     ? messages.expLengthOverTwo.replace('{exp}', messages.expReplacement[section])
            //     : len > 1
            //     ? messages.expLengthTwo.replace('{exp}', messages.expReplacement[section])
            //     : len > 0
            //     ? messages.expLengthOne.replace('{exp}', messages.expReplacement[section])
            //     : section === "projectXp" ?  messages.expLengthZeroProject : section === "workXp" ? messages.expLengthZero.replace('{exp}', messages.expReplacement[section]) : messages.expLengthZeroOther
    }];
    if(!sorted) {
       sortedArr.push({
           name: messages.sorted,
           type: 'layout',
           section,
           message: messages.sortedMessage
       }) 
    }
    experience.forEach((exp, index) => {
        const ratingDetail = exp.bullets || [];
        const position = exp.role;
        const companyName = exp.organization;
        const content = ratingDetail.map((item) => item.bullet);
        const keywords = ratingDetail.map((item) => item.keywords);
        const quantify = ratingDetail.map((item) => item.numericWords);
        if(exp.rating !== 'MEDIOCRE') {
            const companyObj = {
                type: 'exp-value',
                message: `${messages.employedAt1}${exp.orgnization}${messages.employedAt2}${
                    exp.rating === 'GREAT' ? messages.employedAt3 : messages.employedAt4
                }${messages.employedAt5}`,
                section,
                selector: '',
                name:
                    exp.rating === 'GREAT'
                        ? messages.nationalInfluence
                        : messages.globalInfluence
            };
            companyArr.push(companyObj);
        }
        
        
        const keywordsSuggestions =
            keywords.filter((e) => e.length > 0).length / keywords.length <
            0.5;
        const quantifySuggestions =
            quantify.filter((e) => e.length > 0).length / quantify.length <
            0.5;

        if (keywordsSuggestions) {
            keywordsArr.push({
                eventType,
                content,
                name: '',
                type: 'expression',
                section,
                company: companyName,
                position,
                index,
                keywords
            });
        } else {
        }
        if (quantifySuggestions) {
            quantifyArr.push({
                eventType,
                content,
                name: '',
                type: 'expression',
                section,
                position,
                company: companyName,
                index,
                quantify
            });
        } else {
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
      noNav: true
      // selector: `.edu-${index} #education-highest-award`,
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
    const gpaNum = gpa - 0;
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