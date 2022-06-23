package io.springbatch.springbatchlecture.processor;

import io.springbatch.springbatchlecture.domain.CoinName;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessorCoinToHistory implements ItemProcessor<CoinName, CoinName> {

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public CoinName process(CoinName item) throws Exception {

        CoinName coinName = modelMapper.map(item, CoinName.class);
        return coinName;
    }

}
