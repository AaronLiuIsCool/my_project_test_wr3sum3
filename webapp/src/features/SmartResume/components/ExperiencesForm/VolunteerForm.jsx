import React, { useState } from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import { useDispatch, useSelector } from 'react-redux';
import { Row, Col, Form } from 'react-bootstrap';

import SingleDatePicker from 'components/SingleDatePicker';
import InputGroup from 'components/InputGroup';
import DropdownGroup from 'components/DropdownGroup';
import RadioButtonGroup from 'components/RadioButtonGroup';
import Button from 'react-bootstrap/Button';
import DraftEditor from '../../../../components/DraftEditor/index'
import { generateSuggestions, isDescending, extractDate, generateLayoutRating } from '../../utils/resume';

import { ReactComponent as WrittenAssistIcon } from '../../assets/writing_assit.svg';

import { adaptVolunteer } from '../../utils/servicesAdaptor';
import { actions, selectId, assistantSelectors } from '../../slicer';
import { validateVolunteer, validateVolunteerEntry } from '../../slicer/volunteer';
import { updateStatus, updateAllStatus } from '../../slicer/common';
import ResumeServices from 'shell/services/ResumeServices';
import { getLogger } from 'shell/logger';
import { previewResume, wholePageCheck } from '../ResumePreview/resumeBuilder';

import cityOptions from 'data/city.json';

