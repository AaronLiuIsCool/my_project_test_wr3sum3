import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { actions, previewSelectors } from '../../slicer/';
import { useI8n } from 'shell/i18n';
import { dateRangeBuilder, render_Description, render_pageBreaker } from './common';
import { anyWorkChanges } from '../../slicer/work';
import { anyProjectChanges } from '../../slicer/project';

import styles from '../../styles/ResumePreview.module.css';

const WorkExperience = ({ workData, projectData }) => {
	let workLineCount = 0;
	const dispatch = useDispatch();

	let { header: headerLineCount } = useSelector(previewSelectors.selectPreview).data;

	const updateLineCountBy = (n) => {
		workLineCount += n;
	};

	useEffect(
		() => {
			dispatch(actions.updatelineNum({ value: workLineCount, section: 'work' }));
		},
		//run it once after component mounted.
		[workData, projectData] // eslint-disable-line react-hooks/exhaustive-deps
	);

	const messages = useI8n();

	if (anyWorkChanges(workData) || anyProjectChanges(projectData)) {
		updateLineCountBy(1);
		return (
			<div className={styles.section}>
				<div className={styles.sectionHeader}>{messages.RPreview.workExperience}</div>
				{workData.map((work, index) => (
					<React.Fragment key={`work-${index}`}>
						<div className={styles.infoGroupHeader}>
							<div>
								<span className={styles.infoGroupTitle}>{work.workName}</span>
								<span className={styles.infoGroupSubtitle}>{work.workCompanyName}</span>
							</div>
							<div className={styles.infoGroupDate}>
								{dateRangeBuilder(work.workStartDate, work.workEndDate)} {work.workCity} {work.workCountry}
							</div>
						</div>
						{updateLineCountBy(1)}
						{render_pageBreaker(workLineCount + headerLineCount)}
						{render_Description(work.workDescription, workLineCount)}
						{updateLineCountBy(work.workDescription.split('\n').length)}
					</React.Fragment>
				))}

				{projectData.map((project, index) => (
					<React.Fragment key={`project-${index}`}>
						<div className={styles.infoGroupHeader}>
							<div>
								<span className={styles.infoGroupTitle}>{project.projectRole}</span>
								<span className={styles.infoGroupSubtitle}>{project.projectCompanyName}</span>
							</div>
							<div className={styles.infoGroupDate}>
								{dateRangeBuilder(project.projectStartDate, project.projectEndDate)} {project.projectCity} {project.projectCountry}
							</div>
						</div>
						{render_Description(project.projectDescription)}
					</React.Fragment>
				))}
			</div>
		);
	} else {
		return '';
	}
};

export default WorkExperience;
