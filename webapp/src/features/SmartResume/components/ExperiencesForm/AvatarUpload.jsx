import React, { useState, useEffect } from 'react';
import { useDispatch } from 'react-redux';

import { useI8n } from 'shell/i18n';

import AvatarEdit from 'components/AvatarEdit';
import { previewResume } from '../ResumePreview/resumeBuilder';

import styles from '../../styles/AvatarUpload.module.css';
import { actions } from 'features/SmartResume/slicer';
import PhotoUploadIcon from '../../assets/photoupload.svg';
import CloseHoverIcon from '../../assets/close_hover.svg';
import CloseRegularIcon from '../../assets/close_regular.svg';

const AvatarUpload = ({photoReference, resumeLanguage}) => {
	const messages = useI8n();
	const [modalOpen, setModalOpen] = useState(false);
	const dispatch = useDispatch();
	const [avatar, setAvatar] = useState(null);

	function toDataUrl(url, callback) {
		var xhr = new XMLHttpRequest();
		xhr.onload = function() {
			var reader = new FileReader();
			reader.onloadend = function() {
				callback(reader.result);
			}
			reader.readAsDataURL(xhr.response);
		};
		xhr.open('GET', url);
		xhr.responseType = 'blob';
		xhr.send();
	}

	useEffect(() => {
		if (photoReference?.url){
			toDataUrl(photoReference.url, setAvatar);
			if (avatar){
				dispatch(actions.updateAvatar({ value: avatar }));
				previewResume(resumeLanguage);
			}
		}
	}, [photoReference, avatar, dispatch, messages.RPreview, resumeLanguage]);
	
	return (
		<div className={styles.avatarContainer}>
			<div className={styles.avatarPreviewContainer} onClick={() => setModalOpen(true)}>
				<div className={styles.avatarPreview}>
					<img src={avatar ? avatar : PhotoUploadIcon} alt="avatar" />
				</div>
				<div id="upload-photo" className={styles.avatarPreviewText}>{messages.uploadPhoto}</div>
			</div>
			<div className={`${styles.modal} ${modalOpen ? styles.modalOpen : styles.modalClosed}`}>
				<h2>{messages.addPhoto}</h2>
				<AvatarEdit imagePreview={avatar} setImagePreview={setAvatar} closeModalHandler={() => setModalOpen(false)} />
				<div className="closeIconContainer" style={{bottom: "5px", right: "10px"}} onClick={() => setModalOpen(false)}>
					<img src={CloseRegularIcon} alt="Close" className="closeIcon" />
					<img src={CloseHoverIcon} alt="Close" className="closeIconHover" />
				</div>
			</div>
		</div>
	);
};

export default AvatarUpload;
