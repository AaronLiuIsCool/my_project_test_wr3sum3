import React from 'react';
import { useDispatch } from 'react-redux';
import { actions } from '../../slicer/';
import { useI8n } from 'shell/i18n';
import { dateRangeBuilder } from './common';
import { anyBasicChanges } from '../../slicer/basic';

import styles from '../../styles/ResumePreview.module.css';
import PhotoUploadIcon from '../../assets/photoupload.svg';

const HeaderSection = ({ basicData, educationData }) => {
	const dispatch = useDispatch();
	const messages = useI8n();

	const updateReduxLineNum = () => {
		dispatch(actions.updatelineNum({ value: 5, section: 'header' }));
	};

	const render_HeaderAvatar = () => {
		const avatar = basicData.avatar;
		return <img src={avatar ? avatar : PhotoUploadIcon} alt="avatar" />;
	};
	const render_HeaderTop = () => {
		return (
			<div className={styles.headerContentTop}>
				{basicData.nameCn && <div className={styles.headerContentName}>{basicData.nameCn}</div>}
				{educationData.schoolName && <div className={styles.headerContentSchool}>{educationData.schoolName}</div>}
				{educationData.major && <div className={styles.headerContentDept}>{educationData.major}</div>}
				{(educationData.startDate || educationData.graduateDate) && (
					<div className={styles.headerContentTime}>{dateRangeBuilder(educationData.startDate, educationData.graduateDate)}</div>
				)}
			</div>
		);
	};

	const render_HeaderContact = () => {
		if (basicData.phone || basicData.email || basicData.linkedin) {
			return (
				<div className={styles.headerContentContact}>
					<div className={styles.headerPhone}>{basicData.phone}</div>
					<div className={styles.headerEmail}>{basicData.email}</div>
					<div className={styles.headerLink}>
						{basicData.linkedin && <a href={basicData.linkedin}>{messages.RPreview.linkedinLink}</a>}
						{basicData.weblink && <a href={basicData.weblink}>{messages.RPreview.githubLink}</a>}
					</div>
				</div>
			);
		}
	};

	if (anyBasicChanges(basicData)) {
		updateReduxLineNum();
		return (
			<div className={styles.header}>
				<div className={styles.avatar}>{render_HeaderAvatar()}</div>
				<div className={styles.headerContent}>
					{render_HeaderTop()}
					{render_HeaderContact()}
				</div>
			</div>
		);
	} else {
		return '';
	}
};

export default HeaderSection;
