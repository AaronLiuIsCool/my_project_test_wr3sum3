import React from "react";

import { useSelector } from 'react-redux';
import { I8nContext } from 'shell/i18n';
import { selectLanguage } from 'features/App/slicer';
import ErrorPage from 'components/ErrorPage';

import zh from './i18n/zh.json';
import en from './i18n/en.json';

export default function Page500() {
  const language = useSelector(selectLanguage);
  const messages = language === 'zh' ? zh : en;
  return (
    <I8nContext.Provider value={messages}>
      <ErrorPage />
    </I8nContext.Provider>
  );
}
