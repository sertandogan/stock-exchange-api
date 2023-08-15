package com.stockexchangeapi.converter;

import com.stockexchangeapi.domain.StockExchange;
import com.stockexchangeapi.model.response.StockExchangeResponse;

public class StockExchangeConverter {

    public static StockExchangeResponse fromEntity(StockExchange stockExchange) {
        var response = new StockExchangeResponse();
        response.setId(stockExchange.getId());
        response.setName(stockExchange.getName());
        response.setDescription(stockExchange.getDescription());
        response.setLiveInMarket(stockExchange.isLiveInMarket());
        stockExchange.getStocks()
                .forEach(f ->
                        response.getStocks().add(StockConverter.fromEntity(f))
                );
        return response;
    }
}
