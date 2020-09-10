import {flatten, reconstruct} from './resume';

const testObj = {
  a: 'ezio auditore',
  b: {
    c: 'nothing is true',
    d: {
      e: 'everything is permitted'
    }
  },
  f: {
    g: [
      'templars',
      'assassins',
      {
        h: 'bye'
      }
    ]
  }
};

const flattenTestObj = {
  'a': 'ezio auditore',
  'b.c': 'nothing is true',
  'b.d.e': 'everything is permitted',
  'f.g.0': 'templars',
  'f.g.1': 'assassins',
  'f.g.2.h': 'bye',
};



describe('test resume utils', () => {
  it('should reconstruct the resume object', () => {
    expect(flatten(null)).toEqual({});
    expect(flatten(123)).toEqual({});
    expect(flatten('')).toEqual({});
    expect(flatten()).toEqual({});
    expect(flatten([])).toEqual({});
    expect(flatten(testObj)).toEqual(flattenTestObj);
    expect(testObj.b.c).toEqual('nothing is true');
  });

  it('should flatten the resume object', () => {
    expect(reconstruct(flattenTestObj)).toEqual(testObj);
    expect(flattenTestObj['b.d.e']).toEqual('everything is permitted');
  })
});
