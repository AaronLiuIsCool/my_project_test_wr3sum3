import moment from 'moment';

export function validateDate(date) {
  return moment(date).isValid();
}

export function validateString(str) {
  return typeof str === 'string';
}

export function validateNonEmptyString(str) {
  return validateString(str) && (str.length > 0);
}
