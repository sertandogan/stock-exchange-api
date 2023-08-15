package com.stockexchangeapi.service;

import com.stockexchangeapi.converter.StockExchangeConverter;
import com.stockexchangeapi.domain.Stock;
import com.stockexchangeapi.domain.StockExchange;
import com.stockexchangeapi.exceptions.StockExchangeAlreadyExistsException;
import com.stockexchangeapi.exceptions.StockExchangeNotFoundException;
import com.stockexchangeapi.model.request.AddStockToExchangeRequest;
import com.stockexchangeapi.model.request.CreateStockExchangeRequest;
import com.stockexchangeapi.model.response.CreateStockExchangeResponse;
import com.stockexchangeapi.model.response.StockExchangeResponse;
import com.stockexchangeapi.repository.StockExchangeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockExchangeService {
    @Value("${application.stock.exchange.threshold}")
    private int stockThreshold;
    private final StockExchangeRepository stockExchangeRepository;
    private final StockService stockService;

    @Transactional(readOnly = true)
    public StockExchangeResponse getByName(String name) {
        var stockExchange = findStockExchangeByName(name);
        return StockExchangeConverter.fromEntity(stockExchange);
    }

    @Transactional(readOnly = true)
    public List<StockExchangeResponse> getAll() {
        var stockExchanges = stockExchangeRepository.findAll();
        return stockExchanges.stream().map(StockExchangeConverter::fromEntity).toList();
    }
    @Transactional
    public CreateStockExchangeResponse create(CreateStockExchangeRequest request) {
        var exists = stockExchangeRepository.existsStockExchangeByName(StringUtils.upperCase(request.getName()));
        if (exists)
            throw new StockExchangeAlreadyExistsException(StringUtils.upperCase(request.getName()));
        StockExchange stockExchange = new StockExchange(stockThreshold, request.getName(), request.getDescription());
        var created = stockExchangeRepository.save(stockExchange);
        return new CreateStockExchangeResponse(created.getId(), created.isLiveInMarket());
    }

    @Transactional
    public StockExchangeResponse addStock(AddStockToExchangeRequest request) {
        var stockExchange = findStockExchangeByName(request.getStockExchangeName());
        var stock = findStockByName(request.getStockName());
        stockExchange.addStock(stock);
        stockExchangeRepository.save(stockExchange);
        return StockExchangeConverter.fromEntity(stockExchange);
    }

    @Transactional
    public StockExchangeResponse deleteStock(String stockExchangeName, String stockName) {
        var stockExchange = findStockExchangeByName(stockExchangeName);
        var stock = findStockByName(stockName);
        stockExchange.getStocks().removeIf(f -> f.getId() == stock.getId());
        stockExchangeRepository.save(stockExchange);
        return StockExchangeConverter.fromEntity(stockExchange);
    }

    private StockExchange findStockExchangeByName(String name) {
        return stockExchangeRepository.findByName(StringUtils.upperCase(name))
                .orElseThrow(StockExchangeNotFoundException::new);
    }

    private Stock findStockByName(String name) {
        return stockService.findActiveStockByName(name);
    }
}
