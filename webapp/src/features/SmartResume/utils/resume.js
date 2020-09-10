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
