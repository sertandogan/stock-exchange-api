package com.stockexchangeapi.exceptions;

import com.stockexchangeapi.enums.ErrorType;

public class PriceGreaterThanZeroException extends BaseException{
    @Override
    public ErrorType getErrorType() {
        return ErrorType.PRICE_GREATER_THAN_ZERO_EXCEPTION;
    }
}
