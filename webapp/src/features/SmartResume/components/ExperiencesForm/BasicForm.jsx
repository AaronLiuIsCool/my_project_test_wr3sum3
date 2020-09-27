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
import { updateStatus } from '../../slicer/common';
import { adaptBasics } from '../../utils/servicesAdaptor';
import ResumeServices from 'shell/services/ResumeServices';
import { getLogger } from 'shell/logger';

import { previewResume } from '../ResumePreview/resumeBuilder';

// json data for dropdowns
import cityOptions from 'data/city.json';

const logger = getLogger('BasicForm');
const resumeServices = new ResumeServices();

const BasicForm = ({ data, completed, messages }) => {
	const resumeId = useSelector(selectId);
	const [validated, setValidated] = useState(false);
	const [status, setStatus] = useState({
		nameCn: {},
		nameEn: {},
		email: {},
		phone: {},
		city: {},
		linkedin: {},
		weblink: {},
	});
	const dispatch = useDispatch();


	const save = async () => {
		previewResume(messages.RPreview);
		let id;
		try {
			const response =
				data.id === undefined
					? await resumeServices.createBasics(resumeId, adaptBasics({ completed: true, data }))
					: await resumeServices.updateBasics(resumeId, adaptBasics({ completed: true, data }));
			const responseJson = await response.json();
			id = id || responseJson.id;
		} catch (exception) {
			logger.error(exception);
		} finally {
			dispatch(actions.updateBasicsId({ id }));
		}
	};

	const handleSubmit = (event) => {
		event.preventDefault();
		event.stopPropagation();
		if (!validateBasic(data)) {
			setValidated(false);
			// todo: revert, this is for testing
			// return;
		}
		setValidated(true);
		dispatch(actions.completeBasic());
		save();
	};

	const handleNameCnChange = (event) => {
		const value = event.target.value;
		updateStatus(validateBasicEntry, status, setStatus, 'nameCn', value);
		dispatch(actions.updateNameCn({ value }));
	};

	const handleNameEnChange = (event) => {
		const value = event.target.value;
		updateStatus(validateBasicEntry, status, setStatus, 'nameEn', value);
		dispatch(actions.updateNameEn({ value }));
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

	const handleCityChange = (values) => {
		const value = values.length === 0 ? null : values[0].city;
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
		<Form validated={validated} onSubmit={handleSubmit}>
			<Row>
				<AvatarUpload />
			</Row>
			<Row>
				<Col>
					<InputGroup
						label={messages.cnName}
						id='basic-name-cn'
						placeholder={messages.enterCnName}
						value={data.nameCn}
						onChange={handleNameCnChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.nameCn.isValid}
						isInvalid={status.nameCn.isInvalid}
					/>
				</Col>
				<Col>
					<InputGroup
						label={messages.enName}
						id='basic-name-en'
						placeholder={messages.enterEnName}
						value={data.nameEn}
						onChange={handleNameEnChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.nameEn.isValid}
						isInvalid={status.nameEn.isInvalid}
					/>
				</Col>
			</Row>

			<Row>
				<Col lg='6'>
					<InputGroup
						label={messages.email}
						id='basic-email'
						placeholder={messages.enterEmail}
						value={data.email}
						onChange={handleEmailChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.email.isValid}
						isInvalid={status.email.isInvalid}
					/>
				</Col>
				<Col lg='3'>
					<InputGroup
						label={messages.phone}
						id='basic-phone'
						placeholder={messages.enterPhone}
						value={data.phone}
						onChange={handlePhoneChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.phone.isValid}
						isInvalid={status.phone.isInvalid}
					/>
				</Col>
				<Col lg='3'>
					<DropdownGroup
						label={messages.schoolCity}
						id='education-city'
						placeholder={messages.schoolCity}
						searchKey='city'
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
						id='basic-linkedin'
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
						id='basic-weblink'
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
			<Row className='form_buttons'>
				<Col className='space_betweens'>
					{/* just a placeholder so we do need to change the css */}
					<p className='hidden'></p>
					<Button variant='primary' type='submit'>
						{messages.save}
					</Button>
				</Col>
			</Row>
		</Form>
	);
};

BasicForm.propTypes = {
	data: PropTypes.object.isRequired,
	completed: PropTypes.bool.isRequired,
	messages: PropTypes.object.isRequired,
};

export default BasicForm;
