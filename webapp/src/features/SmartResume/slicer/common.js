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

