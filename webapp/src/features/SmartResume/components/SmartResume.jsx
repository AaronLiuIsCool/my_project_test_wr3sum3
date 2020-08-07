import React from 'react';
import PropTypes from 'prop-types';
import { useSelector } from 'react-redux';

import { I8nContext } from 'shell/i18n';
import { selectLanguage } from 'features/App/slicer';

import LeftNav from './LeftNav';
import ExperiencesForm from './ExperiencesForm';
import Preview from './Preview';

import zh from '../i18n/zh.json';
import en from '../i18n/en.json';

const SmartResume = ({ useObserver = false }) => {
  const language = useSelector(selectLanguage);
  const messages = language === 'zh' ? zh : en;

  return (
    <I8nContext.Provider value={messages}>
      <div className="features smart-resume">
        <div className="overlay">
          <LeftNav />
          <Preview />
        </div>
        <ExperiencesForm useObserver={useObserver} />
      </div>
    </I8nContext.Provider>
  );
};

SmartResume.propTypes = {
  useObserver: PropTypes.bool
};

export default SmartResume;