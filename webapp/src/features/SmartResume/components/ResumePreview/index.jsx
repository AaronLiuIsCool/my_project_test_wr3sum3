import React, { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import { useI8n } from 'shell/i18n';

import { selectResume, selectValidatedResume, resumeBuilderSelectors } from './../../slicer/';
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
import { resumeAdaptor } from '../../utils/servicesAdaptor';
import { flatten, reconstruct } from '../../utils/resume';

import { Document, Page, pdfjs } from 'react-pdf';
pdfjs.GlobalWorkerOptions.workerSrc = `//cdnjs.cloudflare.com/ajax/libs/pdf.js/${pdfjs.version}/pdf.worker.js`;

const logger = getLogger('SmartResume');
const accountServices = new AccountServices();
const resumeServices = new ResumeServices();
const appServices = new AppServices();

const ResumePreview = ({ resumeName }) => {
	const messages = useI8n();

	const userId = useSelector(selectUserId);
	const resume = useSelector(selectResume);
	const validatedResume = useSelector(selectValidatedResume);
	const resumeBuilder = useSelector(resumeBuilderSelectors.selectresumeBuilder);
	const color = resumeBuilder.data.color;

	const [resumeData, setResumeData] = useState(resume.resumeBuilder.data.base64);
	const [isResumeTipsModalOpen, setIsResumeTipsModalOpen] = useState(false);
	const [resumeTipsSymbol, setResumeTipsSymbol] = useState("?");
	const [isThemeColorModalOpen, setIsThemeColorModalOpen] = useState(false);
	const [numOfPagesList, setNumOfPagesList] = useState([]);

	useEffect(() => {
		setResumeData(resume.resumeBuilder.data.base64);
	}, [resume]);
	const toggleThemeColorModal = () => {
        setIsThemeColorModalOpen(!isThemeColorModalOpen)
    }
    
	const handleTranslate = async () => {
		try {
			const parsedResume = flatten(resumeAdaptor(validatedResume));
			const translations = await appServices.translate(Object.values(parsedResume));
			Object.keys(parsedResume).forEach((key, index) => {
				parsedResume[key] = translations[index];
			});
			const translatedResume = reconstruct(parsedResume);
			const data = await resumeServices.createResume(translatedResume);
			if (data.success === false) {
				logger.error(data.message);
				return;
			}
			const resumeId = data.id;
			const resumeName = `translated-${resumeId}`;
			// todo: Need a better way to handle translated resume name
			window.open(`${resumeId}`, '_blank');
			accountServices.addResume(userId, resumeId, resumeName);
		} catch (exception) {
			logger.error(exception);
		}
	};

	function onDocumentLoadSuccess(pdf) {
    setNumOfPagesList(Array.from(Array(pdf.numPages), (v, i) => i + 1));
	}
	
	function resumeTipsOpenHandler(){
		setIsResumeTipsModalOpen(!isResumeTipsModalOpen);
		setResumeTipsSymbol(resumeTipsSymbol === "?" ? "X": "?");
	}

	return (
		<div className={styles.container}>
			<div id='displayPDF' className={styles.previewWrapper}>
				{resumeData && (
					<Document file={resumeData} onLoadSuccess={onDocumentLoadSuccess}>
						{numOfPagesList.map(i => <Page pageNumber={i} key={i} />)}
					</Document>
				)}
			</div>

			<div className={styles.widgetContainer} id="widgetsContainer">
				<div>
					<button className={styles.whiteBtn} onClick={() => toggleThemeColorModal()}>
						{messages.RPreview.editThemeColor} <span className={styles.colorSquare} style={{ backgroundColor: color }}></span>
					</button>
					<button onClick={handleTranslate}>{messages.RPreview.smartTranslation}</button>
					<button onClick={() => adjustToWholePage(messages.RPreview)}>{messages.RPreview.oneClickWholePage}</button>
					<button onClick={() => downloadPDF(messages.RPreview, resumeName)}>
						<img src={DownloadIcon} alt='download' /> {messages.RPreview.downloadResume}
					</button>
					<button className={styles.circle} onClick={resumeTipsOpenHandler}>
						{resumeTipsSymbol}
					</button>
				</div>
			</div>
			{isResumeTipsModalOpen && <ResumeTips />}
			{isThemeColorModalOpen && <ResumeThemeColorPicker setIsThemeColorModalOpen={setIsThemeColorModalOpen} messages={messages}/>}
		</div>
	);
};

export default ResumePreview;
