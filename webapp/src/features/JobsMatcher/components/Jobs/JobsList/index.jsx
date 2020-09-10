import React from 'react';

import Pagination from 'react-bootstrap/Pagination'
import Job from './Job';

import { useI8n } from 'shell/i18n';

import styles from '../../../styles/JobsList.module.css';

const TOTAL_PAGINATION_ITEMS = 4;

export function calculate(pageNumber, maxPages) {
    const shouldShowPrev = pageNumber > 0;
    const shouldShowNext = pageNumber < maxPages;
    let min = Math.max(0, pageNumber - 1);
    let max = Math.min(maxPages, pageNumber + 2);

    if ((max - min + 1) < TOTAL_PAGINATION_ITEMS && (min !== 0 || max !== maxPages)) {
        if (min === 0) {
            max = min + TOTAL_PAGINATION_ITEMS - 1;
        } else {
            min = max - TOTAL_PAGINATION_ITEMS + 1;
        }
    }
    return {shouldShowPrev, shouldShowNext, min, max};
}

const getPaginationItems = (pageNumber, maxPages, onPageChange) => {
    const {shouldShowPrev, shouldShowNext, min, max} = calculate(pageNumber, maxPages);
    const items = [];
    for (let i = min; i <= max; i++) {
        items.push(
            <Pagination.Item active={pageNumber === i} onClick={() => onPageChange(i)} key={`pageItem-${i}`}>
                {i+1}
            </Pagination.Item>
        );
    }

    return (
        <Pagination>
            {shouldShowPrev ? <Pagination.Prev onClick={() => onPageChange(pageNumber-1)}/> : null}
            {items}
            {shouldShowNext ? <Pagination.Next onClick={() => onPageChange(pageNumber+1)}/> : null}
        </Pagination>
    );
}

const JobsList = ({ data, pageNumber, onPageChange, selection, onSelect }) => {
    const messages = useI8n();

    return (
        <div className={styles["container-list"]}>
            <span className={styles["list-summary"]}>
                {messages["job_list_summary"].replace('{0}', data.total || 0)}
            </span>
            {data && data.content && data.content.map((job, index) => (
                <Job data={job} key={`job-${index}`}
                    onClick={() => onSelect(index)}
                    selected={selection === index} />
            ))}
            {getPaginationItems(pageNumber, Math.floor(data.total/data.pageSize), onPageChange)}
        </div>
    );
}

export default JobsList;