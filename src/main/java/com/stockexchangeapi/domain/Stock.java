package com.stockexchangeapi.domain;


import com.stockexchangeapi.exceptions.PriceGreaterThanZeroException;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Table(indexes = @Index(columnList = "name"))
@Entity(name = "stock")
@SequenceGenerator(name = "seq_stock", sequenceName = "seq_stock")
@Getter
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_stock")
    private long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private BigDecimal currentPrice;

    @Column
    private Date createdAt;

    @Column
    private Date lastUpdate;

    @Column(nullable = false)
    private boolean deleted;

    @Version
    private Long version;

    @ManyToMany(mappedBy = "stocks")
    private List<StockExchange> stockExchanges;

    protected Stock() {}

    private Stock(String name, String description, BigDecimal currentPrice) {
        this.name = name;
        this.description = description;
        this.createdAt = new Date();
        this.deleted = false;
        setCurrentPrice(currentPrice);
    }

    public static Stock createStock(String name, String description, BigDecimal currentPrice) {
        return new Stock(StringUtils.upperCase(name), description, currentPrice);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return id == stock.id && Objects.equals(name, stock.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    public void setCurrentPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0)
            throw new PriceGreaterThanZeroException();
        this.currentPrice = price;
        this.lastUpdate = new Date();
    }

    public void setDeleted(boolean deleted){
        this.deleted = deleted;
        this.lastUpdate = new Date();
    }
}
