import React from "react";

import styles from "./Page404.module.css";
import Button from 'react-bootstrap/Button';
import { useSelector } from 'react-redux';
import { I8nContext } from 'shell/i18n';
import { selectLanguage } from 'features/App/slicer';

import zh from './i18n/zh.json';
import en from './i18n/en.json';

export default function Page404() {
  const language = useSelector(selectLanguage);
  const messages = language === 'zh' ? zh : en;
  return (
    <I8nContext.Provider value={messages}>
      <div className={styles.page404}>
        <div className={styles.header}>
          <img
            src={process.env.PUBLIC_URL + "/images/logo.png"}
            alt="logo"
          />
        </div>
        <div className={styles.content}>
          <img
            src={process.env.PUBLIC_URL + "/images/404.gif"}
            alt="404 page not found"
          />
          <p>{messages.notFoundPageContent}</p>
          <Button variant="primary" className="w-100">
            {messages.refresh}
          </Button>
        </div>
      </div>
    </I8nContext.Provider>
  );
}
