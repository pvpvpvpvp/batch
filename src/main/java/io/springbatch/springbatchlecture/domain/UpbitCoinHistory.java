package io.springbatch.springbatchlecture.domain;

import lombok.*;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;


@Entity
@Getter @Setter
@ToString
@NoArgsConstructor
public class UpbitCoinHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "CoinId")
//    private CoinName coinId;

    //https://api.upbit.com/v1/ticker
    private Long coinId;//timestamp

    private Long maxSupply;//
    private Long totalSupply;
    private BigDecimal price;//trade_price
    private BigDecimal volume24h;//acc_trade_volume_24h
    private Date lastUpdate;//trade_time_kst
    private BigDecimal lowPrice;//low_price
    private BigDecimal highPrice;//high_price


    public UpbitCoinHistory(Long coinId, BigDecimal price, BigDecimal volume24h, Date lastUpdate, BigDecimal lowPrice, BigDecimal highPrice) {

        this.coinId = coinId;
        this.price = price;
        this.volume24h = volume24h;
        this.lastUpdate = lastUpdate;
        this.lowPrice = lowPrice;
        this.highPrice = highPrice;
    }
}
