import React from 'react';
import { mount } from 'enzyme';
import { Provider } from 'react-redux';
import { I8nContext } from 'shell/i18n';
import renderer from 'react-test-renderer';

import 'react-dates/initialize';

import store from 'store';
import VolunteerForm from './VolunteerForm';
jest.mock('../../../../components/DraftEditor/index', () => () => <div>DraftEditor</div>)
const VolunteerFormWithProvider = (props) => (
  <Provider store={store}>
    <I8nContext.Provider value={{}}>
      <VolunteerForm {...props} />
    </I8nContext.Provider>
  </Provider>
);

describe('Volunteer Form', () => {
  test('should render successfully with input', () => {
    const wrapper = mount(
      <VolunteerFormWithProvider data={{}} messages={{}} index={0} />
    );
    // console.log(wrapper.debug());
    expect(wrapper.find('Form').length).toEqual(1);
  });

  test('It should render <Summary />', () => {
    const data = {
      id: 'test',
    };
    const wrapper = mount(
      <VolunteerFormWithProvider data={data} messages={{}} index={0} />
    );
    expect(wrapper.find('Summary').length).toEqual(1);
  });

  test('It should render <Summary /> after click', () => {
    const data = {};
    const wrapper = mount(
      <VolunteerFormWithProvider data={data} messages={{}} index={0} />
    );
    const btn = wrapper.find('.toggle-up-arrow');
    btn.simulate('click');
    expect(wrapper.find('Summary').length).toEqual(1);
  });

  test('It should match snapshot', () => {
    const data = {
      roleName: 'asdasd',
      startDate: '2020-10-01',
      endDate: '2020-10-02',
      companyName: 'ASD',
      currentFlag: false,
    };
    const tree = renderer
      .create(
        <VolunteerFormWithProvider data={data} messages={{}} index={0} />
      )
      .toJSON();
    expect(tree).toMatchSnapshot();
  });
  
});
