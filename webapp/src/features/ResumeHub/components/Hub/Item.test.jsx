import React from 'react';
import { mount } from 'enzyme';
import { Provider } from 'react-redux';
import { I8nContext } from 'shell/i18n';
import renderer from 'react-test-renderer';
import { Router } from 'react-router-dom';
import { createMemoryHistory } from 'history';

import 'react-dates/initialize';

import store from 'store';
import Item from './Item';

const ItemWithProvider = (props) => (
  <Provider store={store}>
    <I8nContext.Provider value={{}}>
      <Item {...props} />
    </I8nContext.Provider>
  </Provider>
);
jest.mock('moment', () => () => ({format: () => '2018–01–30T12:34:56+00:00'}));

describe('Item', () => {
  const showEditIcon = jest.fn();
  test('should render successfully with input', () => {
    const wrapper = mount(
      <Router history={createMemoryHistory()}>
        <ItemWithProvider
          resume={{
            alias: '123',
            resumeId: '123321',
            thumbnailUri: 'url',
            createdAt: '2020-11-11',
          }}
          messages={{}}
          index={0}
        />
      </Router>
    );
    console.log(wrapper.debug());
    expect(wrapper.find('Item').length).toEqual(1);
  });
  
  test('It should match snapshot', () => {
    const tree = renderer
      .create(
        <Router history={createMemoryHistory()}>
          <ItemWithProvider
            resume={{
              alias: '123',
              resumeId: '123321',
              thumbnailUri: 'url',
              createdAt: '2020-11-11',
            }}
            messages={{}}
            index={0}
          />
        </Router>
      )
      .toJSON();
    expect(tree).toMatchSnapshot();
  });
});
