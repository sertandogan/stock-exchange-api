package com.stockexchangeapi.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddStockToExchangeRequest {
    @NotNull(message = "api.validation.constraints.stock.name.message")
    @NotBlank(message = "api.validation.constraints.stock.name.message")
    private String stockName;
    @JsonIgnore
    private String stockExchangeName;
}
