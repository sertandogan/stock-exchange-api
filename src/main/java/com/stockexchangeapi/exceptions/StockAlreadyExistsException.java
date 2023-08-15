package com.stockexchangeapi.exceptions;

import com.stockexchangeapi.enums.ErrorType;

public class StockAlreadyExistsException extends BaseException{
    public StockAlreadyExistsException(String message){
        super(message);
    }
    @Override
    public ErrorType getErrorType() {
        return ErrorType.STOCK_ALREADY_EXISTS_EXCEPTION;
    }
}
