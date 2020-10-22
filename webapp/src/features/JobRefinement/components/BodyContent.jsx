import React from 'react';

import DraftEditor from 'components/DraftEditor'

import styles from '../styles/leftPanel.module.css';

import checkGreenIcon from '../assets/checkmarkGreen.svg';
import warningIcon from '../assets/warning.svg';


const handleWorkDescriptionChange = (value, index, workExperiences, setWorkExperiences) => {
	let updatedWorkExperiences = [];
	if (!workExperiences) {
		updatedWorkExperiences = [{ description: value }];
	}
	else {
		updatedWorkExperiences = [...workExperiences];
		updatedWorkExperiences[index].description = value;
	}
	setWorkExperiences(updatedWorkExperiences);
}

const BodyContent = ({ item, summary, workExperiences, setWorkExperiences, messages }) => {
	if (item.category === "KeywordScoreRule") {
		return (
			<>
				<div className={styles.BodyContentTitle}>{messages[item.category]}</div>
				<p>{summary}</p>
				<div className={styles.BodyItemUnderline}>
					<img src={checkGreenIcon} alt="checkGreenIcon" /> {messages.resume_already_in_use}
				</div>
				<div className={styles.BodyItemUnderKeywords}>
					{item.extraInfo.resumeIncludedKeywords.map((item, index) =>
						<span key={index}>{item}</span>
					)}
				</div>

				<div className={styles.BodyItemUnderlineBad}>
					<img src={warningIcon} alt="warningIcon" /> {messages.resume_still_need}
				</div>
				<div className={styles.BodyItemUnderKeywordsBad}>
					{item.extraInfo.resumeNotcludedKeywords.map((item, index) =>
						<span key={index}>{item}</span>
					)}
				</div>
				{workExperiences.map((work, index) =>
					(
						<>
							<p className={styles.workRole}>{work?.role}</p>
							<DraftEditor
								handleChangeCallback={(value) => handleWorkDescriptionChange(value, index, workExperiences, setWorkExperiences)}
								texts={work.description}
								key={index}
							/>
						</>
					)
				)}
			</>)

	} else if (item.category === "MajorScoreRule") {
		return (
			<div className={styles.BodyItemBG}>
				<p>{messages[item.category]}</p>
				<p><img src={checkGreenIcon} alt="checkGreenIcon" /> {item.summaryZH}</p>
			</div>
		)
	}
	else {
		// todo: backend haven't return other category yet
		// deal with other types here later
	}

}

export default BodyContent