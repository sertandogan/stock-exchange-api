package com.stockexchangeapi.converter;

import com.stockexchangeapi.domain.Stock;
import com.stockexchangeapi.model.request.CreateStockRequest;
import com.stockexchangeapi.model.response.GetStockResponse;

public class StockConverter {

    public static GetStockResponse fromEntity(Stock stock) {
        return GetStockResponse.builder()
                .id(stock.getId())
                .name(stock.getName())
                .description(stock.getDescription())
                .currentPrice(stock.getCurrentPrice())
                .createdAt(stock.getCreatedAt())
                .lastUpdate(stock.getLastUpdate())
                .build();
    }

    public static Stock fromRequest(CreateStockRequest request) {
        return Stock.createStock(request.getName(), request.getDescription(), request.getCurrentPrice());
    }
}
