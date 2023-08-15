package com.stockexchangeapi.service;

import com.stockexchangeapi.base.TestBase;
import com.stockexchangeapi.domain.Stock;
import com.stockexchangeapi.domain.StockExchange;
import com.stockexchangeapi.exceptions.StockExchangeAlreadyExistsException;
import com.stockexchangeapi.exceptions.StockExchangeNotFoundException;
import com.stockexchangeapi.exceptions.StockNotFoundException;
import com.stockexchangeapi.model.request.AddStockToExchangeRequest;
import com.stockexchangeapi.model.request.CreateStockExchangeRequest;
import com.stockexchangeapi.repository.StockExchangeRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockExchangeServiceTest extends TestBase {

    @InjectMocks
    private StockExchangeService stockExchangeService;

    @Mock
    private StockExchangeRepository stockExchangeRepository;

    @Mock
    private StockService stockService;

    @Test
    void it_should_get_stock_exchange_by_name_successfully() {
        var stockExchange = dataGenerator.nextObject(StockExchange.class);
        when(stockExchangeRepository.findByName(StringUtils.upperCase(stockExchange.getName())))
                .thenReturn(Optional.of(stockExchange));

        var serviceResponse = stockExchangeService.getByName(stockExchange.getName());

        verify(stockExchangeRepository, times(1)).findByName(StringUtils.upperCase(stockExchange.getName()));
        assertNotNull(serviceResponse);
    }

    @Test
    void it_should_throw_stock_exchange_not_found_exception_for_get_by_name_when_not_found() {
        var stockExchangeName = "testName";
        when(stockExchangeRepository.findByName(StringUtils.upperCase(stockExchangeName)))
                .thenReturn(Optional.empty());

        assertThrows(StockExchangeNotFoundException.class, () -> stockExchangeService.getByName(stockExchangeName));
    }

    @Test
    void it_should_get_all_stock_exchanges_successfully() {
        List<StockExchange> stockExchangeList = dataGenerator.objects(StockExchange.class, 3).toList();
        when(stockExchangeRepository.findAll()).thenReturn(stockExchangeList);

        var serviceResponse = stockExchangeService.getAll();

        verify(stockExchangeRepository, times(1)).findAll();
        assertEquals(serviceResponse.size(), stockExchangeList.size());
    }

    @Test
    void it_should_create_stock_exchange_successfully() {
        CreateStockExchangeRequest request = dataGenerator.nextObject(CreateStockExchangeRequest.class);
        var createdStock = new StockExchange(1,request.getName(),request.getDescription());
        createdStock.addStock(dataGenerator.nextObject(Stock.class));
        when(stockExchangeRepository.existsStockExchangeByName(StringUtils.upperCase(request.getName())))
                .thenReturn(false);
        when(stockExchangeRepository.save(any(StockExchange.class)))
                .thenReturn(createdStock);

        var serviceResponse = stockExchangeService.create(request);

        verify(stockExchangeRepository, times(1)).save(any(StockExchange.class));
        assertNotNull(serviceResponse);
        assertTrue(serviceResponse.isLiveInMarket());
    }
    @Test
    void it_should_create_stock_exchange_with_live_in_market_when_stock_count_more_than_threshold() {
        CreateStockExchangeRequest request = dataGenerator.nextObject(CreateStockExchangeRequest.class);
        var createdStock = new StockExchange(1,request.getName(),request.getDescription());
        createdStock.addStock(dataGenerator.nextObject(Stock.class));
        createdStock.addStock(dataGenerator.nextObject(Stock.class));
        when(stockExchangeRepository.existsStockExchangeByName(StringUtils.upperCase(request.getName())))
                .thenReturn(false);
        when(stockExchangeRepository.save(any(StockExchange.class)))
                .thenReturn(createdStock);

        var serviceResponse = stockExchangeService.create(request);

        verify(stockExchangeRepository, times(1)).save(any(StockExchange.class));
        assertNotNull(serviceResponse);
        assertTrue(serviceResponse.isLiveInMarket());
    }
    @Test
    void it_should_throw_stock_exchange_already_exists_exception_when_exists() {
        CreateStockExchangeRequest request = dataGenerator.nextObject(CreateStockExchangeRequest.class);
        when(stockExchangeRepository.existsStockExchangeByName(StringUtils.upperCase(request.getName())))
                .thenReturn(true);

        assertThrows(StockExchangeAlreadyExistsException.class, () -> stockExchangeService.create(request));
    }

    @Test
    void it_should_add_stock_to_stock_exchange_successfully() {
        AddStockToExchangeRequest request = dataGenerator.nextObject(AddStockToExchangeRequest.class);
        var stockExchange = dataGenerator.nextObject(StockExchange.class);
        var stock = dataGenerator.nextObject(Stock.class);
        when(stockExchangeRepository.findByName(StringUtils.upperCase(request.getStockExchangeName())))
                .thenReturn(Optional.of(stockExchange));
        when(stockService.findActiveStockByName(request.getStockName())).thenReturn(stock);
        when(stockExchangeRepository.save(any(StockExchange.class))).thenReturn(stockExchange);

        var serviceResponse = stockExchangeService.addStock(request);

        verify(stockExchangeRepository, times(1)).save(any(StockExchange.class));
        assertNotNull(serviceResponse);
    }

    @Test
    void it_should_delete_stock_from_stock_exchange_successfully() {
        var stockExchangeName = "testExchange";
        var stockName = "testStock";
        var stockExchange = dataGenerator.nextObject(StockExchange.class);
        var stock = dataGenerator.nextObject(Stock.class);
        when(stockExchangeRepository.findByName(StringUtils.upperCase(stockExchangeName)))
                .thenReturn(Optional.of(stockExchange));
        when(stockService.findActiveStockByName(stockName)).thenReturn(stock);
        when(stockExchangeRepository.save(any(StockExchange.class))).thenReturn(stockExchange);

        var serviceResponse = stockExchangeService.deleteStock(stockExchangeName, stockName);

        verify(stockExchangeRepository, times(1)).save(any(StockExchange.class));
        assertNotNull(serviceResponse);
    }

    @Test
    void it_should_throw_stock_exchange_not_found_exception_for_delete_stock_when_stock_exchange_not_found() {
        var stockExchangeName = "testExchange";
        var stockName = "testStock";
        when(stockExchangeRepository.findByName(StringUtils.upperCase(stockExchangeName)))
                .thenReturn(Optional.empty());

        assertThrows(StockExchangeNotFoundException.class,
                () -> stockExchangeService.deleteStock(stockExchangeName, stockName));
    }

    @Test
    void it_should_throw_stock_not_found_exception_for_delete_stock_when_stock_not_found() {
        var stockExchangeName = "testExchange";
        var stockName = "testStock";
        var stockExchange = dataGenerator.nextObject(StockExchange.class);
        when(stockExchangeRepository.findByName(StringUtils.upperCase(stockExchangeName)))
                .thenReturn(Optional.of(stockExchange));
        when(stockService.findActiveStockByName(stockName)).thenThrow(StockNotFoundException.class);

        assertThrows(StockNotFoundException.class,
                () -> stockExchangeService.deleteStock(stockExchangeName, stockName));
    }
}
