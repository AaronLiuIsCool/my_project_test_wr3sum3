import { calculate } from './index';

describe('test JobsList', () => {
  test('should be able to calculate the correct paginations', () => {
    expect(calculate(0, 10)).toEqual({
      shouldShowPrev: false,
      shouldShowNext: true,
      min: 0,
      max: 3
    });

    expect(calculate(1, 10)).toEqual({
      shouldShowPrev: true,
      shouldShowNext: true,
      min: 0,
      max: 3
    });

    expect(calculate(2, 10)).toEqual({
      shouldShowPrev: true,
      shouldShowNext: true,
      min: 1,
      max: 4
    });

    expect(calculate(5, 10)).toEqual({
      shouldShowPrev: true,
      shouldShowNext: true,
      min: 4,
      max: 7
    });

    expect(calculate(8, 10)).toEqual({
      shouldShowPrev: true,
      shouldShowNext: true,
      min: 7,
      max: 10
    });

    expect(calculate(9, 10)).toEqual({
      shouldShowPrev: true,
      shouldShowNext: true,
      min: 7,
      max: 10
    });

    expect(calculate(10, 10)).toEqual({
      shouldShowPrev: true,
      shouldShowNext: false,
      min: 7,
      max: 10
    });
  });
});
