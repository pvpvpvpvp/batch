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
@Table
public class UpbitCoinHistory {

    @Column
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "CoinId")
//    private CoinName coinId;

    //https://api.upbit.com/v1/ticker
    private Long coinId;//

    private Long maxSupply;//
    private Long totalSupply;
    private BigDecimal price;//trade_price
    private BigDecimal volume;//acc_trade_volume_24h
    private Date lastUpdate;//trade_time_kst
    private BigDecimal lowPrice;//low_price
    private BigDecimal highPrice;//high_price


    public UpbitCoinHistory(Long coinId, BigDecimal price, BigDecimal volume, Date lastUpdate, BigDecimal lowPrice, BigDecimal highPrice) {

        this.coinId = coinId;
        this.price = price;
        this.volume = volume;
        this.lastUpdate = lastUpdate;
        this.lowPrice = lowPrice;
        this.highPrice = highPrice;
    }


    public UpbitCoinHistory(Long coinId, BigDecimal price, Date lastUpdate) {
        this.coinId=coinId;
        this.price=price;
        this.lastUpdate=lastUpdate;
    }
}
