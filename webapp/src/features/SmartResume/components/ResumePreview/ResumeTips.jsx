import React, { useState } from 'react';
import { useI8n } from 'shell/i18n';

import {Accordion, Card, Button} from 'react-bootstrap';
import styles from '../../styles/ResumeTips.module.css';

import resumeTipsData from 'data/resume-tips.json';

const ResumeTips = () => {
  const messages = useI8n();
  const [optionsState, setOptionsState] = useState(Object.keys(resumeTipsData)[0]);
  

  return (
    <div className={styles.container}>
      <div>
        <div className={styles.header}>{messages.resumeTips.resumeTips}</div>
        <div className={styles.dropdown}>
          <select value={optionsState} onChange={(e) => setOptionsState(e.target.value)}>
            {Object.keys(resumeTipsData).map((type, index) => (
              <option key={index}>{type}</option>
            ))}
          </select>
        </div>
        <Accordion>
          {
            resumeTipsData[optionsState].map((item, index) => (
              <Card className={styles.card}>
                <Card.Header className={styles.cardHader}>
                  <Accordion.Toggle as={Button} variant="link" eventKey={index+1}>
                    {item.q}
                  </Accordion.Toggle>
                </Card.Header>
                <Accordion.Collapse eventKey={index+1}>
                  <Card.Body className={styles.cardBody}>{item.a}</Card.Body>
                </Accordion.Collapse>
              </Card>
            ))
          }
      </Accordion>
      </div>
    </div>
  );
};

export default ResumeTips;
