package io.springbatch.springbatchlecture.processor;

import io.springbatch.springbatchlecture.domain.Coin;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessorCoinToHistory implements ItemProcessor<Coin, Coin> {

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public Coin process(Coin item) throws Exception {

        Coin coin = modelMapper.map(item, Coin.class);
        return coin;
    }

}
