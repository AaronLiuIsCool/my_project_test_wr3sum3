import React from 'react';
import { useDispatch } from 'react-redux';
import { actions } from '../../slicer';

import styles from '../../styles/ResumePreview.module.css';


const ResumeThemeColorPicker = ({ setIsThemeColorModalOpen }) => {
	const dispatch = useDispatch();
	const COLOR_BLUE = '#3e89ec';
	const COLOR_ORANGE = '#ec663e';
	const COLOR_PURPLE = '#743eec';
	const COLOR_LIGHT_BLUE = '#3ea2ec';
	const colorList = [COLOR_BLUE, COLOR_ORANGE, COLOR_PURPLE, COLOR_LIGHT_BLUE];

	const handleColorPicked = (color) => {
		dispatch(actions.updateColor({ color }));
		setIsThemeColorModalOpen(false);
	};

	return (
		<div className={styles.colorPickercontainer}>
			{colorList.map((color) => (
				<span key={color} className={styles.colorCircle} style={{ backgroundColor: color }} onClick={() => handleColorPicked(color)}></span>
			))}
		</div>
	);
};

export default ResumeThemeColorPicker;
