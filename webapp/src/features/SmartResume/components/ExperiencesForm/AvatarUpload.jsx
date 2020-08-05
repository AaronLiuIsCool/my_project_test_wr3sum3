import React, { useState } from 'react';
import { useI8n } from 'shell/i18n';

import AvatarEdit from 'components/AvatarEdit';

import styles from '../../styles/AvatarUpload.module.css';

const AvatarUpload = () => {
	const messages = useI8n();
	const [modalOpen, setModalOpen] = useState(false);
	const [avatar, setAvatar] = useState(null);
	return (
		<div className={styles.avatarContainer}>
			<div className={styles.avatarPreviewContainer} onClick={() => setModalOpen(true)}>
				<div className={styles.avatarPreview}>
					{/* todo: icon needed */}
					<img src={avatar ? avatar : process.env.PUBLIC_URL + '/images/photoupload.svg'} alt="avatar" />
				</div>
				<div className={styles.avatarPreviewText}>{messages.uploadPhoto}</div>
			</div>
			<div className={`${styles.modal} ${modalOpen ? styles.modalOpen : styles.modalClosed}`}>
				<h2>{messages.addPhoto}</h2>
				<AvatarEdit imagePreview={avatar} setImagePreview={setAvatar} closeModalHandler={() => setModalOpen(false)} />
				<div className={styles.modalCloseButton} onClick={() => setModalOpen(false)}>
					<img src={process.env.PUBLIC_URL + '/images/close_regular.svg'} alt="Close" className={styles.closeImg} />
					<img src={process.env.PUBLIC_URL + '/images/close_hover.svg'} alt="Close" className={styles.closeImgOnHover} />
				</div>
			</div>
		</div>
	);
};

export default AvatarUpload;
