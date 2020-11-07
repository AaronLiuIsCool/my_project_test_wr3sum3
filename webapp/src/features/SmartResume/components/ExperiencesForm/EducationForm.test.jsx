import React from 'react';
import { mount } from 'enzyme';
import { Provider } from 'react-redux';
import { I8nContext } from 'shell/i18n';
import renderer from 'react-test-renderer';

import 'react-dates/initialize';

import store from 'store';
import EducationForm from './EducationForm';

const EducationFormWithProvider = (props) => (
  <Provider store={store}>
    <I8nContext.Provider value={{}}>
      <EducationForm {...props} />
    </I8nContext.Provider>
  </Provider>
);

describe('EducationForm form', () => {
  test('should render successfully with input', () => {
    const wrapper = mount(
      <EducationFormWithProvider data={{}} messages={{}} index={0} />
    );
    expect(wrapper.find('Form').length).toEqual(1);
  });

  test('It should render <Summary />', () => {
    const data = {
      id: 'test',
    };
    const wrapper = mount(
      <EducationFormWithProvider data={data} messages={{}} index={0} />
    );
    expect(wrapper.find('Summary').length).toEqual(1);
  });

  test('It should render <Summary /> after click', () => {
    const data = {};
    const wrapper = mount(
      <EducationFormWithProvider data={data} messages={{}} index={0} />
    );
    const btn = wrapper.find('.toggle-up-arrow');
    btn.simulate('click');
    expect(wrapper.find('Summary').length).toEqual(1);
  });

  test('It should match snapshot', () => {
    const data = {
      id: 'test',
      schoolName: 'asdasd',
      startDate: '2020-09-10',
      graduateDate: '2020-09-11',
      major: 'ASD',
      degree: 'dsa',
    };
    const tree = renderer
      .create(<EducationFormWithProvider data={data} messages={{}} index={0} />)
      .toJSON();
    expect(tree).toMatchSnapshot();
  });
});
