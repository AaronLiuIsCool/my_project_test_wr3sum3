import React, { useState, useRef } from 'react';
import Button from 'react-bootstrap/Button';
import { useI8n } from 'shell/i18n';
import { useHistory } from 'react-router';
import AppServices from 'shell/services/AppServices';
import { useDispatch } from 'react-redux';
import { actions } from 'features/SmartResume/slicer';
import { previewResume } from 'features/SmartResume/components/ResumePreview/resumeBuilder';

import styles from '../styles/ResumeStarter.module.css';
const appServices = new AppServices();

const ResumerStarter = () => {
	const dispatch = useDispatch();
	const history = useHistory();
	const messages = useI8n();
	const fileUploadInput = useRef(null);

	const [selectedFile, setSelectedFile] = useState([]);

	const updateBasic = (data) => {
		dispatch(actions.updateBasicFromResumeSDK({ data }));
	};

	const updateEducation = (data) => {
		const eduData = data.education_objs;
		eduData.forEach((education, index) => {
			if (index > 0) dispatch(actions.addNewEducation());
			dispatch(actions.updateEducationFromResumeSDK({ data: education, index }));
		});
	};

	const updateWork = (data) => {
		const workData = data.job_exp_objs;
		workData.forEach((work, index) => {
			if (index > 0) dispatch(actions.addNewWork());
			dispatch(actions.updateWorkFromResumeSDK({ data: work, index }));
		});
	};

	const updateProject = (data) => {
		const projectData = data.proj_exp_objs;
		projectData.forEach((project, index) => {
			if (index > 0) dispatch(actions.addNewProject());
			dispatch(actions.updateProjectFromResumeSDK({ data: project, index }));
		});
	};

	const updateCertificate = (data) => {
		const certificateData = data.training_objs;
		certificateData.forEach((certificate, index) => {
			if (index > 0) dispatch(actions.addNewProject());
			dispatch(actions.updateCertificateFromResumeSDK({ data: certificate, index }));
		});
	};

	const updateInfo = (data) => {
		updateBasic(data);
		updateEducation(data);
		updateWork(data);
		updateProject(data);
		updateCertificate(data);
		// redirect user to builder page
		history.push('/resume');
	};

	const onChangeHandler = (e) => {
		setSelectedFile(e.target.files);
		handleSubmit();
	};

	const handleSubmit = async () => {
		const data = new FormData();
		data.append('file', selectedFile[0]);

		const response = await appServices.resumeSDKUpload(data);
		if (response) {
			await updateInfo(response.data.result);
			previewResume(messages.RPreview)
		}
	};

	const fileUpload = () => {
		fileUploadInput.current.click();
	};

	return (
		<div className={styles.container}>
			<p className={styles.title}>{messages.createResume}</p>
			<div className={styles.optionWrapper}>
				<div className={styles.option}>
					<p className={styles.optionTitle}>{messages.createNewResume}</p>
					<p className={styles.optionDescription}>{messages.chooseLanguage}</p>
					<div className={styles.buttonWrapper}>
						<Button href='/resume' className={styles.button}>{messages.language_toggle_zh}</Button>
						<Button href='/resume' className={styles.button}>{messages.language_toggle_en}</Button>
					</div>
				</div>
				<div className={styles.option}>
					<p className={styles.optionTitle}>{messages.resumeUpload}</p>
					<p className={styles.optionDescription}>{messages.resumeUploadSupportType}</p>
					<Button className={styles.uploadButton} onClick={fileUpload}>{messages.resumeUpload}</Button>
				</div>
			</div>

			<div className={styles.hidden}>
				<input type='file' ref={fileUploadInput} onChange={onChangeHandler} />
			</div>

		</div>
	);
};

export default ResumerStarter;
