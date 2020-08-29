import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { BrowserRouter, Route, Switch  } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';

import moment from 'moment';
import 'moment/locale/zh-cn';

import AuthServices from 'shell/services/AuthServices';
import { I8nContext } from 'shell/i18n';
import { getLogger } from 'shell/logger';
import { selectLanguage, updateAuthInfo } from './slicer';

import PrivateRoute from './components/PrivateRoute';
import Navigation from './components/Navigation';
import ResumeHub from 'features/ResumeHub';
import ResumeStarter from 'features/ResumeStarter';
import SmartResume from 'features/SmartResume';
import JobsMatcher from 'features/JobsMatcher';
import Page404 from 'features/SpecialPages/Page404';

import './styles/index.scss';

import zh from './i18n/zh.json';
import en from './i18n/en.json';


const authServices = new AuthServices();
const logger = getLogger('App');
async function isAuthenticated(dispatch) {
  let authInfo = {}
  try {
    const response = await authServices.findWhoAmI();
    if (response) {
      authInfo = await response.json();
    }
  } catch (exception) {
    logger.error(exception); // TODO: Remove before we go out to prod
  } finally {
    dispatch(updateAuthInfo(authInfo));
  }
  
}
const App = ({ waitForInit = true }) => {
  const [init, setInit] = useState(false);
  const language = useSelector(selectLanguage);
  const dispatch = useDispatch();

  useEffect(() => {
    isAuthenticated(dispatch).then(() => setInit(true));
    const interval = window.setInterval(() => isAuthenticated(dispatch), 60 * 60 * 1000);
    return () => clearInterval(interval);
  }, []); // eslint-disable-line

  let messages = en;
  moment.locale('en');
  if (language === 'zh') {
    messages = zh;
    moment.locale('zh-cn');
  }

  if (waitForInit && !init) {
    return <React.Fragment />;
  }

  return (
    <I8nContext.Provider value={messages}>
      <BrowserRouter>
        <div className="App">
          <Navigation />
          <Switch>
            <PrivateRoute exact path="/" component={ResumeHub} />
            <PrivateRoute exact path="/resume" component={SmartResume} />
            <PrivateRoute path="/resume/new" component={ResumeStarter} />
            <PrivateRoute path="/jobs" component={JobsMatcher} />
            <Route component={Page404} />
            <Route path="/404" component={Page404} />
          </Switch>
        </div>
      </BrowserRouter>
    </I8nContext.Provider>
  );
};

App.propTypes = {
  waitForInit: PropTypes.bool
}

export default App;
