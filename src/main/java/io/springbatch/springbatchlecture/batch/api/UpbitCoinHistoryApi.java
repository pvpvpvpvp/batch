package io.springbatch.springbatchlecture.batch.api;

import io.springbatch.springbatchlecture.domain.UpbitCoinHistory;
import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@Getter
@Setter
public class UpbitCoinHistoryApi {

    private Date timestamp;
    private BigDecimal trade_price;
    private BigDecimal acc_trade_volume_24h;
    private Date trade_date;
    private BigDecimal low_price;
    private BigDecimal high_price;

    private Long coin_id;

    public UpbitCoinHistory change_UpbitCoinHistory(){

        UpbitCoinHistory upbitCoinHistory = new UpbitCoinHistory();

        upbitCoinHistory.setLowPrice(low_price);
        upbitCoinHistory.setHighPrice(high_price);
        upbitCoinHistory.setPrice(trade_price);
        upbitCoinHistory.setCoinId(coin_id);
        upbitCoinHistory.setVolume24h(acc_trade_volume_24h);
        upbitCoinHistory.setLastUpdate(timestamp);
        return upbitCoinHistory;
    }


//    public UpbitCoinHistory(Long coinId, BigDecimal price, Date lastUpdate) {
//        this.coinId=coinId;
//        this.price=price;
//        this.lastUpdate=lastUpdate;
//    }
//
//    @Column
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long idx;

//        "market": "KRW-BTC",
//                "trade_date": "20220622",
//                "trade_time": "071758",
//                "trade_date_kst": "20220622",
//                "trade_time_kst": "161758",
//                "trade_timestamp": 1655882278000,
//                "opening_price": 26967000.0000,
//                "high_price": 27060000.0000,
//                "low_price": 26150000.0000,
//                "trade_price": 26452000.0000,
//                "prev_closing_price": 26967000.00000000,
//                "change": "FALL",
//                "change_price": 515000.00000000,
//                "change_rate": 0.0190974154,
//                "signed_change_price": -515000.00000000,
//                "signed_change_rate": -0.0190974154,
//                "trade_volume": 0.05977027,
//                "acc_trade_price": 79864841878.822300000000,
//                "acc_trade_price_24h": 266595996081.41489000,
//                "acc_trade_volume": 3000.31114220,
//                "acc_trade_volume_24h": 9801.65105945,
//                "highest_52_week_price": 82700000.00000000,
//                "highest_52_week_date": "2021-11-09",
//                "lowest_52_week_price": 23800000.0000,
//                "lowest_52_week_date": "2022-06-19",
//                "timestamp": 1655882278075


}
