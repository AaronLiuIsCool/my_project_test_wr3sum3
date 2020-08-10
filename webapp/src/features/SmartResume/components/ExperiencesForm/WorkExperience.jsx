import React from 'react';
import { useSelector, useDispatch } from 'react-redux';

import { useI8n } from 'shell/i18n';

import WorkForm from './WorkForm';
import Step from '../Step';
import { actions, workSelectors } from '../../slicer';

const getForms = (WorkData, messages) =>
	WorkData.map((Work, index) => (
		<WorkForm data={Work} index={index} isLast={index === Work.length} messages={messages} key={`Work-${index}`} />
	));

const WorkExperience = () => {
	const Work = useSelector(workSelectors.selectWork);
	const dispatch = useDispatch();
	const messages = useI8n();
	return (
		<Step
			id="Work"
			title={messages.work}
			subtitle={messages.workInfo}
			icon="job.svg"
			addMore={true}
			addMoreMessage={messages.addNewExperience}
			handleAddMore={() => dispatch(actions.addNewWork())}>
			{getForms(Work.data, messages)}
		</Step>
	);
};

export default WorkExperience;
