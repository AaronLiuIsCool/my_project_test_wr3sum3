import moment from 'moment';

export function validateDate(date) {
  if (date==="") return true; //allow date validation to pass on empty date
  return moment(date).isValid();
}

export function validateString(str) {
  return typeof str === 'string';
}

export function validateNonEmptyString(str) {
  return validateString(str) && (str.length > 0);
}
