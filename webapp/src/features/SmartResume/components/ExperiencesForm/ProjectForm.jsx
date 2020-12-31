import React, {useState, useEffect, useRef} from 'react';
import {Summary} from '../Summary';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import {useDispatch, useSelector} from 'react-redux';
import {Row, Col, Form} from 'react-bootstrap';

import ArrowUp from '../../assets/arrow-up.png'; 
import ArrowUpActive from '../../assets/arrow-up-active.png'; 

import SingleDatePicker from 'components/SingleDatePicker';
import InputGroup from 'components/InputGroup';
import DropdownGroup from 'components/DropdownGroup';
import RadioButtonGroup from 'components/RadioButtonGroup';
import Button from 'react-bootstrap/Button';
import DraftEditor from '../../../../components/DraftEditor/index';
import {
  dispatchUpdates,
  updateCityOptions,
  updateRating,
} from '../../utils/resume';

import {ReactComponent as WrittenAssistIcon} from '../../assets/writing_assit.svg';

import {adaptProject} from '../../utils/servicesAdaptor';
import {actions, selectId, assistantSelectors} from '../../slicer';
import {validateProject, validateProjectEntry} from '../../slicer/project';
import {updateStatus, updateAllStatus} from '../../slicer/common';
import ResumeServices from 'shell/services/ResumeServices';
import {getLogger} from 'shell/logger';
import {previewResume} from '../ResumePreview/resumeBuilder';

import countryOptions from 'data/country.json';

