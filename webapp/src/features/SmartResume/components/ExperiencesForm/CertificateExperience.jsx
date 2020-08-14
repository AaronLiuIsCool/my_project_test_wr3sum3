import React from 'react';
import { useSelector, useDispatch } from 'react-redux';

import { useI8n } from 'shell/i18n';

import CertificateForm from './CertificateForm';
import Step from '../Step';
import { actions, certificateSelectors } from '../../slicer';

const getForms = (CertificateData, messages) =>
	CertificateData.map((Certificate, index) => (
		<CertificateForm data={Certificate} index={index} isLast={index === Certificate.length} messages={messages} key={`Certificate-${index}`} />
	));

const CertificateExperience = () => {
	const Certificate = useSelector(certificateSelectors.selectCertificate);
	const dispatch = useDispatch();
	const messages = useI8n();
	return (
		<Step
			id="certifications"
			title={messages.certificate}
			subtitle={messages.certificateInfo}
			icon="job.svg"
			addMore={true}
			addMoreMessage={messages.addNewExperience}
			handleAddMore={() => dispatch(actions.addNewCertificate())}>
			{getForms(Certificate.data, messages)}
		</Step>
	);
};

export default CertificateExperience;
