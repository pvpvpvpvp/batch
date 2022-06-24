package io.springbatch.springbatchlecture.batch;


import io.springbatch.springbatchlecture.batch.api.CoinNameApi;
import io.springbatch.springbatchlecture.domain.CoinName;
import io.springbatch.springbatchlecture.jobparmeter.CustomJobParameterIncrementer;
import io.springbatch.springbatchlecture.processor.CustomItemProcessorCoinName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class CoinJpaItemJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final int chunkSize = 10;
    private boolean checkRestCall = false; //RestAPI 호출여부 판단
    private int nextIndex = 0;//리스트의 데이터를 하나씩 인덱스를 통해 가져온다.
    private List<CoinNameApi> nameList = new ArrayList<>();

    @Bean
    public Job jpaCoinSave() {
        return jobBuilderFactory.get("JpaCoinSave")
                .start(jpaCoinItemWriterStep())
                .incrementer(new CustomJobParameterIncrementer())
                .build();
    }
    @Bean
    public Step jpaCoinItemWriterStep() {
        log.info("jpaItemWriterStep");
        return stepBuilderFactory.get("jpaItemWriterStep")
                .<CoinNameApi, CoinName>chunk(chunkSize)
                .reader(CoinItemWriterReader())
                .processor(jpaCoinItemProcessor())
                .writer(jpaCoinItemWriter())
                .build();
    }

    @Bean
    public ItemReader<CoinNameApi> CoinItemWriterReader() {
        log.info("BatchItemWriterReader");
        return new ItemReader<CoinNameApi>() {
            @Override
            public CoinNameApi read() throws Exception {
                if (checkRestCall == false) {//한번도 호출 않았는지 체크
                    WebClient webClient = WebClient.builder()
                            .baseUrl("https://api.upbit.com/v1/market/all?isDetails=true")
                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .build();
                    nameList = webClient.get()
                            .retrieve().bodyToFlux(CoinNameApi.class).toStream().collect(Collectors.toList());
                    checkRestCall = true;
                }

                CoinNameApi coinName = null;
                if (nextIndex < nameList.size()) {
                    coinName = nameList.get(nextIndex);
                    nextIndex++;
                }
                return coinName;
            }
        };
//        return new JpaPagingItemReaderBuilder<CoinName>()
//                .name("jpaItemWriterReader")
//                .entityManagerFactory(entityManagerFactory)
//                .pageSize(chunkSize)
//                .queryString("select u from CoinName u")
//                .build();
    }
    @Bean
    public ItemProcessor<CoinNameApi, CoinName> jpaCoinItemProcessor() {
        return new CustomItemProcessorCoinName();
    }
    @Bean
    public ItemWriter<CoinName> jpaCoinItemWriter() {
        return new JpaItemWriterBuilder<CoinName>()
                .usePersist(true)
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

}