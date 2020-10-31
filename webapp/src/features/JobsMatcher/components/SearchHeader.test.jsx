import React from 'react';
import { Provider } from 'react-redux';
import renderer from 'react-test-renderer';

import store from 'store';
import { I8nContext } from 'shell/i18n';

import SearchHeader from './SearchHeader';

const SearchHeaderWithProvider = (props) => (
    <Provider store={store}>
        <I8nContext.Provider value={{
            'search_query_placeholder': 'query',
            'search_country_label': 'country',
            'search_city_label': 'city'
        }}>
            <SearchHeader {...props} />
        </I8nContext.Provider>
    </Provider>
);

describe('SearchHeader', () => {
    test('render SearchHeader', async () => {
        const searchHeader = renderer.create(
            <SearchHeaderWithProvider />
        );
        expect(searchHeader.toJSON()).toMatchSnapshot();
    });
});
