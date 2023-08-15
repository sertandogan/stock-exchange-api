package com.stockexchangeapi.controller;

import com.stockexchangeapi.model.request.CreateStockRequest;
import com.stockexchangeapi.model.response.CreateStockResponse;
import com.stockexchangeapi.model.response.GetStockResponse;
import com.stockexchangeapi.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CreateStockResponse create(
            @Valid @RequestBody CreateStockRequest request
    ) {
        return stockService.create(request);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<GetStockResponse> getAll() {
        return stockService.getAll();
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public GetStockResponse get(
            @PathVariable String name
    ) {
        return stockService.getByName(name);
    }

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable String name
    ) {
        stockService.delete(name);
    }

    @PutMapping("/{name}/current-price")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePrice(
            @PathVariable String name,
            @Valid @RequestParam BigDecimal price
    ) {
        stockService.updatePrice(name, price);
    }
}
