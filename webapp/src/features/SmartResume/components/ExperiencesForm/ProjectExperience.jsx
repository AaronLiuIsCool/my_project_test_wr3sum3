import React, { useMemo } from 'react';
import { useSelector, useDispatch } from 'react-redux';

import { useI8n } from 'shell/i18n';

import ProjectForm from './ProjectForm';
import Step from '../Step';
import { actions, projectSelectors, resumeBuilderSelectors } from '../../slicer';

const getForms = (projectData, messages, resumeLanguage) =>
	projectData.map((project, index) => (
		<ProjectForm projectData={projectData} data={project} index={index} isLast={index === project.length} messages={messages} key={`project-${index}`} resumeLanguage={resumeLanguage} />
	));

const ProjectExperience = () => {
	const project = useSelector(projectSelectors.selectProject);
	const dispatch = useDispatch();
	const messages = useI8n();
	const { language:resumeLanguage } = useSelector(resumeBuilderSelectors.selectResumeBuilder).data;
	
	const showAddButton = useMemo(() => {
		return project.data.every(item => item.id)
	}, [
		project.data
	])
	return (
		<Step
			showAddButton={showAddButton}
			id="projectXp"
			title={messages.project}
			subtitle={messages.projectInfo}
			icon="project.svg"
			addMore={true}
			addMoreMessage={messages.addNewExperience}
			handleAddMore={() => dispatch(actions.addNewProject())}>
			{getForms(project.data, messages, resumeLanguage)}
		</Step>
	);
};

export default ProjectExperience;
