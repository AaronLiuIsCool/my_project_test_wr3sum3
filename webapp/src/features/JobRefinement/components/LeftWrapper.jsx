import React from 'react';
import { useI8n } from 'shell/i18n';
import ScoreRing from 'components/ScoreRing';
import BodyContent from './BodyContent';
import Button from 'react-bootstrap/Button';

import styles from '../styles/leftPanel.module.css';
import checkGreenIcon from '../assets/checkmarkGreen.svg';
import warningIcon from '../assets/warning.svg';

const LeftHeader = ({ messages, score, resumeTitle }) => {
  return (
    <div className={styles.leftHeader}>
      <div>
        <p className={styles.leftHeaderTitle}>
          {messages.refinement_selected_resume} <span>{resumeTitle}</span>
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
    </div>);
}

const LeftBody = ({ messages, navIndex, setNavIndex, refinementData, workExperiences, setWorkExperiences, resumeData, updateResume }) => {
  console.log("rrr", refinementData)
  return (
    <div className={styles.leftBody}>
      <div className={styles.leftBodyNav}>
        {refinementData.map((item, index) =>
          <div className={navIndex === index ? styles.leftBodyNavSelected : ''} onClick={() => setNavIndex(index)} key={index}>
            {messages[item.category]}
            {item.color === "RED" ?  <img src={warningIcon} alt="warningIcon" />: <img src={checkGreenIcon} alt="checkGreenIcon" />}
          </div>
        )}
        <Button variant="primary" onClick={updateResume} className={styles.saveResumeButton} type="submit">
          {messages.save}
        </Button>
      </div>
      <div className={styles.leftBodyContent}>
        {refinementData.map((item, index) => {
          if (navIndex === index) return <BodyContent summary={item?.summaryZH} key={index} item={item} messages={messages} workExperiences={workExperiences} setWorkExperiences={setWorkExperiences} resumeData={resumeData} />
          else return <p key={index}></p>;
        })}
      </div>
    </div >
  );
};

const LeftWrapper = ({ score, resumeData, ...other }) => {
  const messages = useI8n();

  return (
    <>
      {/* todo: resumeTitle better save with resume, or we need to call account service */}
      <LeftHeader messages={messages} score={score} resumeTitle={"todo"} />
      <LeftBody messages={messages} resumeData={resumeData} {...other} />
    </>
  );
};

export default LeftWrapper;
