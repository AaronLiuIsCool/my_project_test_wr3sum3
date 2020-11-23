import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useI8n } from 'shell/i18n';
import { actions, selectId } from '../features/SmartResume/slicer';
import Avatar from 'react-avatar-edit';
import Button from 'react-bootstrap/Button';
import AppServices from 'shell/services/AppServices';
import ResumeServices from 'shell/services/ResumeServices';
import styles from '../features/SmartResume/styles/AvatarUpload.module.css';

const appServices = new AppServices();
const resumeServices = new ResumeServices();

const AvatarEdit = ({ imagePreview, setImagePreview, closeModalHandler }) => {
	const messages = useI8n();
	const dispatch = useDispatch();
	const resumeId = useSelector(selectId);

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
		imageUploadS3(imagePreview);
	};

	const imageUploadS3 = async (base64) => {
		let response = await appServices.imageUpload(base64, resumeId);
		if (response && response.url){
			resumeServices.updatePhotoReference(resumeId, response.url);
		}
	};

	return (
		<div>
			<Avatar width={440} height={300} cropRadius={50} onCrop={onCrop} onClose={onClose} />
			<div className={styles.samplesContainer}>
				<h2>{messages.samples}</h2>
				<div className={styles.samples}>
					<div className={styles.sample} style={{backgroundImage: `url(${process.env.PUBLIC_URL}/images/bad_photo_1.png)`}}>
						<img src={process.env.PUBLIC_URL + '/images/bad_picture.svg'} alt='check mark' />
					</div>
					<div className={styles.sample} style={{backgroundImage: `url(${process.env.PUBLIC_URL}/images/bad_photo_2.png)`}}>
						<img src={process.env.PUBLIC_URL + '/images/bad_picture.svg'} alt='check mark' />
					</div>
					<div className={styles.sample} style={{backgroundImage: `url(${process.env.PUBLIC_URL}/images/good_photo_1.png)`}}>
						<img src={process.env.PUBLIC_URL + '/images/good_picture.svg'} alt='check mark' />
					</div>
					<div className={styles.sample} style={{backgroundImage: `url(${process.env.PUBLIC_URL}/images/bad_photo_3.png)`}}>
						<img src={process.env.PUBLIC_URL + '/images/bad_picture.svg'} alt='check mark' />
					</div>
					<div className={styles.sample} style={{backgroundImage: `url(${process.env.PUBLIC_URL}/images/bad_photo_4.png)`}}>
						<img src={process.env.PUBLIC_URL + '/images/bad_picture.svg'} alt='check mark' />
					</div>
					<div className={styles.sample} style={{backgroundImage: `url(${process.env.PUBLIC_URL}/images/good_photo_2.png)`}}>
						<img src={process.env.PUBLIC_URL + '/images/good_picture.svg'} alt='check mark' />
					</div>
				</div>
			</div>
			{imagePreview && (
				<div className={styles.photoEditButton}>
					<Button variant='primary' className='w-100' onClick={onSave}>
						{messages.confirm}
					</Button>
				</div>
			)}
		</div>
	);
};

export default AvatarEdit;