import React, { useState } from 'react';
import { useI8n } from 'shell/i18n';

import DetailHeader from '../JobDetails/DetailHeader';
import DetailContent from '../JobDetails/DetailContent';
import ScoreRing from '../JobDetails/ScoreRing';

import styles from '../../../styles/JobRefinement.module.css';
import checkGreenIcon from '../../../assets/checkmarkGreen.svg';

const renderLeftHead = (messages, score) => (
	<div className={styles.leftHeader}>
		<div>
			<p className={styles.leftHeaderTitle}>
				{messages.refinement_selected_resume} <span>{messages.refinement_finance}</span>
			</p>
			<p>{messages.refinement_selected_resume_hint}</p>
		</div>
		<div className={styles.score_container}>
			<p>{messages.job_matching_score}</p>
			<div className={styles['score-icon']}>
				<ScoreRing stroke={2} radius={24} progress={score} color='#edbc4d' />
				<div className={styles['score']}>{score}</div>
			</div>
		</div>
	</div>
);

const renderBodyContent = () => {
  // this is just some css sample 
  return (
    <>
      <div className={styles.BodyContentTitle}>专业匹配度</div>
      <div className={styles.BodyItemBG}>
        <p>商业营销专业</p>
        <p><img src={checkGreenIcon} alt="checkGreenIcon"/> 非常吻合</p>
      </div>
      <div className={styles.BodyItem}>
        <p>其他学历</p>
        <p style={{color:"#333"}}>本科</p>
      </div>
      <div className={styles.BodyContentTitle}>关键词匹配度</div>
      <div className={styles.BodyItemUnderline}>
        <img src={checkGreenIcon} alt="checkGreenIcon"/> 简历中已用
      </div>
      <div className={styles.BodyItemUnderKeywords}>
        <span>财务报表</span>
        <span>Excel</span>
        <span>SQL</span>
      </div>
    </>
  )
}

const renderLeftBody = (messages, navIndex, setNavIndex) => {
	return (
		<div className={styles.leftBody}>
			<div className={styles.leftBodyNav}>
				<div className={navIndex === 1 ? styles.leftBodyNavSelected : ''} onClick={()=>setNavIndex(0)}>{messages.refinement_major_match}</div>
				<div className={navIndex === 2 ? styles.leftBodyNavSelected : ''} onClick={()=>setNavIndex(1)}>{messages.refinement_education_match}</div>
				<div className={navIndex === 3 ? styles.leftBodyNavSelected : ''} onClick={()=>setNavIndex(2)}>{messages.refinement_keyword_match}</div>
				<div className={navIndex === 4 ? styles.leftBodyNavSelected : ''} onClick={()=>setNavIndex(3)}>{messages.refinement_experience_match}</div>
				<div className={navIndex === 5 ? styles.leftBodyNavSelected : ''} onClick={()=>setNavIndex(4)}>{messages.refinement_certificate_match}</div>
			</div>
			<div className={styles.leftBodyContent}>
        {navIndex === 1 && renderBodyContent()}
        {navIndex === 2 && <div>skip....</div>}
        {navIndex === 3 && <div>skip....</div>}
			</div>
		</div>
	);
};

const JRResumerDetails = ({ data, stepHandler }) => {
	const messages = useI8n();
	const score = 78; // todo: need to be dynamic
	const [navIndex, setNavIndex] = useState(1);

	return (
		<div className={styles.step_lg_container}>
			<div className={styles.left}>
				{renderLeftHead(messages, score)}
				{renderLeftBody(messages, navIndex, setNavIndex)}
			</div>
			<div className={styles.right}>
				<DetailHeader data={data} showExtraInfo={false} />
				<DetailContent data={data} showExtraInfo={false} />
			</div>
		</div>
	);
};

export default JRResumerDetails;
