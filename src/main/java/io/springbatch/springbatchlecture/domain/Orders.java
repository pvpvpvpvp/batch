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
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private Long coinId;
    private Long userIdx;

//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "coinId")
//    private CoinName coinId;
//
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "user")
//    private User userIdx;

    private BigDecimal quantity;
    private BigDecimal price;
    private Long state;
    private Date insDate;
    private Date updDate;

}
