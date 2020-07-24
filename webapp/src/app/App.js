import React, { useEffect, useState } from 'react';
import { BrowserRouter, Route } from 'react-router-dom';
import { useSelector } from 'react-redux';

import { I8nContext } from 'shell/i18n';
import { selectLanguage } from './slicer';

import Navigation from './components/Navigation';
import SmartResume from 'features/SmartResume';
import Counter from 'features/CounterExample';

import './styles/App.css';

import zh from './i18n/zh.json';
import en from './i18n/en.json';

const App = () => {
  const language = useSelector(selectLanguage);
  const [messages, setMessages] = useState({});

  useEffect(() => {
    setMessages(language === 'zh' ? zh : en);
  }, [language]);

  return (
    <I8nContext.Provider value={messages}>
      <BrowserRouter>
        <div className="App">
          <Navigation />
          <Route exact path="/" component={SmartResume} />
          <Route path="/example" component={Counter} />
        </div>
      </BrowserRouter>
    </I8nContext.Provider>
  );
};

export default App;
