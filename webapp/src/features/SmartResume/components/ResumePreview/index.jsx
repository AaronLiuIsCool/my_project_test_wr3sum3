import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { useI8n } from 'shell/i18n';

import { selectResume, resumeBuilderSelectors } from './../../slicer/';
import { selectUserId } from 'features/App/slicer';

import { getLogger } from 'shell/logger';
import ResumeServices from 'shell/services/ResumeServices';
import AccountServices from 'shell/services/AccountServices';
import AppServices from 'shell/services/AppServices';

import ResumeTips from './ResumeTips';
import ResumeThemeColorPicker from './ResumeThemeColorPicker';
import { downloadPDF, adjustToWholePage } from './resumeBuilder';

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
	const resumeBuilder = useSelector(resumeBuilderSelectors.selectresumeBuilder);
	const color = resumeBuilder.data.color;

	const [isResumeTipsModalOpen, setIsResumeTipsModalOpen] = useState(false);
	const [isThemeColorModalOpen, setIsThemeColorModalOpen] = useState(false);

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
			<div id='displayPDF' className={styles.iframeWrapper}>
				<iframe src='' className={styles.iframeStyle} title='preview'></iframe>
			</div>

			<div className={styles.widgetContainer}>
				<div>
					<button className={styles.whiteBtn} onClick={() => setIsThemeColorModalOpen(true)}>
						{messages.RPreview.editThemeColor} <span className={styles.colorSquare} style={{ backgroundColor: color }}></span>
					</button>
					<button onClick={handleTranslate}>{messages.RPreview.smartTranslation}</button>
					<button onClick={adjustToWholePage}>{messages.RPreview.oneClickWholePage}</button>
					<button onClick={() => downloadPDF(messages.RPreview)}>
						<img src={DownloadIcon} alt='download' /> {messages.RPreview.downloadResume}
					</button>
					<button className={styles.circle} onClick={() => setIsResumeTipsModalOpen(true)}>
						?
					</button>
				</div>
			</div>
			{/* ResumeTips close button  */}
			{isResumeTipsModalOpen && (
				<div className='closeIconContainer' style={{ top: '10px', right: '50px' }} onClick={() => setIsResumeTipsModalOpen(false)}>
					<img src={CloseRegularIcon} alt='Close' className='closeIcon' />
					<img src={CloseHoverIcon} alt='Close' className='closeIconHover' />
				</div>
			)}
			{isResumeTipsModalOpen && <ResumeTips />}
			{isThemeColorModalOpen && <ResumeThemeColorPicker setIsThemeColorModalOpen={setIsThemeColorModalOpen}/>}
		</div>
	);
};

export default ResumePreview;
