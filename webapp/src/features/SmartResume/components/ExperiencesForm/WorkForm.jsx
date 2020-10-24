import React, { useState } from 'react';
import classNames from 'classnames';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';
import { Row, Col, Form, Button } from 'react-bootstrap';

import { GAEvent } from 'utils/GATracking';

import SingleDatePicker from 'components/SingleDatePicker';
import InputGroup from 'components/InputGroup';
import RadioButtonGroup from 'components/RadioButtonGroup';
import DropdownGroup from 'components/DropdownGroup';
import KButton from 'components/KButton';
import { ReactComponent as WrittenAssistIcon } from '../../assets/writing_assit.svg';

import { adaptWork } from '../../utils/servicesAdaptor';
import { actions, selectId, assistantSelectors } from '../../slicer';
import { validateWork, validateWorkEntry } from '../../slicer/work';
import { updateStatus, updateAllStatus } from '../../slicer/common';
import ResumeServices from 'shell/services/ResumeServices';
import { getLogger } from 'shell/logger';
import { previewResume, wholePageCheck } from '../ResumePreview/resumeBuilder';
import { generateSuggestions, isDescending, extractDate, generateLayoutRating, dispatchUpdates, updateCityOptions } from '../../utils/resume';

import DraftEditor from '../../../../components/DraftEditor/index'

import countryOptions from 'data/country.json';

