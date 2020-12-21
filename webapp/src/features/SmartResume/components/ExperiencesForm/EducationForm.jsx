import React, {useState, useEffect, useRef} from 'react';
import PropTypes from 'prop-types';
import {useDispatch, useSelector} from 'react-redux';
import {Row, Col, Form} from 'react-bootstrap';

import SingleDatePicker from 'components/SingleDatePicker';
import InputGroup from 'components/InputGroup';
import DropdownGroup from 'components/DropdownGroup';
import Button from 'react-bootstrap/Button';

import {adaptEducation} from '../../utils/servicesAdaptor';
import {actions, selectId} from '../../slicer';
import {
  validateEducation,
  validateEducationEntry,
} from '../../slicer/education';
import {updateStatus, updateAllStatus} from '../../slicer/common';
import ResumeServices from 'shell/services/ResumeServices';
import {getLogger} from 'shell/logger';

import ArrowUp from '../../assets/arrow-up.png'; 
import ArrowUpActive from '../../assets/arrow-up-active.png'; 

import degreeOptions from 'data/degree.json';
import univOptions from 'data/university.json';
import countryOptions from 'data/country.json';

import {previewResume} from '../ResumePreview/resumeBuilder';
import {
  dispatchUpdates,
  updateCityOptions,
  updateRating,
} from '../../utils/resume';

import {Summary} from '../Summary';

