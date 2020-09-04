import React from 'react';
import PropTypes from 'prop-types';
import { useSelector, useDispatch } from 'react-redux';
import { Link } from "react-router-dom";

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

import styles from '../../styles/Hub.module.css';

const logger = getLogger('ResumeHubItem');
const accountServices = new AccountServices();

const Grid = ({ resume }) => {
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
                        <Link to={`/resume/${resume.resumeId}`} className={styles.link} >
                            <EditIcon /> 编辑内容
                        </Link>
                        <Button variant="link" className={styles.link}>
                            <Download /> 下载简历
                        </Button>
                        <Button variant="link" className={styles.link}>
                            <ShareIcon /> 链接分享
                        </Button>
                        <Button variant="link" className={styles.link}>
                            <DuplicateIcon /> 复制简历
                        </Button>
                    </div>
                    <Button variant="link" className={styles.delete}
                        onClick={handleDelete} >
                        <DeleteIcon /> 删除
                    </Button>
                </div>
            </div>
            <Button className={styles.matchingBtn}>
                工作匹配
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