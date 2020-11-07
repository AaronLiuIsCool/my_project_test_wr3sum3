import React, { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import { getLogger } from 'shell/logger';
import { useHistory } from 'react-router-dom';
import { I8nContext } from 'shell/i18n';
import { selectLanguage } from 'features/App/slicer';

import JobDetailHeader from './JobDetailHeader';
import LeftWrapper from './LeftWrapper';

import MatchingServices from 'shell/services/MatchingServices';
import JobsServices from 'shell/services/JobsServices';
import ResumeServices from 'shell/services/ResumeServices';
import styles from '../styles/JobRefinement.module.css';

import zh from '../i18n/zh.json';
import en from '../i18n/en.json';

const logger = getLogger('JobRefinementModal');
const matchingServices = new MatchingServices();
const jobsServices = new JobsServices();
const resumeServices = new ResumeServices();

const JobRefinement = () => {
	const language = useSelector(selectLanguage);
	const messages = language === 'zh' ? zh : en;
	const [score, setScore] = useState(0);
	const [navIndex, setNavIndex] = useState(0);
	const history = useHistory();
	const queryParams = new URLSearchParams(history.location.search);
	const [resumeId] = useState(queryParams.get('resumeId'));
	const [jobId] = useState(queryParams.get('jobId'));
	const [refinementData, setRefinementData] = useState([]);
	const [jobData, setJobData] = useState({});
	const [resumeData, setResumeData] = useState({});
	const [workExperiences, setWorkExperiences] = useState([{ description: "" }]);

	const getResumeData = async () => {
		if (resumeId) {
			try {
				const resumeData = await resumeServices.getResume(resumeId);
				await setResumeData(resumeData);
				if (resumeData.workExperiences) {
					await setWorkExperiences(resumeData.workExperiences);
				}
			} catch (exception) {
				logger.error(exception)
			}
			return;
		}
		return history.replace('/');
	}

	const jobDetailsSearch = async () => {
		try {
			const response = await jobsServices.getJob(jobId);
			setJobData(response);
		} catch (exception) {
			logger.error(exception);
		}
	}
	const jobRefinementSearch = async () => {
		try {
			const data = await await matchingServices.getJobRefinement(jobId, resumeId);
			await getResumeData();
			setRefinementData(data.resumeJobScores);
			let resumeScore = 0;
			data.resumeJobScores.forEach(item => resumeScore += item.fullMark);
			setScore(resumeScore);
		} catch (exception) {
			logger.error(exception);
		}
	}

	const updateResume = async () => {
		try {
			await resumeServices.bulkUpdateWork(resumeId, workExperiences);
		} catch (exception) {
			logger.error(exception);
		} finally {
			jobRefinementSearch();
		}
	};

	useEffect(() => {
		jobRefinementSearch();
		jobDetailsSearch();
		// eslint-disable-next-line
	}, [])

	return (
		<I8nContext.Provider value={messages}>
			<div className={styles.jobRefinementContainer}>
				<div className={styles.left}>
					<LeftWrapper score={score} navIndex={navIndex} setNavIndex={setNavIndex} refinementData={refinementData} workExperiences={workExperiences} setWorkExperiences={setWorkExperiences} resumeData={resumeData} setResumeData={setResumeData} updateResume={updateResume} />

				</div>
				<div className={styles.right}>
					<JobDetailHeader data={jobData} />
					<div>
						<div className={styles['job-details-content']}>
							<div className={styles['job-details-description']}>
								<div className={styles['job-details-description-label']}>{messages['job_details_description']}</div>
								{jobData.jobDescription && jobData.jobDescription.split('\n').map((text, index) => (
									<p key={index}>{text}</p>
								))}
							</div>
						</div>
					</div>
				</div>
			</div>
		</I8nContext.Provider>
	);
};

export default JobRefinement;
