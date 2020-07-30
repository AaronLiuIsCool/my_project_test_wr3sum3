import React from 'react';
import { useSelector, useDispatch } from 'react-redux';

import { useI8n } from 'shell/i18n';

import EducationForm from './EducationForm';
import Step from '../Step';
import { actions, educationSelectors } from '../../slicer';

const getForms = (educationData, messages) => (
  educationData.map((education, index) => (
    <EducationForm data={education} index={index} isLast={index === education.length}
      messages={messages} key={`education-${index}`} />
  ))
);

const EducationExperience = () => {
  const education = useSelector(educationSelectors.selectEducation);
  const dispatch = useDispatch();
  const messages = useI8n();
  return (
    // todo: translate to message string
    <Step
      id="education"
      title={messages.education}
      subtitle={messages.educationInfo}
      addMore={true}
      addMoreMessage={messages.addNewExperience}
      handleAddMore={() => dispatch(actions.addNewEducation())}
    >
      {getForms(education.data, messages)}
    </Step>
  );
};

export default EducationExperience;
