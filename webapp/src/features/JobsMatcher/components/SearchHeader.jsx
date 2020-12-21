import React, { useState } from 'react';

import { InputGroup, Button, Form, Row, Col } from 'react-bootstrap';
import DropdownGroup from 'components/DropdownGroup';
import { useI8n } from 'shell/i18n';
import cityOptions from 'data/cn_ca_us_city.json';
import styles from '../styles/SearchHeader.module.css';

const SearchHeader = ({ onSearch, initial = {} }) => {
    const countryOptions = [
        {
          "id": "0",
          "data": "中国"
        },
        {
          "id": "1",
          "data": "Canada"
        },
        {
          "id": "2",
          "data": "USA"
        }
      ]
    const messages = useI8n();
    const [query, setQuery] = useState(initial.query);
    const [country, setCountry] = useState(initial.country);
    const [city, setCity] = useState(initial.city);

    const handleCountryChange = (values) => {
        const value = values.length === 0 ? null : values[0].data
        setCountry(value);
    };
    
    const handleCityChange = (values) => {
        const value = values.length === 0 ? null : values[0].data
        setCity(value);
    };
    
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
                    <DropdownGroup
                        label={messages["search_country_label"]}
                        id="searchCountry"
                        options={countryOptions}
                        placeholder={messages["search_country_label"]}
                        value={country} onChange={handleCountryChange}
                    />
                </Col>

                <Col lg="2">
                    <DropdownGroup
                        label={messages["search_city_label"]} id="searchCity"
                        placeholder={messages["search_city_label"]}
                        options={cityOptions}
                        value={city} onChange={handleCityChange}
                    />
                </Col>

            </Row>
        </Form>
    );
};

export default SearchHeader;