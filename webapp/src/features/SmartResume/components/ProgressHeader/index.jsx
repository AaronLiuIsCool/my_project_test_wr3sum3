import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { selectId } from '../../slicer';
import { useI8n } from 'shell/i18n';
import { ProgressBar } from 'react-bootstrap';
import { actions } from '../../slicer';
import ResumeServices from 'shell/services/ResumeServices';
import { getLogger } from 'shell/logger';

import styles from './ProgressHeader.module.css';

const resumeServices = new ResumeServices();
const logger = getLogger('App');
const MAX_SCORE = 100; // TODO: Maybe this can come from the backend


const ProgressHeader = ({ setScoreVisible, scoreVisible }) => {
	const resumeId = useSelector(selectId);

	const messages = useI8n();
	const dispatch = useDispatch();

	const [scorePercentage, setScorePercentage] = useState(0);
	const [scoreRating, setScoreRating] = useState('score_D');

	async function getResumeScore(resumeId) {
		let responseJson = {};
		try {
			responseJson = await resumeServices.getScore(resumeId);
		} catch (exception) {
			logger.error(exception)
		} finally {
			return responseJson;
		}
	}

	useEffect(() => {
		if (resumeId){
			updateScore();
      		window.addEventListener('update-score', () => {
        		updateScore()
      		});
		}
	}, [resumeId]); // eslint-disable-line
  const updateScore = () => {
    getResumeScore(resumeId).then((resumeScoreData) => {
      const percentage = Math.round(
        (resumeScoreData.totalScore / MAX_SCORE) * 100
      );
      setScorePercentage(percentage);
      if (percentage < 55) {
        setScoreRating('score_D');
      } else if (percentage < 70) {
        setScoreRating('score_C');
      } else if (percentage < 85) {
        setScoreRating('score_B');
      } else if (percentage <= 100) {
        setScoreRating('score_A');
      } else {
        setScorePercentage(0);
        setScoreRating('error');
        logger.error('简历打分异常');
      }
    });
  };
	const scoreRatingColor = {
		score_A: '#2abc6e',
		score_B: '#edbc4d',
		score_C: '#edbc4d',
		score_D: '#ff6565',
		error: '#ff6565',
	};

	return (
		<div className={styles.container}>
			<div className={styles.progress}>
				<p>
					<span style={{ color: scoreRatingColor[scoreRating] }}>{scorePercentage}%</span> {messages.resumeCompletion}
				</p>
				<ProgressBar className={styles.progressBar} now={scorePercentage} variant={scoreRating} />
			</div>
			<div className={styles.hideSM}>
				<p className={styles.title}>{messages.intensityScore}</p>
				<p>{messages.clickForDetails}</p>
			</div>
			<div className={styles.hideSM} onClick={() => setScoreVisible(!scoreVisible)}>
				<div className={styles.circle} style={{ color: scoreRatingColor[scoreRating], borderColor: scoreRatingColor[scoreRating] }} onClick={() => {
					dispatch(
						actions.toggleAssistant({
							trigger: 'rating',
							context: {},
						})
					);
				}}>{messages[scoreRating]}</div>
			</div>
		</div>
	);
};

export default ProgressHeader;
