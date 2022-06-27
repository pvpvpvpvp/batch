package io.springbatch.springbatchlecture.batch;


import io.springbatch.springbatchlecture.batch.api.CoinNameApi;
import io.springbatch.springbatchlecture.domain.Coin;
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
import java.util.HashMap;
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
                .<CoinNameApi, Coin>chunk(chunkSize)
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
//                    이중 반복문으로 완전 탐색 비교 중복된 데이터 인덱스를 기준인덱스에 합치고 중복 인덱스는 제거한다.(압축)
//                    이후에 프로세싱 부분에서 market에 적힌 문자열을 기준으로 is_ 의 boolean값을 적어준다.
                    for (int i = 0; i < nameList.size(); i++) {
                        for (int j = i+1; j < nameList.size(); j++) {
                            if(nameList.get(i).getMarket().split("-")[1].equals(nameList.get(j).getMarket().split("-")[1])){
                                nameList.get(i)
                                        .setMarket(
                                                nameList.get(i).getMarket()+"-,"+nameList.get(j).getMarket()
                                        );
                                nameList.remove(j);
                            }
                        }
                    }
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
    public ItemProcessor<CoinNameApi, Coin> jpaCoinItemProcessor() {
        return new CustomItemProcessorCoinName();
    }
    @Bean
    public ItemWriter<Coin> jpaCoinItemWriter() {
        return new JpaItemWriterBuilder<Coin>()
                .usePersist(true)
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}