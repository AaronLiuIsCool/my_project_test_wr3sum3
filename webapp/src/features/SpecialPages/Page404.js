import React from "react";

import styles from "./Page404.module.css";
import KButton from "./../../components/KButton";
import { useSelector } from 'react-redux';
import { I8nContext } from 'shell/i18n';
import { selectLanguage } from 'app/slicer';

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
            alt="404 page not found"
          />
        </div>
        <div className={styles.content}>
          <img
            src={process.env.PUBLIC_URL + "/images/404.gif"}
            alt="404 page not found"
          />
          <p>{messages.notFoundPageContent}</p>
          <KButton variant="primary" className="w-100">
          {messages.refresh}
          </KButton>
        </div>
      </div>
    </I8nContext.Provider>
  );
}
