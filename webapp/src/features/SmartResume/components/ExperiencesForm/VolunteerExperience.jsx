import React, {useMemo} from 'react';
import { useSelector, useDispatch } from 'react-redux';

import { useI8n } from 'shell/i18n';

import VolunteerForm from './VolunteerForm';
import Step from '../Step';
import { actions, volunteerSelectors, resumeBuilderSelectors } from '../../slicer';

const getForms = (volunteerData, messages, resumeLanguage) =>
	volunteerData.map((volunteer, index) => (
		<VolunteerForm volunteerData={volunteerData} data={volunteer} index={index} isLast={index === volunteer.length} messages={messages} key={`volunteer-${index}`} resumeLanguage={resumeLanguage}/>
	));

const VolunteerExperience = () => {
	const volunteer = useSelector(volunteerSelectors.selectVolunteer);
	const dispatch = useDispatch();
	const messages = useI8n();
	const { language:resumeLanguage } = useSelector(resumeBuilderSelectors.selectResumeBuilder).data;
	const showAddButton = useMemo(() => {
		return volunteer.data.every(item => item.id)
	}, [
		volunteer.data
	])
	return (
		<Step
			showAddButton={showAddButton}
			id="otherXp"
			title={messages.volunteerExperience}
			subtitle={messages.enterVolunteerExperience}
			icon="volunteer.svg"
			addMore={true}
			addMoreMessage={messages.addNewExperience}
			handleAddMore={() => dispatch(actions.addNewVolunteer())}>
			{getForms(volunteer.data, messages, resumeLanguage)}
		</Step>
	);
};

export default VolunteerExperience;
