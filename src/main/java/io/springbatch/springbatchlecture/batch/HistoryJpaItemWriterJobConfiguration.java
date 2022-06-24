package io.springbatch.springbatchlecture.batch;

import com.google.gson.Gson;
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
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
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


    private List<UpbitCoinHistoryApi> upbitCoinHistoryApiList = null;
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
                .listener(promotionListener())
                .build();
    }

    @Bean
    public ItemWriter<? super CoinName> CoinWriter() {
       return new ItemWriter<CoinName>() {
           private StepExecution stepExecution;
           @Override
           public void write(List<? extends CoinName> items) throws Exception {
               System.out.println("Items from processor : " + items.toString());
               ExecutionContext stepContext = this.stepExecution.getExecutionContext();
               stepContext.put("COIN_NAME",items);
           }
           @BeforeStep
           public  void saveStepExcution(StepExecution stepExecution){
               this.stepExecution = stepExecution;
           }
       };
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
                .listener(promotionListener())
                .build();
    }


    @Bean
    public ItemReader<UpbitCoinHistoryApi> ItemWriterReader() {
        log.info("jdbcBatchItemWriterReader");
        return new ItemReader<UpbitCoinHistoryApi>() {

            private Object someObject;

            @Override
            public UpbitCoinHistoryApi read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                Gson gson = new Gson();
                gson.toJson(someObject);
                List<CoinName> coinName = gson.fromJson(gson.toString(), List.class);
                String path ="?";
                for (CoinName name : coinName) {
                    path +="markets="+name.getSymbol()+"&";
                }
                System.out.println("someObject = " + someObject);
                if (!checkRestCall) {//한번도 호출 않았는지 체크
                    WebClient webClient = WebClient.builder()
                            .baseUrl("https://api.upbit.com/v1/ticker")
                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .build();
                    upbitCoinHistoryApiList = webClient.get().uri(path)
                            .retrieve().bodyToFlux(UpbitCoinHistoryApi.class).toStream().collect(Collectors.toList());

                    log.info("Rest Call result : >>>>>>>" + upbitCoinHistoryApiList);

                    checkRestCall = true;//다음 read() 부터는 재호출 방지하기 위해 true로 변경
                }

                UpbitCoinHistoryApi nextCollect = null; //ItemReader는 반복문으로 동작한다. 하나씩 Writer로 전달해야 한다.s

                if (nextIndex < upbitCoinHistoryApiList.size()) {//전체 리스트에서 하나씩 추출해서, 하나씩 Writer로 전달
                    nextCollect = upbitCoinHistoryApiList.get(nextIndex);
                    nextIndex++;
                    if (nextIndex%10==0){
                        checkRestCall = false;
                    }
                }
                return nextCollect;//DTO 하나씩 반환한다. Rest 호출시 데이터가 없으면 null로 반
            }
            @BeforeStep
            public void InsertDate(StepExecution stepExecution) {
                JobExecution jobExecution = stepExecution.getJobExecution();
                ExecutionContext jobContext = jobExecution.getExecutionContext();
                this.someObject = jobContext.get("COIN_NAME");
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

    @Bean
    public ExecutionContextPromotionListener promotionListener () {
        ExecutionContextPromotionListener executionContextPromotionListener = new ExecutionContextPromotionListener();
        // 데이터 공유를 위해 사용될 key값을 미리 빈에 등록해주어야 합니다.
        executionContextPromotionListener.setKeys(new String[]{"COIN_NAME"});

        return executionContextPromotionListener;
    }


}
