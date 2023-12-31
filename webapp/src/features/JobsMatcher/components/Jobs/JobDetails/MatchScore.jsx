// Note: this component has been removed from Jobs listing page, not sure if we need it or not in the future

import React from 'react';

import Button from 'react-bootstrap/Button';
import ScoreRing from 'components/ScoreRing';

import { useI8n } from 'shell/i18n';

import styles from '../../../styles/JobDetails.module.css';

const MatchScore = ({ score, description, modalOpenHandler }) => {
	const messages = useI8n();

	return (
		<div className={styles['score-container']}>
			<div className={styles['score-icon']}>
				<ScoreRing stroke={2} radius={24} progress={score} color='#edbc4d' />
				<div className={styles['score']}>{score}</div>
			</div>
			<div className={styles['score-description']}>
				<div className={styles['score-description-main']}>{messages['job_matching_score']}</div>
				<div className={styles['score-description-sub']}>{description}</div>
			</div>
			<Button className={styles['suggestions']} variant='outline-warning' onClick={() => modalOpenHandler(true)}>
				{messages['job_finishing_touches']}
			</Button>
		</div>
	);
};

export default MatchScore;
