import React, { useState } from 'react';
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

	const [formEnabled, setFormEnabled] = useState(false);
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
	};

	const handleSubmit = async (e) => {
		e.preventDefault();
		e.stopPropagation();
		const data = new FormData();
		data.append('file', selectedFile[0]);

		const response = await appServices.resumeSDKUpload(data);
		if (response) {
				await updateInfo(response.data.result);
				previewResume(messages.RPreview)
		}
	};

	return (
		<div className={styles.container}>
			<Button href='/resume' className={styles.button}>{messages.createNewResume}</Button>
			<Button  className={styles.button} onClick={() => setFormEnabled(true)}>{messages.resumeUpload}</Button>
			{formEnabled && (
				<div>
					<div className={styles.uploadContainer}>
						<label>{messages.uploadYourResume}</label>
						<input type='file' className={styles.fileInput} onChange={onChangeHandler} />
						{/* todo: need a loading button here  */}
						<Button  className={styles.submitButton}  variant='primary' type='button' onClick={handleSubmit}>
							{messages.upload}
						</Button>
					</div>
				</div>
			)}
		</div>
	);
};

export default ResumerStarter;
