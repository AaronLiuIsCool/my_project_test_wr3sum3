import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { useI8n } from 'shell/i18n';

import { 
	selectResume, 
	basicSelectors, 
	educationSelectors, 
	workSelectors, 
	projectSelectors, 
	volunteerSelectors, 
	previewSelectors 
} from './../../slicer/';
import { selectUserId } from 'features/App/slicer';

import { getLogger } from 'shell/logger';
import ResumeServices from 'shell/services/ResumeServices';
import AccountServices from 'shell/services/AccountServices';
import AppServices from 'shell/services/AppServices';

import ResumeTips from './ResumeTips';
import HeaderSection from './HeaderSection';
import WorkExperience from './WorkExperience';
import VolunteerExperience from './VolunteerExperience';
import { downloadPDF } from './resumeBuilder';

import styles from '../../styles/ResumePreview.module.css';
import DownloadIcon from '../../assets/download_white.svg';
import CloseHoverIcon from '../../assets/close_hover.svg';
import CloseRegularIcon from '../../assets/close_regular.svg';
import { resumeAdaptor } from '../../utils/servicesAdaptor';
import { flatten, reconstruct } from '../../utils/resume';

const logger = getLogger('SmartResume');
const accountServices = new AccountServices();
const resumeServices = new ResumeServices();
const appServices = new AppServices();

const ResumePreview = () => {
	const messages = useI8n();

	const userId = useSelector(selectUserId);
	const resume = useSelector(selectResume);
	const { data: basicData } = useSelector(basicSelectors.selectBasic);
	let { data: educationData } = useSelector(educationSelectors.selectEducation);
	const { data: workData } = useSelector(workSelectors.selectWork);
	const { data: projectData } = useSelector(projectSelectors.selectProject);
	const { data: volunteerData } = useSelector(volunteerSelectors.selectVolunteer);
	educationData = educationData[0]; // need to ask if user enter more than one education

	let { header: headerLineCount, work: workLineCount, volunteer: volunteerLineCount } = useSelector(previewSelectors.selectPreview).data;

	const [lineHeight, setLineHeight] = useState('1.5em');
	const [fontSize, setFontSize] = useState('12px');
	const [resumeTipsModal, setResumeTipsModal] = useState(false);//todo: update
	const [pageBreakCount, setPageBreakCount] = useState(30);

	const lineHeightStyle = {
		lineHeight: lineHeight,
		fontSize: fontSize,
	};

	const adjustToWholePage = () => {
		const totalLineCount = headerLineCount + workLineCount + volunteerLineCount;
		if (totalLineCount > pageBreakCount && totalLineCount < pageBreakCount * 1.5) {
			setLineHeight('1em');
			setFontSize('11px');
			setPageBreakCount(40);
		} else if (totalLineCount >= pageBreakCount * 1.5 && totalLineCount < pageBreakCount * 2) {
			setLineHeight('1.75em');
			setFontSize('13px');
			setPageBreakCount(25);
		} else {
			alert('Too big');
		}
	};

	const handleTranslate = async () => {
		try {
			const parsedResume = flatten(resumeAdaptor(resume));
			let response = await appServices.translate(Object.values(parsedResume));
			const translations = await response.json();
			Object.keys(parsedResume).forEach((key, index) => {
				parsedResume[key] = translations[index];
			});
			const translatedResume = reconstruct(parsedResume);
			response = await resumeServices.createResume(translatedResume);
			const data = await response.json();
			if (!data.success) {
				logger.error(data.message);
				return;
			}
			const resumeId = data.id;
			window.open(`${resumeId}`, '_blank'); // open translated resume in a new tab
			accountServices.addResume(userId, resumeId);
		} catch (exception) {
			logger.error(exception);
		}
	};


	return (
		<div className={styles.container}>
			<div className={styles.pageContainer} style={lineHeightStyle}>
				<HeaderSection basicData={basicData} educationData={educationData} />
				<WorkExperience workData={workData} projectData={projectData} pageBreakCount={pageBreakCount} />
				<VolunteerExperience volunteerData={volunteerData} pageBreakCount={pageBreakCount} />
			</div>
			<div className={styles.widgetContainer}>
				<div>
					<button className={styles.whiteBtn}>{messages.RPreview.editThemeColor} </button>
					<button onClick={handleTranslate} >{messages.RPreview.smartTranslation}</button>
					<button onClick={adjustToWholePage}>{messages.RPreview.oneClickWholePage}</button>
					<button onClick={() => downloadPDF({basicData, educationData, workData, projectData, volunteerData, messagesRP:messages.RPreview})}>
						<img src={DownloadIcon} alt="download" /> {messages.RPreview.downloadResume}
					</button>
					<button className={styles.circle} onClick={() => setResumeTipsModal(true)}>?</button>
				</div>
			</div>
			{/* ResumeTips close button  */}
			{resumeTipsModal &&<div className={styles.modalCloseButton} onClick={() => setResumeTipsModal(false)}>
				<img src={CloseRegularIcon} alt="Close" className={styles.closeImg} />
				<img src={CloseHoverIcon} alt="Close" className={styles.closeImgOnHover} />
			</div>}
			{resumeTipsModal && <ResumeTips />}
		</div>
	);
};

export default ResumePreview;
