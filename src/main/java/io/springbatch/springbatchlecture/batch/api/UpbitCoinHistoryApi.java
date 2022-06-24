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



}
