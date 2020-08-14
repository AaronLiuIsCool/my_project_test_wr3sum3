import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import { useI8n } from 'shell/i18n';
import { basicSelectors, educationSelectors, workSelectors, projectSelectors, volunteerSelectors } from './../../slicer/';

import HeaderSection from './HeaderSection';
import WorkExperience from './WorkExperience';
import VolunteerExperience from './VolunteerExperience';

import styles from '../../styles/ResumePreview.module.css';

const ResumePreview = () => {
	const messages = useI8n();

	const {data:basicData} = useSelector(basicSelectors.selectBasic);
	let {data:educationData} = useSelector(educationSelectors.selectEducation);
	const {data:workData} = useSelector(workSelectors.selectWork);
	const {data:projectData} = useSelector(projectSelectors.selectProject);
	const {data:volunteerData} = useSelector(volunteerSelectors.selectVolunteer);
	educationData = educationData[0]; // need to ask if user enter more than one education 

  // todo: adjust spaces

	return (
		<div className={styles.container}>
			<div className={styles.pageContainer}>
				<HeaderSection basicData={basicData} educationData={educationData} />
				<WorkExperience workData={workData} projectData={projectData} />
				<VolunteerExperience volunteerData={volunteerData} />
			</div>
			<div className={styles.widgetContainer}>
				<div>
					<button className={styles.whiteBtn}>{messages.RPreview.editThemeColor} </button>
					<button>{messages.RPreview.smartTranslation}</button>
					<button>{messages.RPreview.oneClickWholePage}</button>
					<button>{messages.RPreview.downLoadResume}</button>
					<button>?</button>
				</div>
			</div>
		</div>
	);
};

export default ResumePreview;
