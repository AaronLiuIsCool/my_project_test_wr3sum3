import {generateInitials} from './Actions';

describe('Actions test', () => {
    it('should generate initials correctly', () => {
        expect(generateInitials()).toEqual();
        expect(generateInitials(undefined, 'default')).toEqual('default');
        expect(generateInitials('TEST', 'default')).toEqual('TE');
        expect(generateInitials('test', 'default')).toEqual('TE');
        expect(generateInitials('test   ', 'default')).toEqual('TE');
        expect(generateInitials('FOO BAR', 'default')).toEqual('FB');
        expect(generateInitials('foo bar', 'default')).toEqual('FB');
        expect(generateInitials('T', 'default')).toEqual('T');
        expect(generateInitials('T S', 'default')).toEqual('TS');
        expect(generateInitials('Car Engineer 1', 'default')).toEqual('CE');
    });
});