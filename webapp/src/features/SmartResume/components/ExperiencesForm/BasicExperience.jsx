import React from 'react';
// import PropTypes from 'prop-types';
import { Row, Col, Form } from 'react-bootstrap';

import { useI8n } from 'shell/i18n';

import InputGroup from 'components/InputGroup';
import DropdownGroup from 'components/DropdownGroup';
import KButton from 'components/KButton';

// json data for dropdowns
import cityOptions from '../../../../data/city.json';

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
		<Step id="basicInfo" title={messages.basicInfo} subtitle={messages.enterBasicInfo}>
			<Form>
				<Row>{/* todo: need an image upload component  */}</Row>
				<Row>
					<Col>
						<InputGroup label={messages.cnName} id="basic-name-cn" placeholder={messages.enterCnName} />
					</Col>
					<Col>
						<InputGroup label={messages.enName} id="basic-name-en" placeholder={messages.enterEnName} />
					</Col>
				</Row>

				<Row>
					<Col lg="6">
						<InputGroup label={messages.email} id="basic-email" placeholder={messages.enterEmail} type="email" />
					</Col>
					<Col lg="3">
						<InputGroup label={messages.tel} id="basic-phone" placeholder={messages.enterTel} />
					</Col>
			
					<Col lg="3">
						<DropdownGroup label={messages.city} id="basic-city" placeholder={messages.enterCity} searchKey="city" options={cityOptions}/>
					</Col>
					{/* Leave two empty Cols to reserve the space  */}
					<Col></Col>
					<Col></Col>
				</Row>
				<Row>
					<Col>
						<InputGroup label={messages.linkedin} id="basic-linkedin" placeholder={messages.enterLinkedin} />
					</Col>
					<Col>
						<InputGroup label={messages.weblink}id="basic-weblink" placeholder={messages.enterWeblink} />
					</Col>
				</Row>
				<Row className="form_buttons">
					<Col className="space_betweens">
						<KButton variant="link" className="form_reset_link">
							{messages.resetForm}
						</KButton>
						<KButton variant="primary" type="submit">
							{messages.save}
						</KButton>
					</Col>
				</Row>
			</Form>
		</Step>
	);
};

export default BasicExperience;
