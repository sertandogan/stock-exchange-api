package com.stockexchangeapi.model.response;


import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class GetStockResponse {

    private long id;
    private String name;
    private String description;
    private BigDecimal currentPrice;
    private Date createdAt;
    private Date lastUpdate;
}
