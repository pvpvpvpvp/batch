package io.springbatch.springbatchlecture.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@ToString
public class Coin {
    public Coin(String name) {
        this.name = name;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coinId;

    @Column(length = 20)
    private String name;
    @Column(length = 255)
    private String enName;
    @Column(length = 20)
    private String symbol;
    private boolean is_krw;
    private boolean is_btc;
    private boolean is_usdt;

    //fk 설정
//    @OneToMany(mappedBy = "coinId")
//    private List<UpbitCoinHistory> histories = new ArrayList<>();
//
//    @OneToMany(mappedBy = "coinId")
//    private List<Orders> orders = new ArrayList<>();
//
//    @OneToMany(mappedBy = "coinId")
//    private List<UserAsset> userAssets = new ArrayList<>();
//
//    @OneToMany(mappedBy = "coinId")
//    private List<UpbitCoinNowPrice> upbitCoinNowPrices = new ArrayList<>();


}
