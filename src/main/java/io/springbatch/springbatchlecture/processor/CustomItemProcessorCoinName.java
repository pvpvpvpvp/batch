package io.springbatch.springbatchlecture.processor;

import io.springbatch.springbatchlecture.batch.api.CoinNameApi;
import io.springbatch.springbatchlecture.domain.Coin;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessorCoinName implements ItemProcessor<CoinNameApi, Coin> {

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public Coin process(CoinNameApi item) throws Exception {
        Coin coinName = modelMapper.map(item.chageSaveType(), Coin.class);
        return coinName;
    }
}
