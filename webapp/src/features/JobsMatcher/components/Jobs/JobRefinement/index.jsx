import React, { useState } from 'react';
import JROverWriteResume from './JROverWriteResume';
import JRRenameResume from './JRRenameResume';
import JRResumerDetails from './JRResumerDetails';

import styles from '../../../styles/JobRefinement.module.css';
import CloseHoverIcon from '../../../assets/close_hover.svg';
import CloseRegularIcon from '../../../assets/close_regular.svg';

const renderModalSteppers = (data, modalOpenHandler, step, setStep) => {


	return (
		<>
			<div className='closeIconContainer' style={{ top: '-40px', right: '10px' }} onClick={() => modalOpenHandler(false)}>
				<img src={CloseRegularIcon} alt='Close' className='closeIcon' />
				<img src={CloseHoverIcon} alt='Close' className='closeIconHover' />
			</div>
			{step === 1 && <JROverWriteResume stepHandler={setStep} />}
			{step === 2 && <JRRenameResume stepHandler={setStep} />}
			{step === 3 && <JRResumerDetails stepHandler={setStep} data={data} />}
		</>
	);
};

const JobRefinement = ({ data, modalOpenHandler }) => {
	const [step, setStep] = useState(1); 
	return (
		<div className={styles.container}>
			<div className={styles.body}>{Boolean(data) ? renderModalSteppers(data, modalOpenHandler, step, setStep) : null}</div>
		</div>
	);
};

export default JobRefinement;
