import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { useSelector, useDispatch } from 'react-redux';
import moment from 'moment';

import { Link } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import { ReactComponent as EditIcon } from '../../assets/edit.svg'; 
import { ReactComponent as Download } from '../../assets/download.svg'; 
import { ReactComponent as DeleteIcon } from '../../assets/delete.svg'; 

import { selectUserId } from 'features/App/slicer';
import { deleteResume } from 'features/App/slicer/account';
import { actions } from 'features/SmartResume/slicer';
import AccountServices from 'shell/services/AccountServices';
import ResumeServices from 'shell/services/ResumeServices';
import { getLogger } from 'shell/logger';
import { useI8n } from 'shell/i18n';

import styles from '../../styles/Hub.module.css';
import { downloadPDF } from 'features/SmartResume/components/ResumePreview/resumeBuilder';
import Edit from '../../assets/edit.svg'

import zh from 'features/SmartResume/i18n/zh.json';
import en from 'features/SmartResume/i18n/en.json';

const JDBC_DATE_FORMAT = 'YYYY-MM-DD';
const logger = getLogger('ResumeHubItem');
const accountServices = new AccountServices();
const resumeServices = new ResumeServices();

async function getResume(dispatch, resumeId) {
    if (resumeId) {
        try {
            const resumeData = await resumeServices.getResume(resumeId);
            dispatch(actions.setResume(resumeData));
            return resumeData;
        } catch (exception) {
            logger.error(exception);
        }
        return;
    }
}

const Item = ({ resume, account }) => {
    const messages = useI8n();
    const dispatch = useDispatch();
    const userId = useSelector(selectUserId);
    const [showEdit, setShowEdit] = useState(true)
    const [alias, setAlias] = useState('');
    useEffect(() => {
      setAlias(resume.alias);
    }, [resume.alias]);
    const updateResumeName = (resumeId) => {
      const resumeInfo = {
        ...account.resumes.find((resume) => resume.resumeId === resumeId),
        alias,
      };
      console.log('resumeInfo: ', resumeInfo);
      accountServices.updateResume(resumeId, resumeInfo).then((res) => {
        console.log(res);
        console.log('Done!');
      });
    };
    const handleOnBlur = (resumeId) => {
      setShowEdit(true);
      if (alias === resume.alias) {
        return;
      } else {
        updateResumeName(resumeId);
      }
    };
    const handleDelete = async () => {
        dispatch(deleteResume(resume.resumeId));
        try {
            const responseJson = await accountServices.deleteResume(userId, resume);
            if (!responseJson.success) {
                logger.warn(responseJson.message);
            }
        } catch (exception) {
            logger.error(exception)
        }
    }

    const handleDownloadPDF = async () => {
        dispatch(actions.setAlias(resume?.alias));
        const resumeData = await getResume(dispatch, resume.resumeId);
        downloadPDF(resumeData?.language === 'zh' ? zh : en);
    }
    
    const showEditIcon = () => {
        setShowEdit(false)
    }
    return (
        <div className={styles.itemContainer}>
            <div className={styles.itemContent}>
                <div className={styles.right}>
                    <div className={styles.aliasContainer}>
                        <input onBlur={() => {
                            handleOnBlur(resume.resumeId);                        
                        }} onClick={() => {showEditIcon()}} onChange={(e) => {
                            setAlias(e.target.value)
                        }} className={styles.name} value={alias} />
                        {showEdit && (<div className={styles.edit}>
                            <img alt="edit" src={Edit}/>
                        </div>)}
                    </div>
                    <div className={styles.time}>
                        {moment(resume.createdAt).format(JDBC_DATE_FORMAT)}
                    </div>
                    <div className={styles.links}>
                        <Button as={Link} variant="link" to={`/resume/${resume.resumeId}`} className={styles.link} >
                            <EditIcon className={styles.svg} /> 
                            {messages['hub_item_edit']}
                        </Button>
                        <Button variant="link" onClick={handleDownloadPDF} className={styles.link}>
                            <Download className={styles.svg} /> 
                            {messages['hub_item_download']}
                        </Button>
                    </div>
                    <Button variant="link" className={styles.delete}
                        onClick={handleDelete} ><DeleteIcon className={styles.svg} /> {messages['hub_item_delete']}
                    </Button>
                </div>
            </div>
            <Button as={Link} to={`/jobs?resume=${resume.resumeId}`} className={styles.matchingBtn} >
                {messages['hub_item_match']}
            </Button>
        </div>
    );
}

Item.propTypes = {
    resume: PropTypes.shape({
        resumeId: PropTypes.string.isRequired,
        alias: PropTypes.string.isRequired,
        thumbnailUri: PropTypes.string.isRequired,
        createdAt: PropTypes.string.isRequired
    })
};

export default Item;