package com.stockexchangeapi.repository;

import com.stockexchangeapi.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    Optional<Stock> findByNameAndDeletedFalse(String name);

    List<Stock> findAllByDeletedFalse();


    Boolean existsStockByNameAndDeletedFalse(String name);

}

