import React from 'react';
// import PropTypes from 'prop-types';
import { Row, Col, Form } from 'react-bootstrap';

import { useI8n } from 'shell/i18n';

import InputGroup from 'components/InputGroup';
import DropdownGroup from 'components/DropdownGroup';
import KButton from 'components/KButton';

import Step from '../Step';

// todo: do propTypes validaton
// BasicExperience.propTypes = {
//   name: PropTypes.string,
//   title: PropTypes.string
// };

const BasicExperience = () => {
  const messages = useI8n();
  return (
    // todo: translate to message string
    <Step
      id="basicInfo"
      title={messages.basicInfo}
      subtitle={messages.enterBasicInfo}
    >
      <Form>
        <Row>
          {/* todo: need an image upload component  */}
        </Row>
        <Row>
          <Col>
            <InputGroup label="中文姓名" id="basic-name-cn" placeholder="请输入您的中文全名" />
          </Col>
          <Col>
            <InputGroup label="英文称呼" id="basic-name-en" placeholder="First & Last Name" />
          </Col>
        </Row>

        <Row>
          <Col>
            <InputGroup label="邮箱" id="basic-email" placeholder="请填写您的邮箱地址" type="email" />
          </Col>
          <Col>
            <InputGroup label="联系电话" id="basic-phone" placeholder="输入您的联系电话" />
          </Col>
        </Row>

        <Row>
          <Col>
            <DropdownGroup label="省份" id="basic-province" placeholder="您现在的所在的省份" />
          </Col>
          <Col>
            <DropdownGroup label="城市" id="basic-city" placeholder="您现在的居住城市" />
          </Col>
          {/* Leave two empty Cols to reserve the space  */}
          <Col></Col>
          <Col></Col>
        </Row>

        <Row>
          <Col>
            <InputGroup label="个人领英   Linkedin 链接" id="basic-linkedin" placeholder="请填写您的Linkedin链接" />
          </Col>
          <Col>
            <InputGroup label="个人 Github 或者作品集链接" id="basic-weblink" placeholder="请填写您的Github链接或作品集链接" />
          </Col>
        </Row>
        <Row className="form_buttons">
          <Col className="space_betweens">
            <KButton variant="link" className="form_reset_link">
              清空重填
          </KButton>
            <KButton variant="primary" type="submit">
              保存
          </KButton>
          </Col>
        </Row>
      </Form>
    </Step>
  )
};

export default BasicExperience;
