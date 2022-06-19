package io.springbatch.springbatchlecture.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;


@Entity
@Getter @Setter
public class UpbitCoinHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "CoinId")
//    private CoinName coinId;

    private Long coinId;//timestamp

    private Long maxSupply;
    private Long totalSupply;
    private BigDecimal price;
    private BigDecimal volume24h;
    private Date lastUpdate;//trade_time_utc
    private BigDecimal lowPrice;
    private BigDecimal highPrice;


}
