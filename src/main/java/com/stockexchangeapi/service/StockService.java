package com.stockexchangeapi.service;

import com.stockexchangeapi.converter.StockConverter;
import com.stockexchangeapi.domain.Stock;
import com.stockexchangeapi.domain.StockExchange;
import com.stockexchangeapi.exceptions.StockAlreadyExistsException;
import com.stockexchangeapi.exceptions.StockNotFoundException;
import com.stockexchangeapi.model.request.CreateStockRequest;
import com.stockexchangeapi.model.response.CreateStockResponse;
import com.stockexchangeapi.model.response.GetStockResponse;
import com.stockexchangeapi.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    @Transactional(readOnly = true)
    public List<GetStockResponse> getAll() {
        var stocks = stockRepository.findAllByDeletedFalse();
        return stocks.stream().map(StockConverter::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public GetStockResponse getByName(String name) {
        var stock = stockRepository.findByNameAndDeletedFalse(StringUtils.upperCase(name)).orElseThrow(StockNotFoundException::new);
        return StockConverter.fromEntity(stock);
    }

    @Transactional
    public CreateStockResponse create(CreateStockRequest request) {
        var exists = stockRepository.existsStockByNameAndDeletedFalse(StringUtils.upperCase(request.getName()));
        if (exists)
            throw new StockAlreadyExistsException(StringUtils.upperCase(request.getName()));
        var stock = StockConverter.fromRequest(request);
        var created = stockRepository.save(stock);
        return new CreateStockResponse(created.getId());
    }

    @Transactional
    public void delete(String name) {
        Stock stock = findActiveStockByName(name);
        stock.setDeleted(true);
        stockRepository.save(stock);
    }

    @Transactional
    public void updatePrice(String name, BigDecimal price) {
        var stock = findActiveStockByName(name);
        stock.setCurrentPrice(price);
        stockRepository.save(stock);
    }

    public Stock findActiveStockByName(String name) {
        return stockRepository.findByNameAndDeletedFalse(StringUtils.upperCase(name))
                .orElseThrow(StockNotFoundException::new);
    }
}
