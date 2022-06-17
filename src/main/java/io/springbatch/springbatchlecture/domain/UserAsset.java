package io.springbatch.springbatchlecture.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class UserAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private Long id;

    private Long userIdx;
    private Long coinId;

//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "coinId")
//    private CoinName coinId;
//    @ManyToOne(fetch = LAZY)
//    @JoinColumn(name = "userIdx")
//    private User userIdx;

    private BigDecimal quantity;
    private Date insDate;
    private Date updDate;

}
