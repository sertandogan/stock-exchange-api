package com.stockexchangeapi.domain;


import com.stockexchangeapi.exceptions.StockAlreadyExistsInExchangeException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "stock_exchange")
@SequenceGenerator(name = "seq_stock_exchange", sequenceName = "seq_stock_exchange")
@Getter
@NoArgsConstructor
public class StockExchange {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_stock_exchange")
    private long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private Date createdAt;

    private int stockThreshold;

    @Version
    private Long version;

    @ManyToMany
    @JoinTable(
            name = "stock_exchange_stocks",
            joinColumns = @JoinColumn(name = "stockExchangeId"),
            inverseJoinColumns = @JoinColumn(name = "stockId")
    )
    @Where(clause = "deleted = false")
    private List<Stock> stocks = new ArrayList<>();

    public StockExchange(int stockThreshold, String name, String description) {
        this.name = StringUtils.upperCase(name);
        this.description = description;
        this.stockThreshold = stockThreshold;
        this.createdAt = new Date();
    }

    public boolean isLiveInMarket() {
        return stocks.size() >= stockThreshold;
    }

    public void removeStock(Stock stock) {
        stocks.remove(stock);
        stock.getStockExchanges().remove(this);
    }
    public void addStock(Stock stock){
        if (this.stocks.contains(stock))
            throw new StockAlreadyExistsInExchangeException(stock.getName());
        this.stocks.add(stock);
    }

}
