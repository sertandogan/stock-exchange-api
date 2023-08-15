package com.stockexchangeapi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStockExchangeRequest {
    @NotNull(message = "api.validation.constraints.stock.exchange.name.message")
    @NotBlank(message = "api.validation.constraints.stock.exchange.name.message")
    private String name;
    @NotNull(message = "api.validation.constraints.stock.exchange.description.message")
    @NotBlank(message = "api.validation.constraints.stock.exchange.description.message")
    private String description;
}
