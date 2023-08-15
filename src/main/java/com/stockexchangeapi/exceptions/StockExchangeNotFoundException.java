package com.stockexchangeapi.exceptions;

import com.stockexchangeapi.enums.ErrorType;

public class StockExchangeNotFoundException extends BaseException{
    @Override
    public ErrorType getErrorType() {
        return ErrorType.STOCK_EXCHANGE_NOT_FOUND_EXCEPTION;
    }
}
