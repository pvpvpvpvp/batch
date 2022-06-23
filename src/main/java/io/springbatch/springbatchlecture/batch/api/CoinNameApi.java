package io.springbatch.springbatchlecture.batch.api;

import io.springbatch.springbatchlecture.domain.CoinName;
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

    public CoinName chageSaveType () {
        CoinName coinName = new CoinName();

        coinName.setName(korean_name);
        coinName.setSymbol(market);
        coinName.setSlug(english_name);
        return coinName;
    }
}
