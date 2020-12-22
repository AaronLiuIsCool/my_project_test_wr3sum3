import React, { useState, useRef } from 'react';
import Button from 'react-bootstrap/Button';
import { useI8n } from 'shell/i18n';
import AppServices from 'shell/services/AppServices';
import { useDispatch } from 'react-redux';
import { actions } from 'features/SmartResume/slicer';
import NameResume from './NameResume';

import { resumeAdaptorForSDK } from '../../SmartResume/utils/servicesAdaptor';

import store from "store";

import styles from '../styles/ResumeStarter.module.css';
const appServices = new AppServices();

const ResumerStarter = () => {
	const dispatch = useDispatch();
	const messages = useI8n();
	const fileUploadInput = useRef(null);

	const [importedResumeData, setImportedResumeData] = useState({});
	const [isModalOpen, setIsModalOpen] = useState(false);

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
	};

	const onChangeHandler = (e) => {
		if (e.target.files.length > 0) {
			handleSubmit(e.target.files[0]);
		}
	};

	const updateImportedResumeData = () => {
		
		setTimeout(()=>{
			const	resumeData = resumeAdaptorForSDK(store.getState().resume);
			setImportedResumeData(resumeData)
		},0)

	}

	// 把文件转成base64 string 
	const toBase64 = file => new Promise((resolve, reject) => {
		const reader = new FileReader();
		reader.readAsDataURL(file);
		reader.onload = () => resolve(reader.result);
		reader.onerror = error => reject(error);
	});

	const handleSubmit = async (file) => {
		// remove "data:application/pdf;base64," from base64 string
		const base64Str = await toBase64(file);
		const data = { base64: base64Str.slice(28), fileName: file.name };
		const response = await appServices.resumeSDKUpload(data);
		if (response?.status?.message === "success") {
			await updateInfo(response.data);
			updateImportedResumeData()
			setIsModalOpen(true)
		}
	};

	const fileUpload = () => {
		fileUploadInput.current.click();
	};

	const createNewResumeFromBeginning = () => {
		setIsModalOpen(true);
		setImportedResumeData({});
	}

	return (
		<div className={styles.container}>
			<p className={styles.title}>{messages.createResume}</p>
			<div className={styles.optionWrapper}>
				<div className={styles.option}>
					<p className={styles.optionTitle}>{messages.createNewResume}</p>
					<p className={styles.optionDescription}>{messages.chooseLanguage}</p>
					<div className={styles.buttonWrapper}>
						<Button onClick={createNewResumeFromBeginning} className={styles.button}>{messages.createResume}</Button>
						{/* <Button onClick={createNewResumeFromBeginning} className={styles.button}>{messages.language_toggle_en}</Button> */}
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

			{isModalOpen ? <NameResume importedResumeData={importedResumeData}/> : null}
		</div>
	);
};

export default ResumerStarter;
