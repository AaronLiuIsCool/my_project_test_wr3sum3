import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { useDispatch } from 'react-redux';
import { Row, Col, Form } from 'react-bootstrap';

import SingleDatePicker from 'components/SingleDatePicker';
import InputGroup from 'components/InputGroup';
import RadioButtonGroup from 'components/RadioButtonGroup';
import DropdownGroup from 'components/DropdownGroup';
import KButton from 'components/KButton';
import TextArea from 'components/TextArea';

import { actions } from '../../slicer';
import { validateWork, validateWorkEntry } from '../../slicer/work';
import { updateStatus } from '../../slicer/common';

import cityOptions from 'data/city.json';

const WorkForm = ({ data, index, isLast = false, messages }) => {
	const [validated, setValidated] = useState(false);
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

	const handleSubmit = (event) => {
		event.preventDefault();
		event.stopPropagation();
		if (!validateWork(data)) {
			setValidated(false);
			return;
		}
		// TODO: Save this use API.
		alert(`Save this to kuaidao service: ${JSON.stringify(data)}`);
		setValidated(true);
	};

	const handleWorkChange = (event) => {
		const value = event.target.value;
		updateStatus(validateWorkEntry, status, setStatus, 'workName', value);
		dispatch(actions.updateWorkName({ value, index }));
	};
	
	const handleCurrentWorkFlagChange = (event) => {
		const value = event.target.value;
		console.log('value', value);
		dispatch(actions.updateCurrentWorkFlag({ value, index }));
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
		const value = values.length === 0 ? null : values[0].city;
		updateStatus(validateWorkEntry, status, setStatus, 'workCity', value);
		dispatch(actions.updateWorkCity({ value, index }));
	};

	const handleWorkCountryChange = (event) => {
		const value = event.target.value;
		updateStatus(validateWorkEntry, status, setStatus, 'workCountry', value);
		dispatch(actions.updateWorkCountry({ value, index }));
	};
	const handleWorkDescriptionChange = (event) => {
		const value = event.target.value;
		updateStatus(validateWorkEntry, status, setStatus, 'workDescription', value);
		dispatch(actions.updateWorkDescription({ value, index }));
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
						label={messages.workName}
						id="work-name"
						placeholder={messages.enterWorkName}
						value={data.workName}
						onChange={handleWorkChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.workName.isValid}
						isInvalid={status.workName.isInvalid}
					/>
				</Col>
				<Col lg="2">
					<RadioButtonGroup
						label={messages.stillAtWork}
						id="work-currentWorkFlag"
						values={[
							{ label: messages.yes, value: true },
							{ label: messages.no, value: false },
						]}
						value={data.currentWorkFlag}
						onClickHandler={handleCurrentWorkFlagChange}
					/>
				</Col>
				<Col lg="6">
					<InputGroup
						label={messages.companyName}
						id="work-company"
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
						label={messages.enterWorkDate}
						id="work-start-date"
						placeholder={messages.yymmdd}
						value={data.workStartDate}
						allowPastDatesOnly={true}
						readOnly={true}
						monthFormat={messages.monthFormat}
						displayFormat={messages.dateDisplayFormat}
						onDateChange={handleWorkStartDateChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.workStartDate.isValid}
						isInvalid={status.workStartDate.isInvalid}
					/>
				</Col>

				{!data.currentWorkFlag && (<Col>
					<SingleDatePicker
						label={messages.enterWorkDate}
						id="work-end-date"
						placeholder={messages.yymmdd}
						value={data.workEndDate}
						allowPastDatesOnly={true}
						readOnly={true}
						monthFormat={messages.monthFormat}
						displayFormat={messages.dateDisplayFormat}
						onDateChange={handleWorkEndDateChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.workEndDate.isValid}
						isInvalid={status.workEndDate.isInvalid}
					/>
				</Col>)}

				<Col>
					<DropdownGroup
						label={messages.city}
						id="work-city"
						placeholder={messages.workCity}
						searchKey="city"
						options={cityOptions}
						value={data.workCity}
						onChange={handleWorkCityChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.workCity.isValid}
						isInvalid={status.workCity.isInvalid}
					/>
				</Col>
				<Col>
					<InputGroup
						label={messages.country}
						id="work-country"
						placeholder={messages.workCountry}
						value={data.workCountry}
						onChange={handleWorkCountryChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.workCountry.isValid}
						isInvalid={status.workCountry.isInvalid}
					/>
				</Col>
			</Row>

			<Row>
				<Col lg="12">
					{/*todo: replace with rich text editor */}
					<TextArea 
						label={messages.workDetailsDescription}
						id="work-country"
						placeholder={messages.workCountry}
						value={data.workDescription}
						onChange={handleWorkDescriptionChange}
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

WorkForm.propTypes = {
	data: PropTypes.object.isRequired,
	index: PropTypes.number.isRequired,
	isLast: PropTypes.bool,
	messages: PropTypes.object.isRequired,
};

export default WorkForm;