const logger = getLogger('ProjectForm');
const resumeServices = new ResumeServices();
const fields = [
  'projectRole',
  'currentProjectFlag',
  'projectCompanyName',
  'projectStartDate',
  'projectEndDate',
  'projectDescription',
  'projectCity',
  'projectCountry',
];
const ProjectForm = ({data, index, isLast = false, messages, projectData, resumeLanguage}) => {
  const trigger = useSelector(assistantSelectors.selectTrigger);
  const showAssistant = useSelector(assistantSelectors.selectShow);
  const resumeId = useSelector(selectId);
  const [validated, setValidated] = useState(false);
  const [status, setStatus] = useState({
    projectRole: {},
    currentProjectFlag: {},
    projectCompanyName: {},
    projectStartDate: {},
    projectEndDate: {},
    projectDescription: {},
    projectCity: {},
    projectCountry: {},
  });
  const didMount = useRef(false);
  const [showSummary, setShowSummary] = useState(false);
  const dispatch = useDispatch();
  const toggleShowSummary = () => {
    setShowSummary(!showSummary);
  };
  const [cityOptions, setCityOptions] = useState([]);
  const save = async () => {
    previewResume(resumeLanguage);
    let id = data.id;
    try {
      const responseJson =
        id === undefined
          ? await resumeServices.createProject(resumeId, adaptProject(data))
          : await resumeServices.updateProject(data.id, adaptProject(data));
      id = id || responseJson.id;
    } catch (exception) {
      logger.error(exception);
    } finally {
      dispatch(actions.updateProjectId({index, id}));
      dispatchUpdates('update-score');
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    event.stopPropagation();
    updateAllStatus(validateProjectEntry, status, setStatus, fields, data);
    if (!validateProject(data)) {
      setValidated(false);
      return;
    }
    setValidated(true);
    dispatch(actions.completeProject());
    await save();
    updateRating();
  };

  const handleProjectRoleChange = (event) => {
    const value = event.target.value;
    updateStatus(validateProjectEntry, status, setStatus, 'projectRole', value);
    dispatch(actions.updateProjectRole({value, index}));
  };

  const handleCurrentProjectFlagChange = (event) => {
    event.preventDefault();
    const value = event.target.value;
    updateStatus(
      validateProjectEntry,
      status,
      setStatus,
      'currentProjectFlag',
      value,
    );
    dispatch(actions.updateCurrentProjectFlag({value, index}));

    // reset the end date value if current project is true
    dispatch(actions.updateProjectEndDate({value: '', index}));
  };

  const handleProjectCompanyNameChange = (event) => {
    const value = event.target.value;
    updateStatus(
      validateProjectEntry,
      status,
      setStatus,
      'projectCompanyName',
      value,
    );
    dispatch(actions.updateProjectCompanyName({value, index}));
  };

  const handleProjectStartDateChange = (date) => {
    const value = date ? date.toISOString() : undefined;
    updateStatus(
      validateProjectEntry,
      status,
      setStatus,
      'projectStartDate',
      value,
    );
    dispatch(actions.updateProjectStartDate({value, index}));
  };

  const handleProjectEndDateChange = (date) => {
    const value = date ? date.toISOString() : undefined;
    updateStatus(
      validateProjectEntry,
      status,
      setStatus,
      'projectEndDate',
      value,
    );
    dispatch(actions.updateProjectEndDate({value, index}));
  };

  const handleProjectDescriptionEditorChange = (value) => {
    updateStatus(
      validateProjectEntry,
      status,
      setStatus,
      'projectDescription',
      value,
    );
    dispatch(actions.updateProjectDescription({value, index}));
  };

  const handleCityChange = (values) => {
    const value = values.length === 0 ? null : values[0].data;
    updateStatus(validateProjectEntry, status, setStatus, 'projectCity', value);
    dispatch(actions.updateProjectCity({value, index}));
  };

  const handleCountryChange = (values) => {
    const value = values.length === 0 ? null : values[0].data;
    updateCityOptions(value, setCityOptions);
    updateStatus(
      validateProjectEntry,
      status,
      setStatus,
      'projectCountry',
      value,
    );
    dispatch(actions.updateProjectCountry({value, index}));
  };
  const handleAssistantClick = () => {
    dispatch(
      actions.toggleAssistant({
        trigger: 'project',
        context: {index, ...data},
      }),
    );
  };

  const assistantContainerClassNames = classNames({
    writeAssistantContainer: true,
    active: showAssistant && trigger === 'project',
  });
  const handleDelete = async (id) => {
    dispatch(actions.removeProject({index}));
    if (id) {
      await resumeServices.removeProject(id, resumeId);
      updateRating();
      previewResume(resumeLanguage);
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
    updateCityOptions(data.projectCountry, setCityOptions);
  }, [data.projectCountry]);

  return (
    <div className="form_body">
      {showSummary ? (
        <Summary
          type={'ProjectInfo'}
          handleClickCallback={toggleShowSummary}
          roleName={data.projectRole}
          startDate={data.projectStartDate}
          endDate={data.projectEndDate}
          companyName={data.projectCompanyName}
          currentFlag={data.currentProjectFlag}
        />
      ) : (
        <Form validated={validated} onSubmit={handleSubmit}>
          <Row className="flexie">
            <Col>
              <h2 className="form_h2">{messages.enterNewExperience}</h2>
            </Col>
            <div className="toggle-up-arrow" onClick={toggleShowSummary}>
              {data.id && (
                <><img className="hide-on-hover" src={ArrowUp} alt="up-arrow"/><img className="display-on-hover" src={ArrowUpActive} alt="up-arrow-active"/></>
              )}
            </div>
          </Row>
          <Row>
            <Col lg="4">
              <InputGroup
                label={messages.participateRole}
                id="project-name"
                placeholder={messages.enterParticipateRole}
                value={data.projectRole}
                onChange={handleProjectRoleChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.projectRole.isValid}
                isInvalid={status.projectRole.isInvalid}
              />
            </Col>
            <Col lg="2">
              <RadioButtonGroup
                label={messages.endOrNot}
                id="project-currentProjectFlag"
                values={[
                  {label: messages.yes, value: true},
                  {label: messages.no, value: false},
                ]}
                value={data.currentProjectFlag}
                onClickHandler={handleCurrentProjectFlagChange}
              />
            </Col>
            <Col>
              <InputGroup
                label={messages.companyName}
                id="project-company"
                placeholder={messages.enterCompanyName}
                value={data.projectCompanyName}
                onChange={handleProjectCompanyNameChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.projectCompanyName.isValid}
                isInvalid={status.projectCompanyName.isInvalid}
              />
            </Col>
          </Row>
          <Row>
            <Col>
              <SingleDatePicker
                label={messages.projectStartDate}
                id="project-enter-date"
                placeholder={messages.yymmdd}
                value={data.projectStartDate}
                
                displayFormat={messages.dateDisplayFormat}
                onDateChange={handleProjectStartDateChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.projectStartDate.isValid}
                isInvalid={status.projectStartDate.isInvalid}
              />
            </Col>
            {data.currentProjectFlag && (
              <Col>
                <SingleDatePicker
                  label={messages.projectEndDate}
                  id="project-graduate-date"
                  placeholder={messages.yymmdd}
                  value={data.projectEndDate}
                  
                  displayFormat={messages.dateDisplayFormat}
                  onDateChange={handleProjectEndDateChange}
                  feedbackMessage={messages.entryIsInvalid}
                  isValid={status.projectEndDate.isValid}
                  isInvalid={status.projectEndDate.isInvalid}
                />
              </Col>
            )}
            <Col>
              <DropdownGroup
                label={messages.country}
                id="project-country"
                placeholder={messages.projectCountry}
                options={countryOptions}
                value={data.projectCountry}
                onChange={handleCountryChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.projectCountry.isValid}
                isInvalid={status.projectCountry.isInvalid}
              />
            </Col>
            <Col>
              <DropdownGroup
                label={messages.city}
                id="project-city"
                placeholder={messages.projectCity}
                options={cityOptions}
                value={data.projectCity}
                onChange={handleCityChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.projectCity.isValid}
                isInvalid={status.projectCity.isInvalid}
              />
            </Col>
          </Row>
          <Row>
            <Col lg="12">
              <div className={assistantContainerClassNames}>
                <DraftEditor
                  feedbackMessage={messages.entryIsInvalid}
                  isValid={status.projectDescription.isValid}
                  isInvalid={status.projectDescription.isInvalid}
                  label={messages.projectDetailsDescription}
                  handleChangeCallback={handleProjectDescriptionEditorChange}
                  texts={data.projectDescription}
                  eventName={`project-${index}`}
                />
                <span className="writeAssistant">
                  <WrittenAssistIcon />
                  <Button variant="link" onClick={handleAssistantClick}>
                    {messages.writeAssistant}
                  </Button>
                </span>
              </div>
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

ProjectForm.propTypes = {
  data: PropTypes.object.isRequired,
  index: PropTypes.number.isRequired,
  isLast: PropTypes.bool,
  messages: PropTypes.object.isRequired,
};

export default ProjectForm;
