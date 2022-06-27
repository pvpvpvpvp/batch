package io.springbatch.springbatchlecture.batch.api;

import io.springbatch.springbatchlecture.domain.Coin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CoinNameApi {

    private String market;
    private String korean_name;
    private String english_name;

    public Coin chageSaveType () {
        Coin Coin = new Coin();

        Coin.setName(korean_name);

        if (market.contains("KRW-")){
            Coin.set_krw(true);
        }
        if (market.contains("BTC-")){
            Coin.set_btc(true);
        }
        if (market.contains("USDT-")) {
            Coin.set_usdt(true);
        }
        Coin.setSymbol(market.split("-")[1]);
        Coin.setEnName(english_name);
        return Coin;
    }
}
