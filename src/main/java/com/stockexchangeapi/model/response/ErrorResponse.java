package com.stockexchangeapi.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ErrorResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String field;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer errorCode;

    public ErrorResponse(String message){
        this.message = message;
    }

    public ErrorResponse(String field, String message){
        this.field = field;
        this.message = message;
    }

    public ErrorResponse(Integer errorCode, String message){
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ErrorResponse that = (ErrorResponse) o;

        if (!Objects.equals(field, that.field)) return false;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        int result = field != null ? field.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
