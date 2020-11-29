package com.kuaidaoresume.common.error;

import com.kuaidaoresume.common.api.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ServiceException {
    public ResourceNotFoundException(String message) {
        super(ResultCode.NOT_FOUND, message);
    }
}
