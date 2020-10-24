import React from 'react';
import { useI8n } from 'shell/i18n';

import styles from '../../../styles/JobRefinementModal.module.css';

const JROverWriteResume = ({stepHandler}) => {
  const messages = useI8n();
  return (
    <div className={styles.step_sm_container}>
      <div className={styles.h1}>
        {messages['refinement_allow_overwrite']}
      </div>
      <div className={styles.options_container}>
        
        <div className={styles.option} onClick={()=>stepHandler(2)}>
          {messages['refinement_no_new_one']}
        </div>
        <div className={styles.option} onClick={()=>stepHandler(3)}>
          {messages['refinement_yes_overwrite']}
        </div>
        
      </div>
    </div>
  );
}

export default JROverWriteResume;

