import React from 'react';
import { useSelector } from 'react-redux';

import { I8nContext } from 'shell/i18n';
import { selectLanguage } from 'app/slicer';

import LeftNav from './LeftNav';
import ExperiencesForm from './ExperiencesForm';
import Preview from './Preview';

import zh from '../i18n/zh.json';
import en from '../i18n/en.json';

const SmartResume = () => {
  const language = useSelector(selectLanguage);
  const messages = language === 'zh' ? zh : en;

  return (
    <I8nContext.Provider value={messages}>
      <div>
        <div>{messages['title']}</div>
        <LeftNav />
        <ExperiencesForm />
        <Preview />
      </div>
    </I8nContext.Provider>
  );
};

export default SmartResume;