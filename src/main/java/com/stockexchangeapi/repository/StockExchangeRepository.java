package com.stockexchangeapi.repository;

import com.stockexchangeapi.domain.StockExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockExchangeRepository extends JpaRepository<StockExchange, Integer> {
    Optional<StockExchange> findByName(String name);

    Boolean existsStockExchangeByName(String name);

}

