import React from 'react';
import { Row } from 'react-bootstrap';
import PhotoUploadIcon from '../../assets/photoupload.svg';
import moment from 'moment';
import './index.scss';
import { useI8n } from 'shell/i18n';

const JDBC_DATE_FORMAT = 'YYYY-MM-DD';
export const Summary = ({
  type = '',
  handleClickCallback = '',
  name = '',
  avatar = '',
  schoolName = '',
  enterSchoolDate = '',
  graduateDate = '',
  major = '',
  degree = '',
  roleName = '',
  startDate = '',
  endDate = '',
  companyName = '',
  currentFlag = '',
}) => {
  const messages = useI8n();
  switch (type) {
    case 'BasicInfo':
      return (
        <Row className="flexie collapsed-section">
          <div className="left-section">
            <div className="image-wrapper">
              <img src={avatar ? avatar : PhotoUploadIcon} alt="avatar" />
            </div>
            <h2>{name}</h2>
          </div>
          <div className="toggle-up-arrow" onClick={handleClickCallback}>
            <img
              src={require('../../assets/arrow-down.svg')}
              alt="arrow-down"
            />
          </div>
        </Row>
      );
    case 'EducationInfo':
      return (
        <Row className="flexie collapsed-section">
          <div className="left-section">
            <h3>{major}</h3>
            <div className="lower-row">
              <span className="blue">
                {schoolName} | {degree}
              </span>
              <span>
                {moment(new Date(enterSchoolDate)).format(JDBC_DATE_FORMAT)} -{' '}
                {moment(new Date(graduateDate)).format(JDBC_DATE_FORMAT)}
              </span>
            </div>
          </div>
          <div className="toggle-up-arrow" onClick={handleClickCallback}>
            <img
              src={require('../../assets/arrow-down.svg')}
              alt="arrow-down"
            />
          </div>
        </Row>
      );
    case 'WorkInfo':
    case 'ProjectInfo':
    case 'VolunteerInfo':
      return (
        <Row className="flexie collapsed-section">
          <div className="left-section">
            <h3>{roleName}</h3>
            <div className="lower-row">
              <span className="blue">{companyName}</span>
              <span>
                {moment(new Date(startDate)).format(JDBC_DATE_FORMAT)} -{' '}
                {!currentFlag
                  ? messages.present
                  : moment(new Date(endDate)).format(JDBC_DATE_FORMAT)}
              </span>
            </div>
          </div>
          <div className="toggle-up-arrow" onClick={handleClickCallback}>
            <img
              src={require('../../assets/arrow-down.svg')}
              alt="arrow-down"
            />
          </div>
        </Row>
      );
    case 'CertificateInfo':
      return (
        <Row className="flexie collapsed-section">
          <div className="left-section">
            <h3>{roleName}</h3>
            <div className="lower-row">
              <span className="blue">
              {!currentFlag
                  ? messages.forever
                  : messages.valid_through + moment(new Date(endDate)).format(JDBC_DATE_FORMAT)}
              </span>
            </div>
          </div>
          <div className="toggle-up-arrow" onClick={handleClickCallback}>
            <img
              src={require('../../assets/arrow-down.svg')}
              alt="arrow-down"
            />
          </div>
        </Row>
      );
    default:
      return (
        <Row className="flexie collapsed-section">
          <div>{type}</div>
          <div className="toggle-up-arrow" onClick={handleClickCallback}>
            <img
              src={require('../../assets/arrow-down.svg')}
              alt="arrow-down"
            />
          </div>
        </Row>
      );
  }
};
