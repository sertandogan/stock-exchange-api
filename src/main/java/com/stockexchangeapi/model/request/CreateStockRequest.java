package com.stockexchangeapi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CreateStockRequest {
    @NotNull(message = "api.validation.constraints.stock.name.message")
    @NotBlank(message = "api.validation.constraints.stock.name.message")
    private String name;
    @NotNull(message = "api.validation.constraints.stock.description.message")
    @NotBlank(message = "api.validation.constraints.stock.description.message")
    private String description;
    private BigDecimal currentPrice;
}
