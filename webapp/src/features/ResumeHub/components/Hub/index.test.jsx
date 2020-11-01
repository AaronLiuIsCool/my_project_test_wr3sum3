import React from 'react';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import renderer from 'react-test-renderer';

import store from 'store';
import { I8nContext } from 'shell/i18n';

import Hub from './index';

const mockData = [{
    "resumeId":"4028b401755f29230175614025980000",
    "alias":"resume 1 for test",
    "thumbnailUri":"thumbnailUri",
    "createdAt":"2020-10-25T19:33:16Z"
}, {
    "resumeId":"4028b401755f292301756311ef930004",
    "alias":"test 2",
    "thumbnailUri":"thumbnailUri",
    "createdAt":"2020-10-26T04:02:01Z"
}];

describe('Hub', () => {
    let dateSpy;
    beforeEach(() => {
        const dateMock = new Date();
        dateMock.toLocaleString = jest.fn(() => 'test locale date string');
        dateSpy = jest.spyOn(global, 'Date')
                      .mockImplementation(() => dateMock);
        dateSpy.now = jest.fn(() => 5454684654564);
    });

    afterEach(() => {
        dateSpy.mockRestore();
    });

    test('renders Hub', () => {
        const hub = renderer.create(
            <BrowserRouter>
                <Provider store={store}>
                    <I8nContext.Provider value={{}}>
                        <Hub resumes={mockData} />
                    </I8nContext.Provider>
                </Provider>
            </BrowserRouter>
        ).toJSON();
    
        expect(hub).toMatchSnapshot();
    });    
});
