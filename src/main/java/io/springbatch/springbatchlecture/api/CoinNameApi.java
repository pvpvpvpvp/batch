package io.springbatch.springbatchlecture.api;

import io.springbatch.springbatchlecture.domain.CoinName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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
