import React, {useMemo} from 'react';
import { useSelector, useDispatch } from 'react-redux';

import { useI8n } from 'shell/i18n';

import CertificateForm from './CertificateForm';
import Step from '../Step';
import { actions, certificateSelectors, resumeBuilderSelectors } from '../../slicer';

const getForms = (CertificateData, messages, resumeLanguage) =>
	CertificateData.map((Certificate, index) => (
		<CertificateForm certData={CertificateData} data={Certificate} index={index} isLast={index === Certificate.length} messages={messages} key={`Certificate-${index}`} resumeLanguage={resumeLanguage} />
	));

const CertificateExperience = () => {
	const Certificate = useSelector(certificateSelectors.selectCertificate);
	const dispatch = useDispatch();
	const messages = useI8n();
	const { language:resumeLanguage } = useSelector(resumeBuilderSelectors.selectResumeBuilder).data;
	const showAddButton = useMemo(() => {
		return Certificate.data.every(item => item.id)
	}, [
		Certificate.data
	])
	return (
		<Step
			showAddButton={showAddButton}
      id="certifications"
      title={messages.certificate}
      subtitle={messages.certificateInfo}
      icon="job.svg"
      addMore={true}
      addMoreMessage={messages.addNewCertificate}
      handleAddMore={() => dispatch(actions.addNewCertificate())}
    >
      {getForms(Certificate.data, messages, resumeLanguage)}
    </Step>
  );
};

export default CertificateExperience;
