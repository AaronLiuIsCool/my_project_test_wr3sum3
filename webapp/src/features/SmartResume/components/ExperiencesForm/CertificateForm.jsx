import React, { useState, useEffect, useRef } from 'react';
import { Summary } from '../Summary';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';
import { Row, Col, Form } from 'react-bootstrap';

import SingleDatePicker from 'components/SingleDatePicker';
import RadioButtonGroup from 'components/RadioButtonGroup';
import Button from 'react-bootstrap/Button';
import DropdownGroup from 'components/DropdownGroup';

import { adaptCertificate } from '../../utils/servicesAdaptor';
import { actions, selectId } from '../../slicer';
import { validateCertificate, validateCertificateEntry } from '../../slicer/certificate';
import { updateStatus } from '../../slicer/common';
import ResumeServices from 'shell/services/ResumeServices';
import { getLogger } from 'shell/logger';
import { generateCertificeRating } from '../../utils/resume';

import certificateOptions from 'data/certificate.json';

const logger = getLogger('CertificateForm');
const resumeServices = new ResumeServices();

const CertificateForm = ({ data, index, isLast = false, messages, certData = [] }) => {
	const resumeId = useSelector(selectId);
	const [validated, setValidated] = useState(false);
	const [status, setStatus] = useState({
		certificateName: {},
		certificateIssuedDate: {},
		certificateEndDate: {},
	});
	const dispatch = useDispatch();
    
    const handleCertificateFormRating = () => {
        const certRating = generateCertificeRating(certData.length, messages);
        dispatch(actions.updateCertificateRating({details: certRating}))
    }
    useEffect(() => {
        handleCertificateFormRating()
        // eslint-disable-next-line
    }, []);
    
	const save = async () => {
		let id = data.id;
		try {
			const response =
				data.id === undefined
					? await resumeServices.createCertificate(resumeId, adaptCertificate(data))
					: await resumeServices.updateCertificate(data.id, adaptCertificate(data));
			const responseJson = await response.json();
			id = id || responseJson.id;
		} catch (exception) {
			logger.error(exception);
		} finally {
			dispatch(actions.updateCertificateId({ index, id }));
		}
	};

	const handleSubmit = (event) => {
        handleCertificateFormRating();
		event.preventDefault();
		event.stopPropagation();
		if (!validateCertificate(data)) {
			setValidated(false);
			return;
		}
		toggleShowSummary();
		setValidated(true);
        dispatch(actions.completeCertificates());
        save();
        handleCertificateFormRating()
	};

	const handleCertificateChange = (values) => {
		const value = values.length === 0 ? null : values[0].data;
		updateStatus(validateCertificateEntry, status, setStatus, 'certificateName', value);
		dispatch(actions.updateCertificateName({ value, index }));
	};

	const handleValidCertificateFlag = (event) => {
		event.preventDefault();
		const value = event.target.value;
		dispatch(actions.updateCurrentCertificateFlag({ value, index }));
	};

	const handleCertificateStartDateChange = (date) => {
		const value = date ? date.toISOString() : undefined;
		updateStatus(validateCertificateEntry, status, setStatus, 'certificateIssuedDate', value);
		dispatch(actions.updateCertificateIssuedDate({ value, index }));
	};

	const handleCertificateEndDateChange = (date) => {
		const value = date ? date.toISOString() : undefined;
		updateStatus(validateCertificateEntry, status, setStatus, 'certificateEndDate', value);
		dispatch(actions.updateCertificateEndDate({ value, index }));
	};
	const didMount = useRef(false);
	const [showSummary, setShowSummary] = useState(false);
	const toggleShowSummary = () => {
		setShowSummary(!showSummary);
	};
	const handleDelete = (id) => {
		resumeServices.removeCertificate(id, resumeId)
		dispatch(actions.removeCertificate({index}))
	};
	useEffect(() => {
		if(data.id && !didMount.current) {
			setShowSummary(true)
			didMount.current = true
		}
	}, [data])

	return (
    <div className="form_body">
      {showSummary ? (
        <Summary
          type={'CertificateInfo'}
          handleClickCallback={toggleShowSummary}
          roleName={data.certificateName}
          startDate={data.certificateIssuedDate}
          endDate={data.certificateEndDate}
          currentFlag={data.validCertificateFlag}
        />
      ) : (
        <Form validated={validated} onSubmit={handleSubmit}>
					<Row className="flexie">
						<Col>
							<h2 className="form_h2">{messages.enterNewExperience}</h2>
						</Col>
						<div className="toggle-up-arrow" onClick={toggleShowSummary}>
							{data.id && <img src={require('../../assets/arrow-up.svg')} alt="up-arrow" />}
						</div>
          </Row>
          <Row>
            <Col lg="4">
              <DropdownGroup
                label={messages.certificateName}
                id="certificate-name"
                placeholder={messages.enterCertificateName}
                options={certificateOptions}
                value={data.certificateName}
                onChange={handleCertificateChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.certificateName.isValid}
                isInvalid={status.certificateName.isInvalid}
              />
            </Col>
            <Col lg="2">
              <RadioButtonGroup
                label={messages.validForever}
                id="certificate-currentCertificateFlag"
                values={[
                  { label: messages.yes, value: true },
                  { label: messages.no, value: false },
                ]}
                value={data.validCertificateFlag}
                onClickHandler={handleValidCertificateFlag}
              />
            </Col>

            <Col>
              <SingleDatePicker
                label={messages.issueDate}
                id="certificate-start-date"
                placeholder={messages.yymmdd}
                value={data.certificateIssuedDate}
                monthFormat={messages.monthFormat}
                displayFormat={messages.dateDisplayFormat}
                onDateChange={handleCertificateStartDateChange}
                feedbackMessage={messages.entryIsInvalid}
                isValid={status.certificateIssuedDate.isValid}
                isInvalid={status.certificateIssuedDate.isInvalid}
              />
            </Col>

            <Col>
              {!data.validCertificateFlag && (
                <SingleDatePicker
                  label={messages.expireDate}
                  id="certificate-end-date"
                  placeholder={messages.yymmdd}
                  value={data.certificateEndDate}
                  monthFormat={messages.monthFormat}
                  displayFormat={messages.dateDisplayFormat}
                  onDateChange={handleCertificateEndDateChange}
                  feedbackMessage={messages.entryIsInvalid}
                  isValid={status.certificateEndDate.isValid}
                  isInvalid={status.certificateEndDate.isInvalid}
                />
              )}
            </Col>
          </Row>

          <Row className="form_buttons">
						<Col className="flex-end">
              {/* just a placeholder so we do need to change the css */}
              {data.id && (
                <Button
                  onClick={() => {
                    handleDelete(data.id);
                  }}
                  variant="light"
                  className="remove-btn"
                >
                  {messages.delete}
                </Button>
              )}
              <Button variant="primary" type="submit">
                {messages.save}
              </Button>
            </Col>
          </Row>
        </Form>
      )}
    </div>
  );
};

CertificateForm.propTypes = {
	data: PropTypes.object.isRequired,
	index: PropTypes.number.isRequired,
	isLast: PropTypes.bool,
	messages: PropTypes.object.isRequired,
};

export default CertificateForm;