const logger = getLogger('VolunteerForm');
const resumeServices = new ResumeServices();
const fields = [
    'volunteerRole',
    'currentVolunteerFlag',
    'volunteerCompanyName',
    'volunteerStartDate',
    'volunteerEndDate',
    'volunteerCity',
    'volunteerCountry',
    'volunteerDescription'
];
const VolunteerForm = ({ data, index, isLast = false, messages, volunteerData }) => {
	const trigger = useSelector(assistantSelectors.selectTrigger);
	const showAssistant = useSelector(assistantSelectors.selectShow);
	const resumeId = useSelector(selectId);
	const [validated, setValidated] = useState(false);
	const [status, setStatus] = useState({
		volunteerRole: {},
		currentVolunteerFlag: {},
		volunteerCompanyName: {},
		volunteerStartDate: {},
		volunteerEndDate: {},
		volunteerCity: {},
		volunteerCountry: {},
		volunteerDescription: {},
	});
	const dispatch = useDispatch();

	const save = async () => {
		previewResume(messages.RPreview);
		let id = data.id;
		try {
			const response =
				id === undefined
					? await resumeServices.createVolunteer(resumeId, adaptVolunteer(data))
					: await resumeServices.updateVolunteer(data.id, adaptVolunteer(data));
			const responseJson = await response.json();
			id = id || responseJson.id;
		} catch (exception) {
			logger.error(exception);
		} finally {
			dispatch(actions.updateVolunteerId({ index, id }));
		}
	};

    const handleProjectFormRating = async () => {
        
        const response = await resumeServices.getRatings(resumeId);

        // TODO: uncomment this
        const { volunteerExperiences } = await response.json();
        
        // todo delete this afterwards
        // const { volunteerExperiences } = response;
        const layoutRating = generateLayoutRating(wholePageCheck(messages.RPreview), messages)
        dispatch(actions.updateLayoutRating(layoutRating))
        const {
            companyArr,
            keywordsArr,
            quantifyArr,
            expArr,
            sortedArr
        } = generateSuggestions(volunteerExperiences, 'otherXp', 'volunteer', isDescending(extractDate(volunteerData, 'volunteerStartDate')), messages)

        dispatch(actions.updateVolunteerRating({ 
            'amount': expArr,
            'company': companyArr,
            'keywords': keywordsArr,
            'quantify': quantifyArr,
            'sorted': sortedArr,
        }));
    }
	const handleSubmit = async (event) => {
		event.preventDefault();
        event.stopPropagation();
        updateAllStatus(validateVolunteerEntry, status, setStatus, fields, data)
		if (!validateVolunteer(data)) {
			setValidated(false);
			return;
		}
		setValidated(true);
        dispatch(actions.completeVolunteer());
        await save();
        handleProjectFormRating();
	};

	const handleVolunteerRoleChange = (event) => {
		const value = event.target.value;
		updateStatus(validateVolunteerEntry, status, setStatus, 'volunteerRole', value);
		dispatch(actions.updateVolunteerRole({ value, index }));
	};

	const handleCurrentVolunteerFlagChange = (event) => {
		event.preventDefault();
		const value = event.target.value;
		updateStatus(validateVolunteerEntry, status, setStatus, 'currentVolunteerFlag', value);
		dispatch(actions.updateCurrentVolunteerFlag({ value, index }));
		// reset the end date value if current volunteer is true
		dispatch(actions.updateVolunteerEndDate({ value: '', index }));
	};

	const handleVolunteerCompanyNameChange = (event) => {
		const value = event.target.value;
		updateStatus(validateVolunteerEntry, status, setStatus, 'volunteerCompanyName', value);
		dispatch(actions.updateVolunteerCompanyName({ value, index }));
	};

	const handleVolunteerStartDateChange = (date) => {
		const value = date ? date.toISOString() : undefined;
		updateStatus(validateVolunteerEntry, status, setStatus, 'volunteerStartDate', value);
		dispatch(actions.updateVolunteerStartDate({ value, index }));
	};

	const handleVolunteerEndDateChange = (date) => {
		const value = date ? date.toISOString() : undefined;
		updateStatus(validateVolunteerEntry, status, setStatus, 'volunteerEndDate', value);
		dispatch(actions.updateVolunteerEndDate({ value, index }));
	};

	const handleCityChange = (values) => {
		const value = values.length === 0 ? null : values[0].data;
		updateStatus(validateVolunteerEntry, status, setStatus, 'volunteerCity', value);
		dispatch(actions.updateVolunteerCity({ value, index }));
	};

	const handleCountryChange = (event) => {
		const value = event.target.value;
		updateStatus(validateVolunteerEntry, status, setStatus, 'volunteerCountry', value);
		dispatch(actions.updateVolunteerCountry({ value, index }));
	};
    
    const handleVolunteerDescriptionEditorChange = (value) => {
        updateStatus(
            validateVolunteerEntry,
            status,
            setStatus,
            'volunteerDescription',
            value
        );
        dispatch(actions.updateVolunteerDescription({ value, index }));
    }

	const handleAssistantClick = () => {
		dispatch(
			actions.toggleAssistant({
				trigger: 'volunteer',
				context: { index, ...data },
			})
		);
	};
	const assistantContainerClassNames = classNames({
		writeAssistantContainer: true,
		active: showAssistant && trigger === 'volunteer',
	});

	return (
		<Form validated={validated} onSubmit={handleSubmit}>
			<Row>
				<Col>
					<h2 className="form_h2">{messages.enterNewExperience}</h2>
				</Col>
			</Row>
			<Row>
				<Col lg="4">
					<InputGroup
						label={messages.participateRole}
						id="volunteer-name"
						placeholder={messages.enterParticipateRole}
						value={data.volunteerRole}
						onChange={handleVolunteerRoleChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.volunteerRole.isValid}
						isInvalid={status.volunteerRole.isInvalid}
					/>
				</Col>
				<Col lg="2">
					<RadioButtonGroup
						label={messages.endOrNot}
						id="volunteer-currentVolunteerFlag"
						values={[
							{ label: messages.yes, value: true },
							{ label: messages.no, value: false },
						]}
						value={data.currentVolunteerFlag}
						onClickHandler={handleCurrentVolunteerFlagChange}
						feedbackMessage={messages.entryIsInvalid}
                        isInvalid={status.currentVolunteerFlag.isInvalid}
                        isValid={status.currentVolunteerFlag.isValid}
					/>
				</Col>
				<Col>
					<InputGroup
						label={messages.companyName}
						id="volunteer-company"
						placeholder={messages.enterCompanyName}
						value={data.volunteerCompanyName}
                        onChange={handleVolunteerCompanyNameChange}
                        isValid={status.volunteerCompanyName.isValid}
						isInvalid={status.volunteerCompanyName.isInvalid}
					/>
				</Col>
			</Row>
			<Row>
				<Col>
					<SingleDatePicker
						label={messages.volunteerStartDate}
						id="volunteer-enter-date"
						placeholder={messages.yymmdd}
						value={data.volunteerStartDate}
						monthFormat={messages.monthFormat}
						displayFormat={messages.dateDisplayFormat}
						onDateChange={handleVolunteerStartDateChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.volunteerStartDate.isValid}
						isInvalid={status.volunteerStartDate.isInvalid}
					/>
				</Col>
				{data.currentVolunteerFlag && (
					<Col>
						<SingleDatePicker
							label={messages.volunteerEndDate}
							id="volunteer-graduate-date"
							placeholder={messages.yymmdd}
							value={data.volunteerEndDate}
							monthFormat={messages.monthFormat}
							displayFormat={messages.dateDisplayFormat}
							onDateChange={handleVolunteerEndDateChange}
							feedbackMessage={messages.entryIsInvalid}
							isValid={status.volunteerEndDate.isValid}
							isInvalid={status.volunteerEndDate.isInvalid}
						/>
					</Col>
				)}
				<Col>
					<DropdownGroup
						label={messages.city}
						id="volunteer-city"
						placeholder={messages.experienceCity}
						options={cityOptions}
						value={data.volunteerCity}
						onChange={handleCityChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.volunteerCity.isValid}
						isInvalid={status.volunteerCity.isInvalid}
					/>
				</Col>
				<Col>
					<InputGroup
						label={messages.country}
						id="volunteer-country"
						placeholder={messages.experienceCountry}
						value={data.volunteerCountry}
						onChange={handleCountryChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.volunteerCountry.isValid}
						isInvalid={status.volunteerCountry.isInvalid}
					/>
				</Col>
			</Row>
			<Row>
				<Col lg="12">
					{/*todo: replace with rich text editor */}
					<div className={assistantContainerClassNames}>
                        <DraftEditor 
                            feedbackMessage={messages.entryIsInvalid}
                            isValid={status.volunteerDescription.isValid}
                            isInvalid={status.volunteerDescription.isInvalid}
                            label={messages.volunteerDetailsDescription}
                            handleChangeCallback={handleVolunteerDescriptionEditorChange}
                            texts={data.volunteerDescription} 
                            eventName={`volunteer-${index}`}
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
				<Col className="space_betweens">
					{/* just a placeholder so we do need to change the css */}
					<p className="hidden"></p>
					<Button variant="primary" type="submit">
						{messages.save}
					</Button>
				</Col>
			</Row>
		</Form>
	);
};

VolunteerForm.propTypes = {
	data: PropTypes.object.isRequired,
	index: PropTypes.number.isRequired,
	isLast: PropTypes.bool,
	messages: PropTypes.object.isRequired,
};

export default VolunteerForm;
