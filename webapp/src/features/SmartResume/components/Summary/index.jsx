import React from 'react';
import {Row} from 'react-bootstrap';
import PhotoUploadIcon from '../../assets/photoupload.svg';
import moment from 'moment';
import './index.scss';
import {useI8n} from 'shell/i18n';

import ArrowDown from '../../assets/arrow-down.png';
import ArrowDownActive from '../../assets/arrow-down-active.png';

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
            <>
              <img className="hide-on-hover" src={ArrowDown} alt="up-arrow" />
              <img
                className="display-on-hover"
                src={ArrowDownActive}
                alt="up-arrow-active"
              />
            </>
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
                {moment(enterSchoolDate).format(JDBC_DATE_FORMAT)} -{' '}
                {moment(graduateDate).format(JDBC_DATE_FORMAT)}
              </span>
            </div>
          </div>
          <div className="toggle-up-arrow" onClick={handleClickCallback}>
            <>
              <img className="hide-on-hover" src={ArrowDown} alt="up-arrow" />
              <img
                className="display-on-hover"
                src={ArrowDownActive}
                alt="up-arrow-active"
              />
            </>
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
                {moment(startDate).format(JDBC_DATE_FORMAT)} -{' '}
                {!currentFlag
                  ? messages.present
                  : moment(endDate).format(JDBC_DATE_FORMAT)}
              </span>
            </div>
          </div>
          <div className="toggle-up-arrow" onClick={handleClickCallback}>
            <>
              <img className="hide-on-hover" src={ArrowDown} alt="up-arrow" />
              <img
                className="display-on-hover"
                src={ArrowDownActive}
                alt="up-arrow-active"
              />
            </>
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
                  : messages.valid_through +
                    moment(endDate).format(JDBC_DATE_FORMAT)}
              </span>
            </div>
          </div>
          <div className="toggle-up-arrow" onClick={handleClickCallback}>
            <>
              <img className="hide-on-hover" src={ArrowDown} alt="up-arrow" />
              <img
                className="display-on-hover"
                src={ArrowDownActive}
                alt="up-arrow-active"
              />
            </>
          </div>
        </Row>
      );
    default:
      return (
        <Row className="flexie collapsed-section">
          <div>{type}</div>
          <div className="toggle-up-arrow" onClick={handleClickCallback}>
            <>
              <img className="hide-on-hover" src={ArrowDown} alt="up-arrow" />
              <img
                className="display-on-hover"
                src={ArrowDownActive}
                alt="up-arrow-active"
              />
            </>
          </div>
        </Row>
      );
  }
};
