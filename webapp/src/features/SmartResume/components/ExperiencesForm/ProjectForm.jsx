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
import TextArea from 'components/TextArea';
import { ReactComponent as WrittenAssistIcon } from '../../assets/writing_assit.svg';

import { adaptProject } from '../../utils/servicesAdaptor';
import { actions, selectId, assistantSelectors } from '../../slicer';
import { validateProject, validateProjectEntry } from '../../slicer/project';
import { updateStatus } from '../../slicer/common';
import ResumeServices from 'shell/services/ResumeServices';
import { getLogger } from 'shell/logger';

import cityOptions from 'data/city.json';

const logger = getLogger('ProjectForm');
const resumeServices = new ResumeServices();

const ProjectForm = ({ data, index, isLast = false, messages }) => {
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
	const dispatch = useDispatch();

	const save = async () => {
        let id;
        try {
            const response = (data.id === undefined) ?
                await resumeServices.createProject(resumeId, adaptProject(data)) :
                await resumeServices.updateProject(data.id, adaptProject(data));
            const responseJson = await response.json();
            id = responseJson.id;
        } catch(exception) {
            logger.error(exception);
        } finally {
            dispatch(actions.updateProjectId({ index, id }));
        }
    };

		const handleSubmit = (event) => {
		event.preventDefault();
		event.stopPropagation();
		
		if (!validateProject(data)) {
			setValidated(false);
			return;
		}
		setValidated(true);
		save();
	};

	const handleProjectRoleChange = (event) => {
		const value = event.target.value;
		updateStatus(validateProjectEntry, status, setStatus, 'projectRole', value);
		dispatch(actions.updateProjectRole({ value, index }));
	};

	const handleCurrentProjectFlagChange = (event) => {
		const value = event.target.value;
		updateStatus(validateProjectEntry, status, setStatus, 'currentProjectFlag', value);
		dispatch(actions.updateCurrentProjectFlag({ value, index }));

		// reset the end date value if current project is true
		if (value){
			dispatch(actions.updateProjectEndDate({ value: "", index }));
		}
	};

	const handleProjectCompanyNameChange = (event) => {
		const value = event.target.value;
		updateStatus(validateProjectEntry, status, setStatus, 'projectCompanyName', value);
		dispatch(actions.updateProjectCompanyName({ value, index }));
	};

	const handleProjectStartDateChange = (date) => {
		const value = date ? date.toISOString() : undefined;
		updateStatus(validateProjectEntry, status, setStatus, 'projectStartDate', value);
		dispatch(actions.updateProjectStartDate({ value, index }));
	};

	const handleProjectEndDateChange = (date) => {
		const value = date ? date.toISOString() : undefined;
		updateStatus(validateProjectEntry, status, setStatus, 'projectEndDate', value);
		dispatch(actions.updateProjectEndDate({ value, index }));
	};

	const handleProjectDescriptionChange = (event) => {
		const value = event.target.value;
		updateStatus(validateProjectEntry, status, setStatus, 'projectDescription', value);
		dispatch(actions.updateProjectDescription({ value, index }));
	};

	const handleCityChange = (values) => {
		const value = values.length === 0 ? null : values[0].city;
		updateStatus(validateProjectEntry, status, setStatus, 'projectCity', value);
		dispatch(actions.updateProjectCity({ value, index }));
	};

	const handleCountryChange = (event) => {
		const value = event.target.value;
		updateStatus(validateProjectEntry, status, setStatus, 'projectCountry', value);
		dispatch(actions.updateProjectCountry({ value, index }));
	};

    const handleAssistantClick = () => {
        dispatch(actions.toggleAssistant({
            trigger: "project", 
            context: {index, ...data}
        }));
	};
	
	const assistantContainerClassNames = classNames({
        'writeAssistantContainer': true,
        'active': showAssistant && trigger === 'project'
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
							{ label: messages.yes, value: true },
							{ label: messages.no, value: false },
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
						allowPastDatesOnly={true}
						readOnly={true}
						monthFormat={messages.monthFormat}
						displayFormat={messages.dateDisplayFormat}
						onDateChange={handleProjectStartDateChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.projectStartDate.isValid}
						isInvalid={status.projectStartDate.isInvalid}
					/>
				</Col>
				{data.currentProjectFlag && (<Col>
					<SingleDatePicker
						label={messages.projectEndDate}
						id="project-graduate-date"
						placeholder={messages.yymmdd}
						value={data.projectEndDate}
						allowPastDatesOnly={true}
						readOnly={true}
						monthFormat={messages.monthFormat}
						displayFormat={messages.dateDisplayFormat}
						onDateChange={handleProjectEndDateChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.projectEndDate.isValid}
						isInvalid={status.projectEndDate.isInvalid}
					/>
				</Col>)}
				<Col>
					<DropdownGroup
						label={messages.city}
						id="project-city"
						placeholder={messages.projectCity}
						searchKey="city"
						options={cityOptions}
						value={data.projectCity}
						onChange={handleCityChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.projectCity.isValid}
						isInvalid={status.projectCity.isInvalid}
					/>
				</Col>
				<Col>
					<InputGroup
						label={messages.country}
						id="project-country"
						placeholder={messages.projectCountry}
						value={data.projectCountry}
						onChange={handleCountryChange}
						feedbackMessage={messages.entryIsInvalid}
						isValid={status.projectCountry.isValid}
						isInvalid={status.projectCountry.isInvalid}
					/>
				</Col>
			</Row>
			<Row>
                <Col lg="12">
                    {/*todo: replace with rich text editor */}
                    <div className={assistantContainerClassNames}>
						<TextArea
							label={messages.projectDetailsDescription}
							id="volunteer-description"
							placeholder={messages.enterProjectDetailsDescription}
							value={data.projectDescription}
							onChange={handleProjectDescriptionChange}
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
					<Button variant="primary" type="submit">
						{messages.save}
					</Button>
				</Col>
			</Row>
		</Form>
	);
};

ProjectForm.propTypes = {
	data: PropTypes.object.isRequired,
	index: PropTypes.number.isRequired,
	isLast: PropTypes.bool,
	messages: PropTypes.object.isRequired,
};

export default ProjectForm;
