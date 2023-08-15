package com.stockexchangeapi.exceptions;

import com.stockexchangeapi.enums.ErrorType;

public class UnauthorizedException extends BaseException{
    @Override
    public ErrorType getErrorType() {
        return ErrorType.UNAUTHORIZED_EXCEPTION;
    }
}
