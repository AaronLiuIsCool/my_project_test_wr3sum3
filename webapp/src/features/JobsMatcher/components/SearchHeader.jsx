import React, { useState } from 'react';

import { InputGroup, Button, Form, Row, Col } from 'react-bootstrap';
import FormInputGroup from 'components/InputGroup';

import { useI8n } from 'shell/i18n';

import styles from '../styles/SearchHeader.module.css';

const SearchHeader = ({ onSearch, initial = {} }) => {
    const messages = useI8n();
    const [query, setQuery] = useState(initial.query);
    const [country, setCountry] = useState(initial.country);
    const [city, setCity] = useState(initial.city);

    return (
        <Form className={styles.container}>
            <Row>
                <Col lg="6">
                    <Form.Group className="form_item">
                        <Form.Label htmlFor="searchQuery">{messages["search_query_label"]}</Form.Label>
                        <InputGroup>
                            <Form.Control input="text" id="searchQuery"
                                placeholder={messages["search_query_placeholder"]}
                                value={query} onChange={event => setQuery(event.target.value)} />
                            <InputGroup.Append>
                                <Button className={styles["search-button"]} onClick={() => onSearch(query, country, city)}>
                                    {messages["search_button"]}
                                </Button>
                            </InputGroup.Append>
                        </InputGroup>
                    </Form.Group>
                </Col>

                <Col lg="2">
                    <FormInputGroup label={messages["search_country_label"]} id="searchCountry"
                        placeholder={messages["search_country_label"]}
                        value={country} onChange={event => setCountry(event.target.value)} />
                </Col>

                <Col lg="2">
                    <FormInputGroup label={messages["search_city_label"]} id="searchCity"
                        placeholder={messages["search_city_label"]}
                        value={city} onChange={event => setCity(event.target.value)} />
                </Col>

            </Row>
        </Form>
    );
};

export default SearchHeader;