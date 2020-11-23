import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';
import { Row, Col, Form } from 'react-bootstrap';

import InputGroup from 'components/InputGroup';
import DropdownGroup from 'components/DropdownGroup';
import Button from 'react-bootstrap/Button';

import { actions, selectId } from '../../slicer';
import AvatarUpload from './AvatarUpload';

import { validateBasic, validateBasicEntry } from '../../slicer/basic';
import { updateStatus, updateAllStatus } from '../../slicer/common';
import { adaptBasics } from '../../utils/servicesAdaptor';
import { dispatchUpdates, updateRating, updateCityOptions } from '../../utils/resume';
import ResumeServices from 'shell/services/ResumeServices';
import { getLogger } from 'shell/logger';

import { previewResume } from '../ResumePreview/resumeBuilder';

// json data for dropdowns
import countryOptions from 'data/country.json';
import { Summary } from '../Summary';
import { useEffect } from 'react';
import ArrowUp from '../../assets/arrow-up.svg'; 

const logger = getLogger('BasicForm');
const resumeServices = new ResumeServices();
const fields = [
    'nameCn',
    'email',
    'phone',
    'city',
    'linkedin',
    'weblink',
    'country'
];

const BasicForm = ({ data, photoReference, completed, messages }) => {
	const [showSummary, setShowSummary] = useState(false);
	const resumeId = useSelector(selectId);
  const [validated, setValidated] = useState(false);
  const [cityOptions, setCityOptions] = useState([]);
	const [status, setStatus] = useState({
		nameCn: {},
		email: {},
		phone: {},
		city: {},
		linkedin: {},
    weblink: {},
    country: {}
	});
	useEffect(() => {
    setShowSummary(Boolean(completed));
  }, [completed])
  useEffect(() => {
    updateCityOptions(data.country, setCityOptions);
  }, [data.country]);
	const dispatch = useDispatch();
  const toggleShowSummary = () => {
    setShowSummary(!showSummary);
  };
	const save = async () => {
		previewResume(messages.RPreview);
		let id = data.id;
		try {
			const responseJson =  data.id === undefined ? 
				await resumeServices.createBasics(resumeId, adaptBasics({ completed: true, data })) :
				await resumeServices.updateBasics(resumeId, adaptBasics({ completed: true, data }));
			id = id || responseJson.id;
		} catch (exception) {
			logger.error(exception);
		} finally {
			dispatch(actions.updateBasicsId({ id }));
			dispatchUpdates('update-score');
		}
	};

	const handleSubmit = async (event) => {
    event.preventDefault();
    event.stopPropagation();
    updateAllStatus(validateBasicEntry, status, setStatus, fields, data);
    if (!validateBasic(data)) {
      console.log(111111)
      setValidated(false);
      return;
    }
    toggleShowSummary();
    setValidated(true);
    dispatch(actions.completeBasic());
    
    await save();
    updateRating();
  };

	const handleNameCnChange = (event) => {
		const value = event.target.value;
		updateStatus(validateBasicEntry, status, setStatus, 'nameCn', value);
		dispatch(actions.updateNameCn({ value }));
	};

	const handleEmailChange = (event) => {
		const value = event.target.value;
		updateStatus(validateBasicEntry, status, setStatus, 'email', value);
		dispatch(actions.updateEmail({ value }));
	};

	const handlePhoneChange = (event) => {
		const value = event.target.value;
		updateStatus(validateBasicEntry, status, setStatus, 'phone', value);
		dispatch(actions.updatePhone({ value }));
  };
  
  const handleCountryChange = (values) => {
    const value = values.length === 0 ? null : values[0].data;
    updateCityOptions(value, setCityOptions);
    updateStatus(validateBasicEntry, status, setStatus, 'country', value);
    dispatch(actions.updateCountry({ value }));
  }

	const handleCityChange = (values) => {
		const value = values.length === 0 ? null : values[0].data;
		if (!validateBasicEntry('city', value)) {
			return;
		}
		updateStatus(validateBasicEntry, status, setStatus, 'city', value);
		dispatch(actions.updateCity({ value }));
	};

	const handleLinkedinChange = (event) => {
		const value = event.target.value;
		updateStatus(validateBasicEntry, status, setStatus, 'linkedin', value);

		dispatch(actions.updateLinkedin({ value }));
	};

	const handleWeblinkChange = (event) => {
		const value = event.target.value;
		updateStatus(validateBasicEntry, status, setStatus, 'weblink', value);
		dispatch(actions.updateWeblink({ value }));
	};
	return (
    <div className="form_body">
      {showSummary ? (
        <div>
          <Summary
            name={data.nameCn}
            avatar={photoReference?.url}
            type={'BasicInfo'}
            handleClickCallback={toggleShowSummary}
          />
        </div>
      ) : (
        <Form validated={validated} onSubmit={handleSubmit}>
          <Row className="flexie">
            <AvatarUpload photoReference={photoReference} />
            <div className="toggle-up-arrow" onClick={toggleShowSummary}>
              {completed && <img src={ArrowUp} alt="up-arrow"/>}
            </div>
          </Row>
          <Row>
            <Col>
              <InputGroup
                label={messages.cnName}
                id="basic-name-cn"
                placeholder={messages.enterCnName}
                value={data.nameCn}
                onChange={handleNameCnChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.nameCn.isValid}
                isInvalid={status.nameCn.isInvalid}
              />
            </Col>
            <Col lg="6">
              <InputGroup
                label={messages.email}
                id="basic-email"
                placeholder={messages.enterEmail}
                value={data.email}
                onChange={handleEmailChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.email.isValid}
                isInvalid={status.email.isInvalid}
              />
            </Col>
          </Row>

          <Row>
            
            <Col lg="6">
              <InputGroup
                label={messages.phone}
                id="basic-phone"
                placeholder={messages.enterPhone}
                value={data.phone}
                onChange={handlePhoneChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.phone.isValid}
                isInvalid={status.phone.isInvalid}
              />
            </Col>
            <Col lg="3">
              <DropdownGroup
                label={messages.country}
                id="basic-country"
                placeholder={messages.country}
                options={countryOptions}
                value={data.country}
                onChange={handleCountryChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.country.isValid}
                isInvalid={status.country.isInvalid}
              />
            </Col>
            <Col lg="3">
              <DropdownGroup
                label={messages.schoolCity}
                id="education-city"
                placeholder={messages.schoolCity}
                options={cityOptions}
                value={data.city}
                onChange={handleCityChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.city.isValid}
                isInvalid={status.city.isInvalid}
              />
            </Col>
          </Row>
          <Row>
            <Col>
              <InputGroup
                label={messages.linkedin}
                id="basic-linkedin"
                placeholder={messages.enterLinkedin}
                value={data.linkedin}
                onChange={handleLinkedinChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.linkedin.isValid}
                isInvalid={status.linkedin.isInvalid}
              />
            </Col>
            <Col>
              <InputGroup
                label={messages.weblink}
                id="basic-weblink"
                placeholder={messages.enterWeblink}
                value={data.weblink}
                onChange={handleWeblinkChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.weblink.isValid}
                isInvalid={status.weblink.isInvalid}
              />
            </Col>
          </Row>
          {/* todo: reset button  */}
          <Row className="form_buttons">
            <Col className="space_betweens">
              {/* just a placeholder so we do need to change the css */}
              <p className="hidden"></p>
              <Button variant="primary" type="submit">
                {messages.save}
              </Button>
            </Col>
          </Row>
        </Form>
      )}
    </div>
  );
};

BasicForm.propTypes = {
	data: PropTypes.object.isRequired,
	completed: PropTypes.bool.isRequired,
	messages: PropTypes.object.isRequired,
};

export default BasicForm;
