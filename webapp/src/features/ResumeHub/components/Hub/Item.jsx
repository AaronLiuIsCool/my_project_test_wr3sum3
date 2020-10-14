import React from 'react';
import PropTypes from 'prop-types';
import { useSelector, useDispatch } from 'react-redux';

import { Link } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import thumbnail from '../../assets/thumbnail@2x.png'; 
import { ReactComponent as EditIcon } from '../../assets/edit.svg'; 
import { ReactComponent as Download } from '../../assets/download.svg'; 
import { ReactComponent as ShareIcon } from '../../assets/share.svg'; 
import { ReactComponent as DuplicateIcon } from '../../assets/duplicate.svg'; 
import { ReactComponent as DeleteIcon } from '../../assets/delete.svg'; 

import { selectUserId } from 'features/App/slicer';
import { deleteResume } from 'features/App/slicer/account';
import AccountServices from 'shell/services/AccountServices';
import { getLogger } from 'shell/logger';
import { useI8n } from 'shell/i18n';

import styles from '../../styles/Hub.module.css';

const logger = getLogger('ResumeHubItem');
const accountServices = new AccountServices();

const Grid = ({ resume }) => {
    const messages = useI8n();
    const dispatch = useDispatch();
    const userId = useSelector(selectUserId);

    const handleDelete = async () => {
        dispatch(deleteResume(resume.resumeId));
        try {
            const response = await accountServices.deleteResume(userId, resume);
            const responseJson = await response.json();
            if (!response.success) {
                logger.warn(responseJson.message);
            }
        } catch (exception) {
            logger.error(exception)
        }
    }

    return (
        <div className={styles.itemContainer}>
            <div className={styles.itemContent}>
                <img src={thumbnail} width={160} height={208} alt="Thumbnail" />
                <div className={styles.right}>
                    <div className={styles.name}>
                        {resume.alias}
                    </div>
                    <div className={styles.time}>
                        {new Date(resume.createdAt).toLocaleDateString()}
                    </div>
                    <div className={styles.links}>
                        <Button as={Link} variant="link" to={`/resume/${resume.resumeId}`} className={styles.link} >
                            <EditIcon /> {messages['hub_item_edit']}
                        </Button>
                        <Button as={Link} variant="link" className={styles.link} to={'#'}>
                            <Download /> {messages['hub_item_download']}
                        </Button>
                        <Button as={Link} variant="link" className={styles.link} to={'#'}>
                            <ShareIcon /> {messages['hub_item_share']}
                        </Button>
                        <Button as={Link} variant="link" className={styles.link} to={'#'}>
                            <DuplicateIcon /> {messages['hub_item_duplicate']}
                        </Button>
                    </div>
                    <Button variant="link" className={styles.delete}
                        onClick={handleDelete} >
                        <DeleteIcon /> {messages['hub_item_delete']}
                    </Button>
                </div>
            </div>
            <Button as={Link} to={`/jobs?resume=${resume.resumeId}`} className={styles.matchingBtn} >
                {messages['hub_item_match']}
            </Button>
        </div>
    );
}

Grid.propTypes = {
    resume: PropTypes.shape({
        resumeId: PropTypes.string.isRequired,
        alias: PropTypes.string.isRequired,
        thumbnailUri: PropTypes.string.isRequired,
        createdAt: PropTypes.string.isRequired
    })
};

export default Grid;