import React, { useMemo } from 'react';

import { useSelector, useDispatch } from 'react-redux';

import { useI8n } from 'shell/i18n';

import WorkForm from './WorkForm';
import Step from '../Step';
import { actions, workSelectors, resumeBuilderSelectors } from '../../slicer';

const getForms = (WorkData, messages, resumeLanguage) =>
	WorkData.map((Work, index) => (
		<WorkForm workData={WorkData} data={Work} index={index} isLast={index === Work.length} messages={messages} key={`Work-${index}`} resumeLanguage={resumeLanguage}/>
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
	const { language:resumeLanguage } = useSelector(resumeBuilderSelectors.selectResumeBuilder).data;
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
			{getForms(Work.data, messages, resumeLanguage)}
		</Step>
	);
};

export default WorkExperience;
