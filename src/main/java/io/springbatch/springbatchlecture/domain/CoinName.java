package io.springbatch.springbatchlecture.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class CoinName {
    public CoinName(String name) {
        this.name = name;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coinId;

    private String name;
    private String symbol;
    private String slug;
    private Date firstHistory;
    private Date lastHistory;

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
