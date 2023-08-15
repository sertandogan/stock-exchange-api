package com.stockexchangeapi.controller;

import com.stockexchangeapi.model.request.AddStockToExchangeRequest;
import com.stockexchangeapi.model.request.CreateStockExchangeRequest;
import com.stockexchangeapi.model.response.CreateStockExchangeResponse;
import com.stockexchangeapi.model.response.StockExchangeResponse;
import com.stockexchangeapi.service.StockExchangeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-exchange")
@RequiredArgsConstructor
public class StockExchangeController {

    private final StockExchangeService stockExchangeService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<StockExchangeResponse> getAll(
    ) {
        return stockExchangeService.getAll();
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public StockExchangeResponse get(
            @PathVariable String name
    ) {
        return stockExchangeService.getByName(name);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CreateStockExchangeResponse create(
            @Valid @RequestBody CreateStockExchangeRequest request
    ) {
        return stockExchangeService.create(request);
    }

    @PostMapping("/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    public StockExchangeResponse addStock(
            @PathVariable String name,
            @Valid @RequestBody AddStockToExchangeRequest request
    ) {
        request.setStockExchangeName(name);
        return stockExchangeService.addStock(request);
    }

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public StockExchangeResponse deleteStock(
            @PathVariable String name,
            @Valid @RequestParam("stockName") String stockName
    ) {
        return stockExchangeService.deleteStock(name, stockName);
    }


}
