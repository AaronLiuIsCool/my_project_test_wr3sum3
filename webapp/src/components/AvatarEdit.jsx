import React from 'react';
import { useDispatch } from 'react-redux';
import { useI8n } from 'shell/i18n';
import { actions } from '../features/SmartResume/slicer';
import Avatar from 'react-avatar-edit';
import Button from 'react-bootstrap/Button';

import styles from '../features/SmartResume/styles/AvatarUpload.module.css';

const AvatarEdit = ({ imagePreview, setImagePreview, closeModalHandler }) => {
	// class AvatarEdit extends Component {
	const messages = useI8n();
	const dispatch = useDispatch();

	const onCrop = (imagePreview) => {
		setImagePreview(imagePreview);
	};

	const onClose = () => {
		setImagePreview(null);
	};

	const onSave = () => {
		setImagePreview(imagePreview);
		dispatch(actions.updateAvatar({ value: imagePreview }));
		closeModalHandler();
	};

	return (
		<div>
			<Avatar width={440} imageWidth={440} height={160} cropRadius={50} onCrop={onCrop} onClose={onClose} />
			{imagePreview == null && (
				<div className={styles.samplesContainer}>
					<h2>{messages.samples}</h2>
					<div className={styles.samples}>
						<div className={styles.sample}>
							<img src={process.env.PUBLIC_URL + '/images/good_picture.svg'} alt="check mark" />
						</div>
						<div className={styles.sample}>
							<img src={process.env.PUBLIC_URL + '/images/good_picture.svg'} alt="check mark" />
						</div>
						<div className={styles.sample}>
							<img src={process.env.PUBLIC_URL + '/images/bad_picture.svg'} alt="check mark" />
						</div>
						<div className={styles.sample}>
							<img src={process.env.PUBLIC_URL + '/images/good_picture.svg'} alt="check mark" />
						</div>
						<div className={styles.sample}>
							<img src={process.env.PUBLIC_URL + '/images/good_picture.svg'} alt="check mark" />
						</div>
						<div className={styles.sample}>
							<img src={process.env.PUBLIC_URL + '/images/bad_picture.svg'} alt="check mark" />
						</div>
					</div>
				</div>
			)}
			{imagePreview && (
				<div className={styles.photoEditButton}>
					<Button variant="primary" className="w-100" onClick={onSave}>
						{messages.confirm}
					</Button>
				</div>
			)}
		</div>
	);
};

export default AvatarEdit;
