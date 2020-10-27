import React from 'react';
import { mount } from 'enzyme';
import { Provider } from 'react-redux';
import { I8nContext } from 'shell/i18n';
import renderer from 'react-test-renderer';

import 'react-dates/initialize';

import store from 'store';
import CertificateForm from './CertificateForm';

const CertificateFormWithProvider = (props) => (
  <Provider store={store}>
    <I8nContext.Provider value={{}}>
      <CertificateForm {...props} />
    </I8nContext.Provider>
  </Provider>
);

describe('Certificate form', () => {
  test('should render successfully with input', () => {
    const wrapper = mount(
      <CertificateFormWithProvider data={{}} messages={{}} index={0} />
    );
    // console.log(wrapper.debug());
    expect(wrapper.find('Form').length).toEqual(1);
  });

  test('It should render <Summary />', () => {
    const data = {
      id: 'test',
    };
    const wrapper = mount(
      <CertificateFormWithProvider data={data} messages={{}} index={0} />
    );
    expect(wrapper.find('Summary').length).toEqual(1);
  });

  test('It should render <Summary /> after click', () => {
    const data = {};
    const wrapper = mount(
      <CertificateFormWithProvider data={data} messages={{}} index={0} />
    );
    const btn = wrapper.find('.toggle-up-arrow');
    btn.simulate('click');
    expect(wrapper.find('Summary').length).toEqual(1);
  });

  test('It should match snapshot', () => {
    const data = {
      id: 'test',
      certificateName: 'test',
      certificateIssuedDate: '2020-10-01',
      certificateEndDate: '2020-10-02',
      validCertificateFlag: false,
    };
    const tree = renderer
      .create(
        <CertificateFormWithProvider data={data} messages={{}} index={0} />
      )
      .toJSON();
    expect(tree).toMatchSnapshot();
  });
});
