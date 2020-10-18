import React, { useState } from 'react';
import { useI8n } from 'shell/i18n';
import { useHistory } from 'react-router-dom';

import { Form, Button } from 'react-bootstrap';
import styles from '../../../styles/JobRefinementModal.module.css';

const JRRenameResume = () => {
	const messages = useI8n();
	const history = useHistory();

	const [inputValue, setInputValue] = useState('');

	const handleRenameResume = e => {
		e.preventDefault();
		// todo: save resume name
	};

	return (
		<div className={styles.step_sm_container}>
			<div className={styles.h2}>{messages['refinement_name_it']}</div>
			<div className={styles.p}>{messages['refinement_name_suggestion']}</div>

			<div className={styles.inputContainer}>
				<Form.Control input='text' size='lg' id='searchQuery' autoFocus value={inputValue} onChange={(event) => setInputValue(event.target.value)} />
			</div>
			<div className={styles.buttonContainer}>
				<Button disabled={inputValue.length === 0} className={styles['search-button']} onClick={handleRenameResume}>
					{messages['refinement_confirm']}
				</Button>
			</div>
		</div>
	);
};

export default JRRenameResume;
