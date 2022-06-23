package io.springbatch.springbatchlecture.batch;

import io.springbatch.springbatchlecture.batch.api.UpbitCoinHistoryApi;
import io.springbatch.springbatchlecture.domain.CoinName;
import io.springbatch.springbatchlecture.domain.UpbitCoinHistory;
import io.springbatch.springbatchlecture.jobparmeter.CustomJobParameterIncrementer;
import io.springbatch.springbatchlecture.processor.CustomItemProcessorCoinToHistory;
import io.springbatch.springbatchlecture.processor.CustomItemProcessorHistory;
import io.springbatch.springbatchlecture.repository.UpbitApiCoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class HistoryJpaItemWriterJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final int chunkSize = 10;

    @Autowired
    private UpbitApiCoinRepository upbitApiCoinRepository;
    List<UpbitCoinHistoryApi> upbitCoinHistoryApiList = null;
    private boolean checkRestCall = false; //RestAPI 호출여부 판단
    private int nextIndex = 0;//리스트의 데이터를 하나씩 인덱스를 통해 가져온다.
    @Bean
    public Job jpaItemWriterJob(){
        return jobBuilderFactory.get("jpaItemWriterJob")
                .start(coinReader())
                .next(jpaItemWriterStep())
                .incrementer(new CustomJobParameterIncrementer())
                .build();
    }

    @Bean
    public Step coinReader(){
        return stepBuilderFactory.get("coinReader")
                .<CoinName, CoinName>chunk(chunkSize)
                .reader(CoinItemReader())
                .processor(CoinProcessor())
                .writer(CoinWriter())
                .build();
    }

    private ItemWriter<? super CoinName> CoinWriter() {
        return items -> System.out.println("items = " + items);
    }

    @Bean
    public ItemProcessor<? super CoinName,? extends CoinName> CoinProcessor() {
        return new CustomItemProcessorCoinToHistory();
    }

    @Bean
    public JpaPagingItemReader<CoinName> CoinItemReader() {

        return new JpaPagingItemReaderBuilder()
                .name("CoinItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(10)
                .queryString("select c from CoinName c")
                .build();

    }

    @Bean
    public Step jpaItemWriterStep() {
        log.info("jpaItemWriterStep");
        return stepBuilderFactory.get("jpaItemWriterStep")
                .<UpbitCoinHistoryApi, UpbitCoinHistory>chunk(chunkSize)
                .reader(ItemWriterReader())
                .processor(jpaItemProcessor())
                .writer(jpaItemWriter())
                .build();
    }

    @Bean
    public ItemReader<UpbitCoinHistoryApi> ItemWriterReader() {
        log.info("jdbcBatchItemWriterReader");
        return new ItemReader<UpbitCoinHistoryApi>() {
            @Override
            public UpbitCoinHistoryApi read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {


                if (!checkRestCall) {//한번도 호출 않았는지 체크
                    WebClient webClient = WebClient.builder()
                            .baseUrl("https://api.upbit.com/v1/ticker")
                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .build();
                    upbitCoinHistoryApiList = webClient.get().uri("?markets=KRW-BTC&markets=BTC-ETH")
                            .retrieve().bodyToFlux(UpbitCoinHistoryApi.class).toStream().collect(Collectors.toList());

                    log.info("Rest Call result : >>>>>>>" + upbitCoinHistoryApiList);
                    checkRestCall = true;//다음 read() 부터는 재호출 방지하기 위해 true로 변경
                }

                UpbitCoinHistoryApi nextCollect = null; //ItemReader는 반복문으로 동작한다. 하나씩 Writer로 전달해야 한다.


                if (nextIndex < upbitCoinHistoryApiList.size()) {//전체 리스트에서 하나씩 추출해서, 하나씩 Writer로 전달
                    nextCollect = upbitCoinHistoryApiList.get(nextIndex);
                    nextIndex++;
                }
                return nextCollect;//DTO 하나씩 반환한다. Rest 호출시 데이터가 없으면 null로 반
            }
        };

    }
    @Bean
    public ItemProcessor<UpbitCoinHistoryApi, UpbitCoinHistory> jpaItemProcessor() {

        return new CustomItemProcessorHistory();

    }
    @Bean
    public ItemWriter<UpbitCoinHistory> jpaItemWriter() {
        return new JpaItemWriterBuilder<UpbitCoinHistory>()
                .usePersist(true)
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

}
