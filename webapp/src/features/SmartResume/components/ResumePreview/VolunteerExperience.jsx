import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { actions, previewSelectors } from '../../slicer/';
import { useI8n } from 'shell/i18n';
import { dateRangeBuilder, render_Description, render_pageBreaker } from './common';
import { anyVolunteerChanges } from '../../slicer/volunteer';

import styles from '../../styles/ResumePreview.module.css';

const VolunteerExperience = ({ volunteerData }) => {
	let volunteerLineCount = 0;
	const dispatch = useDispatch();
	let { header: headerLineCount } = useSelector(previewSelectors.selectPreview).data;
	let { work: workLineCount } = useSelector(previewSelectors.selectPreview).data;

	const updateLineCountBy = (n) => {
		volunteerLineCount += n;
	};

	useEffect(() => {
		dispatch(actions.updatelineNum({ value: volunteerLineCount, section: 'volunteer' }));
	}, [volunteerData]); // eslint-disable-line react-hooks/exhaustive-deps

	const messages = useI8n();
	if (anyVolunteerChanges(volunteerData)) {
		updateLineCountBy(1);
		return (
			<div className={styles.section}>
				<div className={styles.sectionHeader}>{messages.RPreview.studentWorkAndVolunteer}</div>
				{volunteerData.map((volunteer, index) => (
					<React.Fragment key={`volunteer-${index}`}>
						<div className={styles.infoGroupHeader}>
							<div>
								<span className={styles.infoGroupTitle}>{volunteer.volunteerRole}</span>
								<span className={styles.infoGroupSubtitle}>{volunteer.volunteerCompanyName}</span>
							</div>
							<div className={styles.infoGroupDate}>
								{dateRangeBuilder(volunteer.volunteerStartDate, volunteer.volunteerEndDate)} {volunteer.volunteerCity}{' '}
								{volunteer.volunteerCountry}
							</div>
						</div>
						{updateLineCountBy(1)}
						{render_pageBreaker(volunteerLineCount + workLineCount + headerLineCount)}
						{render_Description(volunteer.volunteerDescription, volunteerLineCount)}
						{updateLineCountBy(volunteer.volunteerDescription.split('\n').length)}
					</React.Fragment>
				))}
			</div>
		);
	} else {
		return '';
	}
};

export default VolunteerExperience;
