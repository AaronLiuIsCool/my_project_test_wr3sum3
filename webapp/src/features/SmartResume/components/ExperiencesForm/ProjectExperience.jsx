import React from 'react';
import { useSelector, useDispatch } from 'react-redux';

import { useI8n } from 'shell/i18n';

import ProjectForm from './ProjectForm';
import Step from '../Step';
import { actions, projectSelectors } from '../../slicer';

const getForms = (projectData, messages) =>
	projectData.map((project, index) => (
		<ProjectForm projectData={projectData} data={project} index={index} isLast={index === project.length} messages={messages} key={`project-${index}`} />
	));

const ProjectExperience = () => {
	const project = useSelector(projectSelectors.selectProject);
	const dispatch = useDispatch();
	const messages = useI8n();
	return (
		<Step
			id="projectXp"
			title={messages.project}
			subtitle={messages.projectInfo}
			icon="project.svg"
			addMore={true}
			addMoreMessage={messages.addNewExperience}
			handleAddMore={() => dispatch(actions.addNewProject())}>
			{getForms(project.data, messages)}
		</Step>
	);
};

export default ProjectExperience;
