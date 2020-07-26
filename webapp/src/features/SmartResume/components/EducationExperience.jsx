import React from 'react';
// import PropTypes from 'prop-types';
import { Container, Row, Col, Form } from 'react-bootstrap';

import { useI8n } from 'shell/i18n';

import InputGroup from 'components/InputGroup';
import DropdownGroup from 'components/DropdownGroup';
import KButton from 'components/KButton';
import AddMoreForm from 'components/AddMoreForm';

// json data for dropdowns
import degreeOptions from '../../../data/degree.json';
import majorOptions from '../../../data/major.json';
import univOptions from '../../../data/university.json';

// todo: do propTypes validaton
// BasicExperience.propTypes = {
//   name: PropTypes.string,
//   title: PropTypes.string
// };

const EducationExperience = () => {
	const messages = useI8n();
	return (
		// todo: translate to message string
		<div>
			<div className="form_h1">
				<div>
					{/* todo: need an icon  */}
					<h1>{messages.education}</h1>
				</div>
				<p>{messages.educationInfo}</p>
			</div>

			<div className="form_body">
				<Container>
					<Form>
						<Row>
							<Col>
								<h2 className="form_h2">填写院校信息</h2>
							</Col>
						</Row>
						<Row>
							<Col lg="4">
								<DropdownGroup label="院校名称" id="education-school" placeholder="请输入学校名字" searchKey="u" options={univOptions}/>
							</Col>
							<Col lg="2">
								<InputGroup label="毕业GPA" id="education-gpa" placeholder="4.0?" />
							</Col>
							<Col>
								{/* todo: Convert this to airbnb calendar */}
								<InputGroup label="入学日期" id="education-enter-date" placeholder="年/月/日期" />
							</Col>
							<Col>
								{/* todo: Convert this to airbnb calendar */}
								<InputGroup label="毕业GPA" id="education-graduate-date" placeholder="年/月/日期" />
							</Col>
						</Row>

						<Row>
							<Col>
								<DropdownGroup label="专业" id="education-major" placeholder="输入所在的专业" searchKey="major" options={majorOptions}/>
							</Col>
							<Col>
								<DropdownGroup label="学历" id="education-degree" placeholder="获得的最高学历" searchKey="degree" options={degreeOptions} />
							</Col>
							<Col>
								<InputGroup label="学校所在国家" id="education-school-country" placeholder="学院所在国家" />
							</Col>
							<Col>
								<InputGroup label="毕业GPA" id="education-graduate-date" placeholder="年/月/日期" />
							</Col>
						</Row>

						<Row>
							<Col>
								<InputGroup
									label="所获最高奖项 (有任何奖项都是加分好事！)"
									id="education-highest-award"
									placeholder="请填写您在校期间所获得的最高奖项"
								/>
							</Col>
							<Col>
								<InputGroup label="其他获得奖项" id="education-other-award" placeholder="请填写您在校期间所获得的其他奖项" />
							</Col>
						</Row>
						<Row className="form_buttons">
							<Col className="space_betweens">
								{/* just a placeholder so we do need to change the css */}
								<p className="hidden"></p>
								<KButton variant="primary" type="submit">
									保存
								</KButton>
							</Col>
						</Row>
					</Form>
				</Container>
			</div>
			<AddMoreForm>添加新经历</AddMoreForm>
		</div>
	);
};

export default EducationExperience;
