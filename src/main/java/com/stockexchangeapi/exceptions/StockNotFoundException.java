package com.stockexchangeapi.exceptions;

import com.stockexchangeapi.enums.ErrorType;

public class StockNotFoundException extends BaseException{
    @Override
    public ErrorType getErrorType() {
        return ErrorType.STOCK_NOT_FOUND_EXCEPTION;
    }
}
