import React, {useState, useEffect} from 'react';

import {useSelector} from 'react-redux';
import {I8nContext} from 'shell/i18n';
import {selectLanguage, selectUserId} from 'features/App/slicer';
import zh from '../i18n/zh.json';
import en from '../i18n/en.json';

import AccManagementCard from './AccManagementCard';
import BasicInfoCard from './BasicInfoCard';

import AccountServices from 'shell/services/AccountServices';

import {ToggleButton, ToggleButtonGroup} from 'react-bootstrap';


import './AccountManagement.scss';
const accountServices = new AccountServices();

const getAccountInfo = async (userId) => {
  const res = await accountServices.getAccountInfo(userId);
  return res.account;
};

const AccountManagement = () => {
  const language = useSelector(selectLanguage);
  const messages = language === 'zh' ? zh : en;
  const userId = useSelector(selectUserId);
  const [visibleCard, setVisibleCard] = useState('basic');

  const [userData, setUserData] = useState({
    confirmedAndActive: true,
    email: '',
    id: '',
    loginType: '',
    memberSince: '',
    name: '',
    openid: null,
    photoUrl: '',
    resumes: [],
    support: true,
  });

  const handleToggleButtonChange = (e) => {
    setVisibleCard(e);
  };

  const handleSave = (name) => {
    accountServices
      .updateAccountInfo({
        ...userData,
        name,
      })
      .then((res) => {
        // console.log(res);
      })
      .catch((err) => {
        // console.log(err);
      });
  };

  const handleRequestPasswordReset = (email) => {
    // if the email address is null
    if (email === '') {
      alert(
        'You must have a valid email address to receive the password reset email. Please bind your email address first.',
      );
    }
    accountServices
      .passwordResetRequest({
        email,
      })
      .then(async (res) => {
        const result = await res.json();
        if (result.code === 'SUCCESS') {
          window.alert(
            'Password reset link has been sent to your email address!',
          );
        }
      })
      .catch((err) => {
        console.error(err);
      });
  };

  const handleBindEmailAddress = (email, userId) => {
    accountServices
      .emailChangeRequest({email, userId})
      .then(async (res) => {
        const result = await res.json();
        if (result.code === 'SUCCESS') {
          window.alert('Email has been sent to your email address!');
        }
      })
      .catch((err) => {
        console.error(err);
      });
  };

  useEffect(() => {
    getAccountInfo(userId).then((res) => {
      console.log(res);
      setUserData(res);
    });
    // eslint-disable-next-line
  }, []);

  return (
    <I8nContext.Provider value={messages}>
      <div className="features account-management">
        <h1>{messages.heading}</h1>
        <div className="content-container">
          <div className="left-container">
            <ToggleButtonGroup
              vertical={true}
              type="radio"
              name="collectio-select"
              defaultValue="basic"
              onChange={handleToggleButtonChange}
            >
              <ToggleButton className="basic" value={'basic'}>
                {messages['basicInfo']}
              </ToggleButton>
              <ToggleButton className="acc" value={'acc'}>
                {messages['accountManagement']}
              </ToggleButton>
            </ToggleButtonGroup>
          </div>
          <div className="right-container">
            {visibleCard === 'acc' ? (
              <AccManagementCard
                userData={userData}
                messages={messages}
                handleRequestPasswordReset={handleRequestPasswordReset}
                handleBindEmailAddress={handleBindEmailAddress}
              />
            ) : (
              <BasicInfoCard
                userData={userData}
                messages={messages}
                handleSave={handleSave}
              />
            )}
          </div>
        </div>
      </div>
    </I8nContext.Provider>
  );
};

export default AccountManagement;
