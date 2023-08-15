package com.stockexchangeapi.exceptions;

import com.stockexchangeapi.enums.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }
    public abstract ErrorType getErrorType();
}
