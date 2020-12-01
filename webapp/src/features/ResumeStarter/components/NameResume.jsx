import React, { useState } from 'react';
import { useI8n } from 'shell/i18n';
import { useHistory } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { selectLanguage, selectUserId } from 'features/App/slicer';
import ResumeServices from 'shell/services/ResumeServices';
import AccountServices from 'shell/services/AccountServices';
import { actions } from 'features/SmartResume/slicer';
import { getLogger } from 'shell/logger';

import { Form, Button } from 'react-bootstrap';
import styles from '../styles/ResumeStarter.module.css';

const logger = getLogger('ResumeStarter');
const accountServices = new AccountServices();
const resumeServices = new ResumeServices();

const NameResume = ({ importedResumeData }) => {
	const history = useHistory();
	const messages = useI8n();
	const userId = useSelector(selectUserId);
	const language = useSelector(selectLanguage);
	const dispatch = useDispatch();
	const [inputValue, setInputValue] = useState('');

	const createResume = async (resumeName) => {
		try {
			const data = await resumeServices.createResume({ language, ...importedResumeData });
			const resumeId = data.id;
			await accountServices.addResume(userId, resumeId, resumeName);
			dispatch(actions.setId(resumeId));
			history.push(`/resume/${resumeId}`);
		} catch (exception) {
			logger.error(exception);
			history.push('/');
		}
	}
	const handleRenameResume = e => {
		e.preventDefault();
		createResume(inputValue, importedResumeData);
	};

	return (
		<div className={styles.full_screen_container}>
			<div className={styles.h2}>{messages['name_it']}</div>
			<div className={styles.p}>{messages['name_suggestion']}</div>

			<div className={styles.inputContainer}>
				<Form.Control input='text' size='lg' autoFocus value={inputValue} onChange={(event) => setInputValue(event.target.value)} />
			</div>
			<div className={styles.buttonContainer}>
				<Button disabled={inputValue.length === 0} className={styles['search-button']} onClick={handleRenameResume}>
					{messages['confirm']}
				</Button>
			</div>
		</div>
	);
};

export default NameResume;
