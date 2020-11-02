import React, {useMemo} from 'react';
import { useSelector, useDispatch } from 'react-redux';

import { useI8n } from 'shell/i18n';

import VolunteerForm from './VolunteerForm';
import Step from '../Step';
import { actions, volunteerSelectors } from '../../slicer';

const getForms = (volunteerData, messages) =>
	volunteerData.map((volunteer, index) => (
		<VolunteerForm volunteerData={volunteerData} data={volunteer} index={index} isLast={index === volunteer.length} messages={messages} key={`volunteer-${index}`} />
	));

const VolunteerExperience = () => {
	const volunteer = useSelector(volunteerSelectors.selectVolunteer);
	const dispatch = useDispatch();
	const messages = useI8n();
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
			{getForms(volunteer.data, messages)}
		</Step>
	);
};

export default VolunteerExperience;
