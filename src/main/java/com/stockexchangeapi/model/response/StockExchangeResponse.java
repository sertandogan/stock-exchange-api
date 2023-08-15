package com.stockexchangeapi.model.response;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StockExchangeResponse {
    private long id;
    private String name;
    private String description;
    private boolean liveInMarket;
    private List<GetStockResponse> stocks = new ArrayList<>();
}
