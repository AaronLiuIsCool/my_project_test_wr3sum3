import moment from 'moment';

import { validateDate, validateString, validateNonEmptyString } from './validator';

describe('test validators', () => {
  test('it should validate dates', () => {
    expect(validateDate()).toBe.false;
    expect(validateDate('')).toBe.false;
    expect(validateDate(123)).toBe.false;
    expect(validateDate({})).toBe.false;
    expect(validateDate(null)).toBe.false;

    expect(validateDate('2020-02-02')).toBe.true;
    expect(validateDate(new Date())).toBe.true;
    expect(validateDate(new Date().toISOString())).toBe.true;
    expect(validateDate(moment())).toBe.true;
    expect(validateDate(moment().toISOString())).toBe.true;
  });

  test('it should validate validate', () => {
    expect(validateString()).toBe.false;
    expect(validateString(null)).toBe.false;

    expect(validateString('')).toBe.true;
    expect(validateString('test')).toBe.true;
  });

  test('it should validate strings that are not empty', () => {
    expect(validateNonEmptyString()).toBe.false;
    expect(validateNonEmptyString(null)).toBe.false;
    expect(validateNonEmptyString('')).toBe.false;

    expect(validateNonEmptyString('test')).toBe.true;
  });
})
