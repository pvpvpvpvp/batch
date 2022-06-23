package io.springbatch.springbatchlecture.processor;

import io.springbatch.springbatchlecture.api.CoinNameApi;
import io.springbatch.springbatchlecture.domain.CoinName;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessorCoinName implements ItemProcessor<CoinNameApi, CoinName> {

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public CoinName process(CoinNameApi item) throws Exception {
        CoinName coinName = modelMapper.map(item.chageSaveType(), CoinName.class);
        return coinName;
    }
}