const logger = getLogger('WorkForm');
const resumeServices = new ResumeServices();
const fields = [
    'workName',
    'workCompanyName',
    'workStartDate',
    'workEndDate',
    'workCity',
    'workCountry',
    'workDescription'
];
const WorkForm = ({ data, index, isLast = false, messages, workData }) => {
	const trigger = useSelector(assistantSelectors.selectTrigger);
	const showAssistant = useSelector(assistantSelectors.selectShow);
	const resumeId = useSelector(selectId);
  const [validated, setValidated] = useState(false);
  const [cityOptions, setCityOptions] = useState([]);
	const [status, setStatus] = useState({
		workName: {},
		workCompanyName: {},
		workStartDate: {},
		workEndDate: {},
		workCity: {},
		workCountry: {},
		workDescription: {},
	});
	const dispatch = useDispatch();

	const save = async () => {
		GAEvent('Resume Edit', 'Save work form'); // call GA on save
		previewResume(messages.RPreview);
		let id = data.id;
		try {
			const response =
				id === undefined ? await resumeServices.createWork(resumeId, adaptWork(data)) : await resumeServices.updateWork(data.id, adaptWork(data));
			const responseJson = await response.json();
			id = id || responseJson.id;
		} catch (exception) {
			logger.error(exception);
		} finally {
      dispatch(actions.updateWorkId({ index, id }));
      dispatchUpdates('update-score');
		}
	};

	const handleSubmit = async (event) => {
		event.preventDefault();
        event.stopPropagation();
        updateAllStatus(validateWorkEntry, status, setStatus, fields, data)
		if (!validateWork(data)) {
			setValidated(false);
			return;
		}
		setValidated(true);
        dispatch(actions.completeWork());
        await save();
        handleWorkFormRating();
    };
    
    const handleWorkFormRating = async () => {
        const response = await resumeServices.getRatings(resumeId);

				const { workExperiences } = await response.json();
        const layoutRating = generateLayoutRating(wholePageCheck(messages.RPreview), messages)
        dispatch(actions.updateLayoutRating(layoutRating))
        
        const {
            companyArr,
            keywordsArr,
            quantifyArr,
            expArr,
            sortedArr
        } = generateSuggestions(workExperiences, 'workXp', 'work', isDescending(extractDate(workData, 'workStartDate')), messages)
        
        dispatch(actions.updateWorkRating({ 
            'amount': expArr,
            'company': companyArr,
            'keywords': keywordsArr,
            'quantify': quantifyArr,
            'sorted': sortedArr,
        }));
    }
    
    
    const handleWorkDescriptionEditorChange = (value) => {
        updateStatus(
            validateWorkEntry,
            status,
            setStatus,
            'workDescription',
            value
        );
        dispatch(actions.updateWorkDescription({ value, index }));
    }
    
	const handleWorkChange = (event) => {
		const value = event.target.value;
		updateStatus(validateWorkEntry, status, setStatus, 'workName', value);
		dispatch(actions.updateWorkName({ value, index }));
	};

	const handleCurrentWorkFlagChange = (event) => {
		event.preventDefault();
		const value = event.target.value;
		dispatch(actions.updateCurrentWorkFlag({ value, index }));
		// reset the end date value if current work is true
		dispatch(actions.updateWorkEndDate({ value: '', index }));
	};
	const handleWorkCompanyNameChange = (event) => {
		const value = event.target.value;
		updateStatus(validateWorkEntry, status, setStatus, 'workCompanyName', value);
		dispatch(actions.updateWorkCompanyName({ value, index }));
	};

	const handleWorkStartDateChange = (date) => {
		const value = date ? date.toISOString() : undefined;
		updateStatus(validateWorkEntry, status, setStatus, 'workStartDate', value);
		dispatch(actions.updateWorkStartDate({ value, index }));
	};

	const handleWorkEndDateChange = (date) => {
		const value = date ? date.toISOString() : undefined;
		updateStatus(validateWorkEntry, status, setStatus, 'workEndDate', value);
		dispatch(actions.updateWorkEndDate({ value, index }));
	};

	const handleWorkCityChange = (values) => {
		const value = values.length === 0 ? null : values[0].data;
		updateStatus(validateWorkEntry, status, setStatus, 'workCity', value);
		dispatch(actions.updateWorkCity({ value, index }));
	};

  const handleWorkCountryChange = (values) => {
    const value = values.length === 0 ? null : values[0].data
    updateCityOptions(value, setCityOptions)
    updateStatus(validateWorkEntry, status, setStatus, 'workCountry', value);
    dispatch(actions.updateWorkCountry({value, index}));
  };


	const handleAssistantClick = () => {
		dispatch(
			actions.toggleAssistant({
				trigger: 'work',
				context: { index, ...data },
			})
		);
	};

	const assistantContainerClassNames = classNames({
		writeAssistantContainer: true,
		active: showAssistant && trigger === 'work',
	});

	return (
		<Form validated={validated} onSubmit={handleSubmit}>
			<Row>
				<Col>
					<h2 className='form_h2'>{messages.enterNewExperience}</h2>
				</Col>
			</Row>
			<Row>
				<Col lg='4'>
					<InputGroup
						label={messages.workName}
						id='work-name'
						placeholder={messages.enterWorkName}
						value={data.workName}
						onChange={handleWorkChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.workName.isValid}
						isInvalid={status.workName.isInvalid}
					/>
				</Col>
				<Col lg='2'>
					<RadioButtonGroup
						label={messages.stillAtWork}
						id='work-currentWorkFlag'
						values={[
							{ label: messages.yes, value: true },
							{ label: messages.no, value: false },
						]}
						value={data.currentWorkFlag}
						onClickHandler={handleCurrentWorkFlagChange}
					/>
				</Col>
				<Col lg='6'>
					<InputGroup
						label={messages.companyName}
						id='work-company'
						placeholder={messages.enterCompanyName}
						value={data.workCompanyName}
						onChange={handleWorkCompanyNameChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.workCompanyName.isValid}
						isInvalid={status.workCompanyName.isInvalid}
					/>
				</Col>
			</Row>

			<Row>
				<Col>
					<SingleDatePicker
						label={messages.workStartDate}
						id='work-start-date'
						placeholder={messages.yymmdd}
						value={data.workStartDate}
						monthFormat={messages.monthFormat}
						displayFormat={messages.dateDisplayFormat}
						onDateChange={handleWorkStartDateChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.workStartDate.isValid}
						isInvalid={status.workStartDate.isInvalid}
					/>
				</Col>

				{data.currentWorkFlag && (
					<Col>
						<SingleDatePicker
							label={messages.workEndDate}
							id='work-end-date'
							placeholder={messages.yymmdd}
							value={data.workEndDate}
							monthFormat={messages.monthFormat}
							displayFormat={messages.dateDisplayFormat}
							onDateChange={handleWorkEndDateChange}
							feedbackMessage={messages.entryIsInvalid}
							isValid={status.workEndDate.isValid}
							isInvalid={status.workEndDate.isInvalid}
						/>
					</Col>
				)}
        <Col>
          <DropdownGroup
              label={messages.country}
              id='work-country'
              placeholder={messages.workCountry}
              options={countryOptions}
              value={data.workCountry}
              onChange={handleWorkCountryChange}
              feedbackMessage={messages.entryIsInvalid}
              isValid={status.workCountry.isValid}
              isInvalid={status.workCountry.isInvalid}
            />
				</Col>
				<Col>
					<DropdownGroup
						label={messages.city}
						id='work-city'
						placeholder={messages.workCity}
						options={cityOptions}
						value={data.workCity}
						onChange={handleWorkCityChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.workCity.isValid}
						isInvalid={status.workCity.isInvalid}
					/>
				</Col>
			</Row>

            <Row>
                <Col lg="12">
                    <div className={assistantContainerClassNames}>
                        <DraftEditor 
                            feedbackMessage={messages.entryIsInvalid}
                            isValid={status.workDescription.isValid}
                            isInvalid={status.workDescription.isInvalid}
                            label={messages.workDetailsDescription}
                            handleChangeCallback={handleWorkDescriptionEditorChange}
                            texts={data.workDescription} 
                            eventName={`work-${index}`}
                        /> 
                        
                        <span className='writeAssistant'>
                            <WrittenAssistIcon />
                            <Button variant="link" onClick={handleAssistantClick}>{messages.writeAssistant}</Button>
                        </span>
                    </div>
                </Col>
            </Row>
            <Row className="form_buttons">
                <Col className="space_betweens">
                    {/* just a placeholder so we do need to change the css */}
                    <p className="hidden"></p>
                    <KButton variant="primary" type="submit">
                        {messages.save}
                    </KButton>
                </Col>
            </Row>
        </Form>
    );
};

WorkForm.propTypes = {
	data: PropTypes.object.isRequired,
	index: PropTypes.number.isRequired,
	isLast: PropTypes.bool,
	messages: PropTypes.object.isRequired,
};

export default WorkForm;
