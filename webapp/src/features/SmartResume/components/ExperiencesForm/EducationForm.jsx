import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';
import { Row, Col, Form } from 'react-bootstrap';

import SingleDatePicker from 'components/SingleDatePicker';
import InputGroup from 'components/InputGroup';
import DropdownGroup from 'components/DropdownGroup';
import Button from 'react-bootstrap/Button';

import { adaptEducation } from '../../utils/servicesAdaptor';
import { actions, selectId } from '../../slicer';
import { validateEducation, validateEducationEntry } from '../../slicer/education';
import { updateStatus, updateAllStatus } from '../../slicer/common';
import ResumeServices from 'shell/services/ResumeServices';
import { getLogger } from 'shell/logger';

import degreeOptions from 'data/degree.json';
import majorOptions from 'data/major.json';
import univOptions from 'data/university.json';
import cityOptions from 'data/city.json';

import { previewResume, wholePageCheck } from '../ResumePreview/resumeBuilder';
import { dispatchUpdates, generateEducationRating, generateLayoutRating } from '../../utils/resume';

const logger = getLogger('EducationForm');
const resumeServices = new ResumeServices();
const fields = [
    'schoolName',
    'gpa',
    'startDate',
    'graduateDate',
    'major',
    'degree',
    'city',
    'country',
    'highestAward',
    'otherAward'
];
const EducationForm = ({ data, index, isLast = false, messages }) => {
    const resumeId = useSelector(selectId);
    const [validated, setValidated] = useState(false);
    const [status, setStatus] = useState({
        schoolName: {},
        gpa: {},
        startDate: {},
        graduateDate: {},
        major: {},
        degree: {},
        city: {},
        country: {},
        highestAward: {},
        otherAward: {}
    });
    const dispatch = useDispatch();

	const save = async () => {
		previewResume(messages.RPreview);
        let id = data.id;
        try {
            const response = (id === undefined) ?
                await resumeServices.createEducation(resumeId, adaptEducation(data)) :
                await resumeServices.updateEducation(data.id, adaptEducation(data));
            const responseJson = await response.json();
            id = id || responseJson.id;
        } catch(exception) {
            logger.error(exception);
        } finally {
            dispatch(actions.updateEducationId({ index, id }));
            dispatchUpdates('update-score');
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        event.stopPropagation();
        updateAllStatus(validateEducationEntry, status, setStatus, fields, data)
        if (!validateEducation(data)) {
            setValidated(false);
            return;
        }
        setValidated(true);
        dispatch(actions.completeEducation());
        await save();
        handleEducationFormRatings(data, index, messages)
    };

    const handleSchoolChange = (values) => {
        const value = values.length === 0 ? null : values[0].data
        updateStatus(validateEducationEntry, status, setStatus, 'schoolName', value);
        dispatch(actions.updateSchoolName({value, index}));
    };

    const handleGPAChange = (event) => {
        const value = event.target.value;
        updateStatus(validateEducationEntry, status, setStatus, "gpa", value);
        dispatch(actions.updateGPA({value, index}));
    };

    const handleSchoolStartDateChange = (date) => {
        const value = date ? date.toISOString() : undefined;
        updateStatus(validateEducationEntry, status, setStatus, "startDate", value);
        dispatch(actions.updateStartDate({value, index}))
    };

    const handleGradDateChange = (date) => {
        const value = date ? date.toISOString() : undefined;
        updateStatus(validateEducationEntry, status, setStatus, "graduateDate", value);
        dispatch(actions.updateGraduateDate({value, index}))
    };

    const handleMajorChange = (values) => {
        const value = values.length === 0 ? null : values[0].data
        updateStatus(validateEducationEntry, status, setStatus, "major", value);
        dispatch(actions.updateMajor({value, index}))
    };

    const handleDegreeChange = (values) => {
        const value = values.length === 0 ? null : values[0].data
        updateStatus(validateEducationEntry, status, setStatus, "degree", value);
        dispatch(actions.updateDegree({value, index}))
    };

    const handleCityChange = (values) => {
        const value = values.length === 0 ? null : values[0].data
        updateStatus(validateEducationEntry, status, setStatus, "city", value);
        dispatch(actions.updateEduCity({value, index}));
    };

    const handleCountryChange = (event) => {
        const value = event.target.value;
        updateStatus(validateEducationEntry, status, setStatus, "country", value);
        dispatch(actions.updateEduCountry({value, index}));
    };
    
    const handleEducationFormRatings = async ({gpa, highestAward, otherAward}, index, messages) => {
        const response = await resumeServices.getRatings(resumeId);
        const { educations } = await response.json();
        const schools = educations || [];
        const educationRating = generateEducationRating({gpa, highestAward, otherAward, schools, index}, messages)
        const layoutRating = generateLayoutRating(wholePageCheck(messages.RPreview), messages)
        dispatch(actions.updateLayoutRating(layoutRating))
        dispatch(actions.updateEducationRating({
            index,
            details: educationRating
        }))
    }
    
    return (
        <Form validated={validated} onSubmit={handleSubmit} className={`edu-${index}`}>
            <Row>
                <Col>
                    <h2 className="form_h2">{messages.enterSchoolInfo}</h2>
                </Col>
            </Row>
            <Row>
                <Col lg="4">
                    <DropdownGroup label={messages.schoolName} id="education-school" placeholder={messages.enterSchoolName}
                        options={univOptions} value={data.schoolName}
                        onChange={handleSchoolChange} feedbackMessage={messages.entryIsInvalid}
                        isValid={status.schoolName.isValid} isInvalid={status.schoolName.isInvalid} />
                </Col>
                <Col lg="2">
                    <InputGroup label={messages.graduateGPA} id="education-gpa"
                        placeholder="4.0?" value={data.gpa}
                        onChange={handleGPAChange} feedbackMessage={messages.entryIsInvalid}
                        isValid={status.gpa.isValid} isInvalid={status.gpa.isInvalid} />
                </Col>
                <Col>
                    <SingleDatePicker label={messages.enterSchoolDate} id="education-enter-date"
                        placeholder={messages.yymmdd} value={data.startDate}
                        monthFormat={messages.monthFormat} displayFormat={messages.dateDisplayFormat}
                        onDateChange={handleSchoolStartDateChange} feedbackMessage={messages.entryIsInvalid}
                        isValid={status.startDate.isValid} isInvalid={status.startDate.isInvalid} />
                </Col>
                <Col>
                    <SingleDatePicker label={messages.graduateDate} id="education-graduate-date"
                        placeholder={messages.yymmdd} value={data.graduateDate}
                        monthFormat={messages.monthFormat} displayFormat={messages.dateDisplayFormat}
                        onDateChange={handleGradDateChange} feedbackMessage={messages.entryIsInvalid}
                        isValid={status.graduateDate.isValid} isInvalid={status.graduateDate.isInvalid} />
                </Col>
            </Row>

            <Row>
                <Col>
                    <DropdownGroup label={messages.major} id="education-major" placeholder={messages.enterMajor}
                         options={majorOptions} value={data.major}
                        onChange={handleMajorChange} feedbackMessage={messages.entryIsInvalid}
                        isValid={status.major.isValid} isInvalid={status.major.isInvalid} />
                </Col>
                <Col>
                    <DropdownGroup label={messages.degree} id="education-degree" placeholder={messages.enterDegree}
                        options={degreeOptions} value={data.degree}
                        onChange={handleDegreeChange} feedbackMessage={messages.entryIsInvalid}
                        isValid={status.degree.isValid} isInvalid={status.degree.isInvalid} />
                </Col>
                <Col>
                    <DropdownGroup label={messages.schoolCity} id="education-school-city" placeholder={messages.schoolCity}
                        options={cityOptions} value={data.city}
                        onChange={handleCityChange} feedbackMessage={messages.entryIsInvalid}
                        isValid={status.city.isValid} isInvalid={status.city.isInvalid} />
                </Col>
                <Col>
                    <InputGroup label={messages.schoolCountry} id="education-school-country"
                        placeholder={messages.schoolCountry} value={data.country}
                        onChange={handleCountryChange} feedbackMessage={messages.entryIsInvalid}
                        isValid={status.country.isValid} isInvalid={status.country.isInvalid} />
                </Col>
            </Row>

            <Row>
                <Col>
                    <InputGroup label={messages.highestAward} id="education-highest-award"
                        placeholder={messages.enterHighestAward} value={data.highestAward}
                        onChange={event => dispatch(actions.updateHighestAward({value: event.target.value, index}))}
                        isValid={status.highestAward.isValid} isInvalid={status.highestAward.isInvalid} />
                </Col>
                <Col>
                    <InputGroup label={messages.otherAward} id="education-other-award"
                        placeholder={messages.enterOtherAward} value={data.otherAward}
                        onChange={event => dispatch(actions.updateOtherAward({value: event.target.value, index}))}
                        isValid={status.otherAward.isValid} isInvalid={status.otherAward.isInvalid} />
                </Col>
            </Row>
            <Row className="form_buttons">
                <Col className="space_betweens">
                    {/* just a placeholder so we do need to change the css */}
                    <p className="hidden"></p>
                    <Button variant="primary" type="submit">
                        {messages.save}
                    </Button>
                </Col>
            </Row>
        </Form>
    )
};

EducationForm.propTypes = {
    data: PropTypes.object.isRequired,
    index: PropTypes.number.isRequired,
    isLast: PropTypes.bool,
    messages: PropTypes.object.isRequired
};

export default EducationForm;
