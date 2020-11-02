import React, { useMemo } from 'react';

import { useSelector, useDispatch } from 'react-redux';

import { useI8n } from 'shell/i18n';

import WorkForm from './WorkForm';
import Step from '../Step';
import { actions, workSelectors } from '../../slicer';

const getForms = (WorkData, messages) =>
	WorkData.map((Work, index) => (
		<WorkForm workData={WorkData} data={Work} index={index} isLast={index === Work.length} messages={messages} key={`Work-${index}`} />
	));

const WorkExperience = () => {
	const Work = useSelector(workSelectors.selectWork);
	const showAddButton = useMemo(() => {
		return Work.data.every(item => item.id)
	}, [
		Work.data
	])
	const dispatch = useDispatch();
	const messages = useI8n();
	return (
		<Step
			showAddButton={showAddButton}
			id="workXp"
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
