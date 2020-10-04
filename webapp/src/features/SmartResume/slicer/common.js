// A shared function 
export function updateStatus(validatorFunc, status, statusSetter, name, value) {
	const validateFlag = validatorFunc(name, value);
	const newStatus = { ...status };
	if (validateFlag === undefined) {
		newStatus[name] = {};
	} else {
		newStatus[name] = { isValid: validateFlag, isInvalid: !validateFlag };
	}
  statusSetter(newStatus);
}

export const updateAllStatus = (validatorFunc, status, statusSetter, fields, data) => {
    const newStatus = {...status}
    fields.forEach(field => {
        const validateFlag = validatorFunc(field, data[field], data);
        if (validateFlag === undefined) {
            newStatus[field] = {};
        } else {
            newStatus[field] = { isValid: validateFlag, isInvalid: !validateFlag };
        }
    })
    statusSetter(newStatus);
}



export function detectChangesForAllItem(datas, originData) {
	let changed = false;
	datas.forEach(data=>{
			if (detectChangesForSingleItem(data, originData))
					changed = true;
	})
	return changed;
}

export function detectChangesForSingleItem(data, originData){
	return Object.keys(data).some(key => data[key] !== originData[key]);
}