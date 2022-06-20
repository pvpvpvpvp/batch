package io.springbatch.springbatchlecture.batch;

import io.springbatch.springbatchlecture.domain.UpbitCoinHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


@RequiredArgsConstructor
@Slf4j
@Configuration
public class HistoryBatch {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int chunkSize = 10;


    @Bean
    public Job jsonJob1_batchBuild(){
        return jobBuilderFactory.get("jsonJob1").start(jsonJob1_batchStep1()).build();
    }

    @Bean
    public Step jsonJob1_batchStep1(){
        return stepBuilderFactory.get("jsonJob1_jsonReader")
                .<UpbitCoinHistory,UpbitCoinHistory>chunk(chunkSize)
                .reader(jsonJob1_jsonReader())
                .writer(coniMarket -> coniMarket.forEach(coniMarket2 ->{
                    log.debug(coniMarket2.toString());
                })).build();
    }

    @Bean
    public JsonItemReader<UpbitCoinHistory> jsonJob1_jsonReader(){
        return new JsonItemReaderBuilder<UpbitCoinHistory>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(UpbitCoinHistory.class))
                .resource(new ClassPathResource("https://api.upbit.com/v1/ticker?markets=KRW-BTC"))
                .name("jsonJob1_jsonReader")
                .build();
    }
}
