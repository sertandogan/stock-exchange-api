package com.stockexchangeapi.exceptions;

import com.stockexchangeapi.enums.ErrorType;

public class StockAlreadyExistsInExchangeException extends BaseException{
    public StockAlreadyExistsInExchangeException(String message){
        super(message);
    }
    @Override
    public ErrorType getErrorType() {
        return ErrorType.STOCK_ALREADY_EXISTS_IN_EXCHANGE_EXCEPTION;
    }
}
