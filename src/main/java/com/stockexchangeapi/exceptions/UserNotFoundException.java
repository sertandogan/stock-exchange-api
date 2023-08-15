package com.stockexchangeapi.exceptions;

import com.stockexchangeapi.enums.ErrorType;

public class UserNotFoundException extends BaseException{
    @Override
    public ErrorType getErrorType() {
        return ErrorType.USER_NOT_FOUND_EXCEPTION;
    }
}
