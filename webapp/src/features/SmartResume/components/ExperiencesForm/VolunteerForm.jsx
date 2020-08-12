import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';
import { Row, Col, Form } from 'react-bootstrap';

import SingleDatePicker from 'components/SingleDatePicker';
import InputGroup from 'components/InputGroup';
import DropdownGroup from 'components/DropdownGroup';
import RadioButtonGroup from 'components/RadioButtonGroup';
import KButton from 'components/KButton';
import TextArea from 'components/TextArea';

import { adaptVolunteer } from '../../utils/servicesAdaptor';
import { actions, selectId } from '../../slicer';
import { validateVolunteer, validateVolunteerEntry } from '../../slicer/volunteer';
import { updateStatus } from '../../slicer/common';
import ResumeServices from 'shell/services/ResumeServices';
import { getLogger } from 'shell/logger';

import cityOptions from 'data/city.json';

const logger = getLogger('VolunteerForm');
const resumeServices = new ResumeServices();

const VolunteerForm = ({ data, index, isLast = false, messages }) => {
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
        let id;
        try {
            const response = (data.id === undefined) ?
                await resumeServices.createVolunteer(resumeId, adaptVolunteer(data)) :
                await resumeServices.updateVolunteer(data.id, adaptVolunteer(data));
            const responseJson = await response.json();
            id = responseJson.id;
        } catch(exception) {
            logger.error(exception);
        } finally {
            dispatch(actions.updateVolunteerId({ index, id }));
        }
    };

	const handleSubmit = (event) => {
		event.preventDefault();
		event.stopPropagation();
		if (!validateVolunteer(data)) {
			setValidated(false);
			return;
		}
		setValidated(true);
		save();
	};

	const handleVolunteerRoleChange = (event) => {
		const value = event.target.value;
		updateStatus(validateVolunteerEntry, status, setStatus, 'volunteerRole', value);
		dispatch(actions.updateVolunteerRole({ value, index }));
	};

	const handleCurrentVolunteerFlagChange = (event) => {
		const value = event.target.value;
		updateStatus(validateVolunteerEntry, status, setStatus, 'currentVolunteerFlag', value);
		dispatch(actions.updateCurrentVolunteerFlag({ value, index }));
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
		const value = values.length === 0 ? null : values[0].city;
		updateStatus(validateVolunteerEntry, status, setStatus, 'volunteerCity', value);
		dispatch(actions.updateEduCity({ value, index }));
	};

	const handleCountryChange = (event) => {
		const value = event.target.value;
		updateStatus(validateVolunteerEntry, status, setStatus, 'volunteerCountry', value);
		dispatch(actions.updateCountry({ value, index }));
	};

	const handleVolunteerDescriptionChange = (event) => {
		const value = event.target.value;
		updateStatus(validateVolunteerEntry, status, setStatus, 'volunteerDescription', value);
		dispatch(actions.updateVolunteerDescription({ value, index }));
	};

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
					/>
				</Col>
				<Col>
					<InputGroup
						label={messages.companyName}
						id="volunteer-company"
						placeholder={messages.enterCompanyName}
						value={data.volunteerCompanyName}
						onChange={handleVolunteerCompanyNameChange}
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
						allowPastDatesOnly={true}
						readOnly={true}
						monthFormat={messages.monthFormat}
						displayFormat={messages.dateDisplayFormat}
						onDateChange={handleVolunteerStartDateChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.volunteerStartDate.isValid}
						isInvalid={status.volunteerStartDate.isInvalid}
					/>
				</Col>
				
				{!data.currentVolunteerFlag && (<Col>
					<SingleDatePicker
						label={messages.volunteerEndDate}
						id="volunteer-graduate-date"
						placeholder={messages.yymmdd}
						value={data.volunteerEndDate}
						allowPastDatesOnly={true}
						readOnly={true}
						monthFormat={messages.monthFormat}
						displayFormat={messages.dateDisplayFormat}
						onDateChange={handleVolunteerEndDateChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.volunteerEndDate.isValid}
						isInvalid={status.volunteerEndDate.isInvalid}
					/>
				</Col>)}
				<Col>
					<DropdownGroup
						label={messages.city}
						id="volunteer-city"
						placeholder={messages.experienceCity}
						searchKey="city"
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
					<TextArea
						label={messages.volunteerDetailsDescription}
						id="volunteer-description"
						placeholder={messages.enterVolunteerDetailsDescription}
						value={data.volunteerDescription}
						onChange={handleVolunteerDescriptionChange}
					/>
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

VolunteerForm.propTypes = {
	data: PropTypes.object.isRequired,
	index: PropTypes.number.isRequired,
	isLast: PropTypes.bool,
	messages: PropTypes.object.isRequired,
};

export default VolunteerForm;