const logger = getLogger('EducationForm');
const resumeServices = new ResumeServices();
const fields = [
  'schoolName',
  'gpa',
  'startDate',
  'graduateDate',
  'major',
  'degree',
  'city',
  'country',
  'highestAward',
  'otherAward',
];
const EducationForm = ({data, index, isLast = false, messages, resumeLanguage}) => {
  const [showSummary, setShowSummary] = useState(false);
  const didMount = useRef(false);
  const resumeId = useSelector(selectId);
  const [validated, setValidated] = useState(false);
  const [cityOptions, setCityOptions] = useState([]);
  const [status, setStatus] = useState({
    schoolName: {},
    gpa: {},
    startDate: {},
    graduateDate: {},
    major: {},
    degree: {},
    city: {},
    country: {},
    highestAward: {},
    otherAward: {},
  });
  const dispatch = useDispatch();
  const toggleShowSummary = () => {
    setShowSummary(!showSummary);
  };
  const save = async () => {
    previewResume(resumeLanguage);
    let id = data.id;
    try {
      const responseJson =
        id === undefined
          ? await resumeServices.createEducation(resumeId, adaptEducation(data))
          : await resumeServices.updateEducation(data.id, adaptEducation(data));
      id = id || responseJson.id;
    } catch (exception) {
      logger.error(exception);
    } finally {
      dispatch(actions.updateEducationId({index, id}));
      dispatchUpdates('update-score');
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    event.stopPropagation();
    updateAllStatus(validateEducationEntry, status, setStatus, fields, data);
    if (!validateEducation(data)) {
      setValidated(false);
      return;
    }
    setValidated(true);
    dispatch(actions.completeEducation());
    await save();
    updateRating();
  };

  const handleSchoolChange = (values) => {
    const value = values.length === 0 ? null : values[0].data;
    updateStatus(
      validateEducationEntry,
      status,
      setStatus,
      'schoolName',
      value,
    );
    dispatch(actions.updateSchoolName({value, index}));
  };

  const handleGPAChange = (event) => {
    const value = event.target.value;
    updateStatus(validateEducationEntry, status, setStatus, 'gpa', value);
    dispatch(actions.updateGPA({value, index}));
  };

  const handleSchoolStartDateChange = (date) => {
    const value = date ? date.toISOString() : undefined;
    updateStatus(validateEducationEntry, status, setStatus, 'startDate', value);
    dispatch(actions.updateStartDate({value, index}));
  };

  const handleGradDateChange = (date) => {
    const value = date ? date.toISOString() : undefined;
    updateStatus(
      validateEducationEntry,
      status,
      setStatus,
      'graduateDate',
      value,
    );
    dispatch(actions.updateGraduateDate({value, index}));
  };

  const handleMajorChange = (event) => {
    const value = event.target.value;
    updateStatus(validateEducationEntry, status, setStatus, 'major', value);
    dispatch(actions.updateMajor({value, index}));
  };

  const handleDegreeChange = (values) => {
    const value = values.length === 0 ? null : values[0].data;
    updateStatus(validateEducationEntry, status, setStatus, 'degree', value);
    dispatch(actions.updateDegree({value, index}));
  };

  const handleCityChange = (values) => {
    const value = values.length === 0 ? null : values[0].data;
    updateStatus(validateEducationEntry, status, setStatus, 'city', value);
    dispatch(actions.updateEduCity({value, index}));
  };

  const handleCountryChange = (values) => {
    const value = values.length === 0 ? null : values[0].data;
    updateCityOptions(value, setCityOptions);
    updateStatus(validateEducationEntry, status, setStatus, 'country', value);
    dispatch(actions.updateEduCountry({value, index}));
  };

  const handleDelete = async (id) => {
    dispatch(actions.removeEducation({index}));
    if (id) {
      await resumeServices.removeEducation(id, resumeId);
      updateRating();
    }
  };
  useEffect(() => {
    if (data.id && !didMount.current) {
      setShowSummary(true);
      didMount.current = true;
    } else if (!data.id && didMount.current) {
      setShowSummary(false);
    }
  }, [data]);
  useEffect(() => {
    updateCityOptions(data.country, setCityOptions);
  }, [data.country]);
  return (
    <div className="form_body">
      {showSummary ? (
        <Summary
          type={'EducationInfo'}
          handleClickCallback={toggleShowSummary}
          schoolName={data.schoolName}
          enterSchoolDate={data.startDate}
          graduateDate={data.graduateDate}
          major={data.major}
          degree={data.degree}
        />
      ) : (
        <Form
          validated={validated}
          onSubmit={handleSubmit}
          className={`edu-${index}`}
        >
          <Row className="flexie">
            <Col>
              <h2 className="form_h2">{messages.enterSchoolInfo}</h2>
            </Col>
            <div className="toggle-up-arrow" onClick={toggleShowSummary}>
              {data.id && (
                <><img className="hide-on-hover" src={ArrowUp} alt="up-arrow"/><img className="display-on-hover" src={ArrowUpActive} alt="up-arrow-active"/></>
              )}
            </div>
          </Row>
          <Row>
            <Col lg="4">
              <DropdownGroup
                label={messages.schoolName}
                id="education-school"
                placeholder={messages.enterSchoolName}
                options={univOptions}
                value={data.schoolName}
                onChange={handleSchoolChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.schoolName.isValid}
                isInvalid={status.schoolName.isInvalid}
              />
            </Col>
            <Col lg="2">
              <InputGroup
                label={messages.graduateGPA}
                id="education-gpa"
                placeholder="4.0?"
                value={data.gpa}
                onChange={handleGPAChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.gpa.isValid}
                isInvalid={status.gpa.isInvalid}
              />
            </Col>
            <Col>
              <SingleDatePicker
                label={messages.enterSchoolDate}
                id="education-enter-date"
                placeholder={messages.yymmdd}
                value={data.startDate}
                
                displayFormat={messages.dateDisplayFormat}
                onDateChange={handleSchoolStartDateChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.startDate.isValid}
                isInvalid={status.startDate.isInvalid}
              />
            </Col>
            <Col>
              <SingleDatePicker
                label={messages.graduateDate}
                id="education-graduate-date"
                placeholder={messages.yymmdd}
                value={data.graduateDate}
                
                displayFormat={messages.dateDisplayFormat}
                onDateChange={handleGradDateChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.graduateDate.isValid}
                isInvalid={status.graduateDate.isInvalid}
              />
            </Col>
          </Row>

          <Row>
            <Col>
              <InputGroup
                label={messages.major}
                id="education-major"
                placeholder={messages.enterMajor}
                value={data.major}
                onChange={handleMajorChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.major.isValid}
                isInvalid={status.major.isInvalid}
              />
            </Col>
            <Col>
              <DropdownGroup
                label={messages.degree}
                id="education-degree"
                placeholder={messages.enterDegree}
                options={degreeOptions}
                value={data.degree}
                onChange={handleDegreeChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.degree.isValid}
                isInvalid={status.degree.isInvalid}
              />
            </Col>
            <Col>
              <DropdownGroup
                label={messages.schoolCountry}
                id="education-school-country"
                placeholder={messages.schoolCountry}
                options={countryOptions}
                value={data.country}
                onChange={handleCountryChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.country.isValid}
                isInvalid={status.country.isInvalid}
              />
            </Col>
            <Col>
              <DropdownGroup
                label={messages.schoolCity}
                id="education-school-city"
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
                label={messages.highestAward}
                id="education-highest-award"
                placeholder={messages.enterHighestAward}
                value={data.highestAward}
                onChange={(event) =>
                  dispatch(
                    actions.updateHighestAward({
                      value: event.target.value,
                      index,
                    }),
                  )
                }
                isValid={status.highestAward.isValid}
                isInvalid={status.highestAward.isInvalid}
              />
            </Col>
            <Col>
              <InputGroup
                label={messages.otherAward}
                id="education-other-award"
                placeholder={messages.enterOtherAward}
                value={data.otherAward}
                onChange={(event) =>
                  dispatch(
                    actions.updateOtherAward({
                      value: event.target.value,
                      index,
                    }),
                  )
                }
                isValid={status.otherAward.isValid}
                isInvalid={status.otherAward.isInvalid}
              />
            </Col>
          </Row>
          <Row className="form_buttons">
            <Col className="flex-end">
              {/* just a placeholder so we do need to change the css */}
              {
                <Button
                  onClick={() => {
                    handleDelete(data.id);
                  }}
                  variant="light"
                  className="remove-btn"
                >
                  {messages.delete}
                </Button>
              }
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

EducationForm.propTypes = {
  data: PropTypes.object.isRequired,
  index: PropTypes.number.isRequired,
  isLast: PropTypes.bool,
  messages: PropTypes.object.isRequired,
};

export default EducationForm;
