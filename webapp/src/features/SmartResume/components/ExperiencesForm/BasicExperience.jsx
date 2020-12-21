import React from 'react';
import { useSelector } from 'react-redux';

import { useI8n } from 'shell/i18n';

import { basicSelectors, photoReferenceSelectors, resumeBuilderSelectors } from './../../slicer/';
import BasicForm from './BasicForm';
import Step from '../Step';

const BasicExperience = () => {
	const basic = useSelector(basicSelectors.selectBasic);
	const photoReference = useSelector(photoReferenceSelectors.selectPhotoReference);
	const messages = useI8n();
	const { language:resumeLanguage } = useSelector(resumeBuilderSelectors.selectResumeBuilder).data;

	return (
		<Step id="basicInfo" title={messages.basicInfo} subtitle={messages.enterBasicInfo} icon="job.svg">
			<BasicForm completed={basic.completed} data={basic.data} photoReference={photoReference} messages={messages}  resumeLanguage={resumeLanguage}/>
		</Step>
	);
};

export default BasicExperience;
