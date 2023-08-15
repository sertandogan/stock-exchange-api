package com.stockexchangeapi.exceptions;

import com.stockexchangeapi.enums.ErrorType;

public class StockExchangeAlreadyExistsException extends BaseException{
    public StockExchangeAlreadyExistsException(String message){
        super(message);
    }
    @Override
    public ErrorType getErrorType() {
        return ErrorType.STOCK_EXCHANGE_ALREADY_EXISTS_EXCEPTION;
    }
}
