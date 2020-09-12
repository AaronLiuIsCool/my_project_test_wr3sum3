import React, { useState } from 'react';
import { useI8n } from 'shell/i18n';

import { Form, Button } from 'react-bootstrap';
import styles from '../../../styles/JobRefinement.module.css';

const JRRenameResume = ({ stepHandler }) => {
	const messages = useI8n();

	const [inputValue, setInputValue] = useState('');

	const handleRenameResume = e => {
		e.preventDefault();
		stepHandler(3);
		// todo: call rename api?
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
