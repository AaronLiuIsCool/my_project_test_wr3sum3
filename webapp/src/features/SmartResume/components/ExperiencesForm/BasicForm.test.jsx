import React from 'react';
import { mount } from 'enzyme';
import { Provider } from 'react-redux';
import { I8nContext } from 'shell/i18n';
import renderer from 'react-test-renderer';

import 'react-dates/initialize';

import store from 'store';
import BasicForm from './BasicForm';

jest.mock('./AvatarUpload', () => () => <div>Avatar</div>)

const BasicFormWithProvider = (props) => (
  <Provider store={store}>
    <I8nContext.Provider value={{}}>
      <BasicForm {...props} />
    </I8nContext.Provider>
  </Provider>
);

describe('BasicForm form', () => {
  test('should render successfully with input', () => {
    const wrapper = mount(
      <BasicFormWithProvider data={{}} messages={{}} index={0} />
    );
    expect(wrapper.find('Form').length).toEqual(1);
  });

  test('It should render <Summary />', () => {
    const data = {
      id: 'test',
    };
    const wrapper = mount(
      <BasicFormWithProvider
        data={data}
        messages={{}}
        index={0}
        completed={true}
      />
    );
    expect(wrapper.find('Summary').length).toEqual(1);
  });

  test('It should render <Summary /> after click', () => {
    const data = {};
    const wrapper = mount(
      <BasicFormWithProvider data={data} messages={{}} index={0} />
    );
    const btn = wrapper.find('.toggle-up-arrow');
    btn.simulate('click');
    expect(wrapper.find('Summary').length).toEqual(1);
  });

  test('It should match snapshot', () => {
    const data = {
      nameCn: '中文',
      nameEn: 'English',
      email: '123@asd.ca',
      phone: '1231231321',
      city: '北京',
      linkedin: 'http://www.google.ca',
      weblink: 'http://www.google.ca',
    };
    const tree = renderer
      .create(<BasicFormWithProvider data={data} messages={{}} index={0} />)
      .toJSON();
    expect(tree).toMatchSnapshot();
  });
});
