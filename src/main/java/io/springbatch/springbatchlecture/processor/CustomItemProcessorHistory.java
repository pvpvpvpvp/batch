package io.springbatch.springbatchlecture.processor;

import io.springbatch.springbatchlecture.batch.api.UpbitCoinHistoryApi;
import io.springbatch.springbatchlecture.domain.CoinName;
import io.springbatch.springbatchlecture.domain.UpbitCoinHistory;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessorHistory implements ItemProcessor<UpbitCoinHistoryApi, UpbitCoinHistory> {

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public UpbitCoinHistory process(UpbitCoinHistoryApi upbitCoinHistoryApi) throws Exception {
        return modelMapper.map(upbitCoinHistoryApi.change_UpbitCoinHistory(), UpbitCoinHistory.class);
    }
}
