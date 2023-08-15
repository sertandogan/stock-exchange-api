package com.stockexchangeapi.enums;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorType {

    UNKNOWN_ERROR(INTERNAL_SERVER_ERROR, 5000, "api.error.UNKNOWN_ERROR.message"),
    USER_NOT_FOUND_EXCEPTION(NOT_FOUND, 4001, "api.error.USER_NOT_FOUND_EXCEPTION.message"),
    STOCK_EXCHANGE_NOT_FOUND_EXCEPTION(NOT_FOUND, 4002, "api.error.STOCK_EXCHANGE_NOT_FOUND_EXCEPTION.message"),
    UNAUTHORIZED_EXCEPTION(UNAUTHORIZED, 4003, "api.error.UNAUTHORIZED_EXCEPTION.message"),
    USER_ALREADY_EXISTS_EXCEPTION(BAD_REQUEST, 4004, "api.error.USER_ALREADY_EXISTS_EXCEPTION.message"),
    STOCK_NOT_FOUND_EXCEPTION(NOT_FOUND, 4005, "api.error.STOCK_NOT_FOUND_EXCEPTION.message"),
    PRICE_GREATER_THAN_ZERO_EXCEPTION(BAD_REQUEST, 4005, "api.domain.currentPrice.greater.than.zero.message"),
    STOCK_ALREADY_EXISTS_IN_EXCHANGE_EXCEPTION(BAD_REQUEST, 4006, "api.domain.stock.already.exists.in.exchange.message"),
    STOCK_ALREADY_EXISTS_EXCEPTION(BAD_REQUEST, 4007, "api.error.STOCK_ALREADY_EXISTS_EXCEPTION.message"),
    STOCK_EXCHANGE_ALREADY_EXISTS_EXCEPTION(BAD_REQUEST, 4008, "api.error.STOCK_EXCHANGE_ALREADY_EXISTS_EXCEPTION.message"),
    OPTIMISTIC_LOCKING_EXCEPTION(CONFLICT, 4009, "api.error.OPTIMISTIC_LOCKING_EXCEPTION.message");


    private final Integer errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessageKey;

    ErrorType(HttpStatus httpStatus, Integer errorCode, String errorMessageKey) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessageKey = errorMessageKey;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMessageKey() {
        return errorMessageKey;
    }


}
