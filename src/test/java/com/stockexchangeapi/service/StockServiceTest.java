package com.stockexchangeapi.service;

import com.stockexchangeapi.base.TestBase;
import com.stockexchangeapi.domain.Stock;
import com.stockexchangeapi.exceptions.StockAlreadyExistsException;
import com.stockexchangeapi.exceptions.StockNotFoundException;
import com.stockexchangeapi.model.request.CreateStockRequest;
import com.stockexchangeapi.repository.StockRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest extends TestBase {

    @InjectMocks
    private StockService stockService;

    @Mock
    private StockRepository stockRepository;

    @Test
    void it_should_get_all_stocks_successfully() {
        List<Stock> stockList = dataGenerator.objects(Stock.class, 3).toList();
        when(stockRepository.findAllByDeletedFalse()).thenReturn(stockList);

        var serviceResponse = stockService.getAll();

        verify(stockRepository, times(1)).findAllByDeletedFalse();
        assertEquals(serviceResponse.size(), stockList.size());
    }

    @Test
    void it_should_get_stock_successfully() {
        var stock = dataGenerator.nextObject(Stock.class);
        when(stockRepository.findByNameAndDeletedFalse(StringUtils.upperCase(stock.getName()))).thenReturn(Optional.of(stock));

        stockService.getByName(stock.getName());

        verify(stockRepository, times(1)).findByNameAndDeletedFalse(StringUtils.upperCase(stock.getName()));
    }

    @Test
    void it_should_create_stock_successfully() {
        CreateStockRequest request = dataGenerator.nextObject(CreateStockRequest.class);
        Stock stock = Stock.createStock(request.getName(), request.getDescription(), request.getCurrentPrice());
        when(stockRepository.existsStockByNameAndDeletedFalse(StringUtils.upperCase(request.getName()))).thenReturn(false);
        when(stockRepository.save(any())).thenReturn(stock);

        stockService.create(request);

        verify(stockRepository, times(1)).save(any());
    }

    @Test
    void it_should_throw_already_exists_exception_when_stock_exists() {
        CreateStockRequest request = dataGenerator.nextObject(CreateStockRequest.class);
        when(stockRepository.existsStockByNameAndDeletedFalse(StringUtils.upperCase(request.getName()))).thenReturn(true);
        assertThrows(StockAlreadyExistsException.class, () -> stockService.create(request));
    }

    @Test
    void it_should_delete_stock_successfully() {

        Stock stock = dataGenerator.nextObject(Stock.class);
        stock.setDeleted(false);
        when(stockRepository.findByNameAndDeletedFalse(StringUtils.upperCase(stock.getName()))).thenReturn(Optional.of(stock));

        stockService.delete(stock.getName());

        verify(stockRepository, times(1)).save(any());
    }

    @Test
    void it_should_update_stock_price_successfully() {
        Stock stock = dataGenerator.nextObject(Stock.class);

        when(stockRepository.findByNameAndDeletedFalse(StringUtils.upperCase(stock.getName()))).thenReturn(Optional.of(stock));
        when(stockRepository.save(any())).thenReturn(stock);

        stockService.updatePrice(stock.getName(), BigDecimal.TEN);

        verify(stockRepository, times(1)).save(any());
    }

    @Test
    void it_should_find_stock_by_name_successfully() {
        Stock stock = dataGenerator.nextObject(Stock.class);

        when(stockRepository.findByNameAndDeletedFalse(StringUtils.upperCase(stock.getName()))).thenReturn(Optional.of(stock));

        stockService.findActiveStockByName(stock.getName());

        verify(stockRepository, times(1)).findByNameAndDeletedFalse(StringUtils.upperCase(stock.getName()));
    }

    @Test
    void it_should_throw_stock_not_found_exception_for_get_by_name_when_stock_not_found() {
        var stockName = "testName";
        when(stockRepository.findByNameAndDeletedFalse(StringUtils.upperCase(stockName))).thenReturn(Optional.empty());

        assertThrows(StockNotFoundException.class, () -> stockService.getByName(stockName));
    }

    @Test
    void it_should_throw_stock_not_found_exception_for_delete_stock_when_stock_not_found() {
        var stockName = "testName";
        when(stockRepository.findByNameAndDeletedFalse(StringUtils.upperCase(stockName))).thenReturn(Optional.empty());

        assertThrows(StockNotFoundException.class, () -> stockService.delete(stockName));
    }
}
