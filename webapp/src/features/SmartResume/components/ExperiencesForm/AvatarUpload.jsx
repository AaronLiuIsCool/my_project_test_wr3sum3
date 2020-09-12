import React, { useState } from 'react';
import { useI8n } from 'shell/i18n';

import AvatarEdit from 'components/AvatarEdit';

import styles from '../../styles/AvatarUpload.module.css';
import PhotoUploadIcon from '../../assets/photoupload.svg';
import CloseHoverIcon from '../../assets/close_hover.svg';
import CloseRegularIcon from '../../assets/close_regular.svg';

const AvatarUpload = () => {
	const messages = useI8n();
	const [modalOpen, setModalOpen] = useState(false);
	const [avatar, setAvatar] = useState(null);

	
	return (
		<div className={styles.avatarContainer}>
			<div className={styles.avatarPreviewContainer} onClick={() => setModalOpen(true)}>
				<div className={styles.avatarPreview}>
					<img src={avatar ? avatar : PhotoUploadIcon} alt="avatar" />
				</div>
				<div className={styles.avatarPreviewText}>{messages.uploadPhoto}</div>
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
