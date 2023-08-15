package com.stockexchangeapi.exceptions;

import com.stockexchangeapi.enums.ErrorType;

public class UserAlreadyExistsException extends BaseException{
    @Override
    public ErrorType getErrorType() {
        return ErrorType.USER_ALREADY_EXISTS_EXCEPTION;
    }
}
