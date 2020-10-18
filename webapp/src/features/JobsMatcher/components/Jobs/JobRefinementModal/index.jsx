import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import JROverWriteResume from './JROverWriteResume';
import JRRenameResume from './JRRenameResume';

import styles from '../../../styles/JobRefinementModal.module.css';
import CloseHoverIcon from '../../../assets/close_hover.svg';
import CloseRegularIcon from '../../../assets/close_regular.svg';

const renderModalSteppers = (jobId, resumeId, history, modalOpenHandler, step, setStep) => {

	return (
		<>
			<div className='closeIconContainer' style={{ top: '-40px', right: '10px' }} onClick={() => modalOpenHandler(false)}>
				<img src={CloseRegularIcon} alt='Close' className='closeIcon' />
				<img src={CloseHoverIcon} alt='Close' className='closeIconHover' />
			</div>
			{step === 1 && <JROverWriteResume stepHandler={setStep} />}
			{step === 2 && <JRRenameResume />}
			{step === 3 && history.push(`/refinement?jobId=${jobId}&resumeId=${resumeId}`)}
		</>
	);
};

const JobRefinementModal = ({ data, modalOpenHandler, selectedJob }) => {
	const history = useHistory();

	const [step, setStep] = useState(1); 
	const [resumeId] = useState(history.location.search.slice(8)); 

	return (
		<div className={styles.container}>
			<div className={styles.body}>{Boolean(data) ? renderModalSteppers(data.jobs[selectedJob].jobUuid, resumeId, history, modalOpenHandler, step, setStep) : null}</div>
		</div>
	);
};

export default JobRefinementModal;
