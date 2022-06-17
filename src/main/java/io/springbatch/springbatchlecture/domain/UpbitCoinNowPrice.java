package io.springbatch.springbatchlecture.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class UpbitCoinNowPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long id;

    private Long coinId;
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn
//    private CoinName coinId;

    private BigDecimal price;
    private Date lastUpdate;
}
