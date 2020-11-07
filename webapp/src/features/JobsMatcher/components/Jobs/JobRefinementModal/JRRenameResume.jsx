import React, { useState } from 'react';
import { useI8n } from 'shell/i18n';
import ResumeServices from 'shell/services/ResumeServices';
import AccountServices from 'shell/services/AccountServices';
import { useHistory } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { selectUserId } from 'features/App/slicer';
import { actions } from 'features/SmartResume/slicer';
import { getLogger } from 'shell/logger';
import { Form, Button } from 'react-bootstrap';
import styles from '../../../styles/JobRefinementModal.module.css';

const logger = getLogger('SmartResume');
const accountServices = new AccountServices();
const resumeServices = new ResumeServices();

const JRRenameResume = ({jobId}) => {
	const messages = useI8n();
	const userId = useSelector(selectUserId);
	const dispatch = useDispatch();
	const [resumeName, setResumeName] = useState('');

	const history = useHistory();

	const handleRenameResume = async(e) => {
		e.preventDefault();
		// todo: save resume name
		const queryParams = new URLSearchParams(history.location.search);
		let resumeId = queryParams.get('resume');
		
		// create a new resume 
		const resumeData = await resumeServices.getResume(resumeId);
		const data = await resumeServices.createResume(resumeData);

		if (data.success === false) {
			logger.error(data.message);
			return;
		}
		resumeId = data.id;
		await accountServices.addResume(userId, resumeId, resumeName);
		dispatch(actions.setId(resumeId));
		history.replace(`/refinement?jobId=${jobId}&resumeId=${resumeId}`);

	};

	return (
		<div className={styles.step_sm_container}>
			<div className={styles.h2}>{messages['refinement_name_it']}</div>
			<div className={styles.p}>{messages['refinement_name_suggestion']}</div>

			<div className={styles.inputContainer}>
				<Form.Control input='text' size='lg' autoFocus value={resumeName} onChange={(event) => setResumeName(event.target.value)} focused="true"/>
			</div>
			<div className={styles.buttonContainer}>
				<Button disabled={resumeName.length === 0} className={styles['search-button']} onClick={handleRenameResume}>
					{messages['refinement_confirm']}
				</Button>
			</div>
		</div>
	);
};

export default JRRenameResume;
