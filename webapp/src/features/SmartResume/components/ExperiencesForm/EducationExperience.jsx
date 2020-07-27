import React from 'react';
// import PropTypes from 'prop-types';
import { Row, Col, Form } from 'react-bootstrap';

import { useI8n } from 'shell/i18n';

import InputGroup from 'components/InputGroup';
import DropdownGroup from 'components/DropdownGroup';
import KButton from 'components/KButton';

// json data for dropdowns
import degreeOptions from '../../../../data/degree.json';
import majorOptions from '../../../../data/major.json';
import univOptions from '../../../../data/university.json';
import cityOptions from '../../../../data/city.json';

import Step from '../Step';


// todo: do propTypes validaton
// BasicExperience.propTypes = {
//   name: PropTypes.string,
//   title: PropTypes.string
// };

const EducationExperience = () => {
  const messages = useI8n();
  return (
    // todo: translate to message string
    <Step
      id="education"
      title={messages.education}
      subtitle={messages.educationInfo}
      addMore={true}
    >
      <Form>
        <Row>
          <Col>
            <h2 class="form_h2">{messages.enterSchoolInfo}</h2>
          </Col>
        </Row>
        <Row>
          <Col lg="4">
            <DropdownGroup label={messages.schoolName} id="education-school" placeholder={messages.enterSchoolName} searchKey="u" options={univOptions}/>
          </Col>
          <Col lg="2">
            <InputGroup label={messages.graduateGPA} id="education-gpa" placeholder="4.0?" />
          </Col>
          <Col>
            {/* todo: Convert this to airbnb calendar */}
            <InputGroup label={messages.enterSchoolDate} id="education-enter-date" placeholder={messages.yymmdd} />
          </Col>
          <Col>
            {/* todo: Convert this to airbnb calendar */}
            <InputGroup label={messages.graduateDate} id="education-graduate-date" placeholder={messages.yymmdd} />
          </Col>
        </Row>

        <Row>
          <Col>
            <DropdownGroup label={messages.major} id="education-major" placeholder={messages.enterMajor} searchKey="city" options={cityOptions}/>
          </Col>
          <Col>
            <DropdownGroup label={messages.degree} id="education-degree" placeholder={messages.enterDegree} searchKey="degree" options={degreeOptions} />
          </Col>
          <Col>
            <DropdownGroup label={messages.schoolCity} id="education-school-city" placeholder={messages.schoolCity} searchKey="major" options={majorOptions}/>
          </Col>
          <Col>
            <InputGroup label={messages.schoolCountry} id="education-school-country" placeholder={messages.schoolCountry} />
          </Col>
        </Row>

        <Row>
          <Col>
            <InputGroup label={messages.highestAward} id="education-highest-award" placeholder={messages.enterHighestAward} />
          </Col>
          <Col>
            <InputGroup label={messages.otherAward} id="education-other-award" placeholder={messages.enterOtherAward} />
          </Col>
        </Row>
        <Row className="form_buttons">
          <Col className="space_betweens">
            {/* just a placeholder so we do need to change the css */}
            <p className="hidden"></p>
            <KButton variant="primary" type="submit">
            {messages.save}
          </KButton>
          </Col>
        </Row>
      </Form>
    </Step>
  );
};

export default EducationExperience;
