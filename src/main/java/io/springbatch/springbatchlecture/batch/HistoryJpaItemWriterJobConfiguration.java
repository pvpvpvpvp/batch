package io.springbatch.springbatchlecture.batch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.springbatch.springbatchlecture.batch.api.UpbitCoinHistoryApi;
import io.springbatch.springbatchlecture.domain.Coin;
import io.springbatch.springbatchlecture.domain.UpbitCoinHistory;
import io.springbatch.springbatchlecture.jobparmeter.CustomJobParameterIncrementer;
import io.springbatch.springbatchlecture.processor.CustomItemProcessorCoinToHistory;
import io.springbatch.springbatchlecture.processor.CustomItemProcessorHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static final int step1ChunkSize = 10;
    //api 처리단위
    private static final int requestSize = 10;
    private int requestIndex = 0;

    private List<UpbitCoinHistoryApi> upbitCoinHistoryApiList = null;
    private boolean checkRestCall = false; //RestAPI 호출여부 판단
    private int nextIndex = 0;//리스트의 데이터를 하나씩 인덱스를 통해 가져온다.
    @Bean
    public Job historyJob(){
        return jobBuilderFactory.get("historyJob")
                .start(coinReader())
                .next(jpaItemWriterStep())
                .incrementer(new CustomJobParameterIncrementer())
                .build();
    }

    @Bean
    public Step coinReader(){
        return stepBuilderFactory.get("coinReader")
                .<Coin, Coin>chunk(step1ChunkSize)
                .reader(CoinItemReader())
                .processor(CoinProcessor())
                .writer(CoinWriter())
                .listener(promotionListener())
                .build();
    }

    @Bean
    public ItemWriter<? super Coin> CoinWriter() {
       return new ItemWriter<Coin>() {
           private StepExecution stepExecution;
           @Override
           public void write(List<? extends Coin> items) throws Exception {
               System.out.println("Items from processor : " + items.toString());
               ExecutionContext stepContext = this.stepExecution.getExecutionContext();
               List<Coin> coinData = new ArrayList<>();
               Object o = stepContext.get("COIN");
               if (o!=null){
//                   coinData.addAll((Collection<? extends Coin>) o);
                   System.out.println("o = " + o);
                   List<Coin> list = Arrays.asList(new GsonBuilder().create().fromJson((String) o, Coin[].class));
//                   Coin coin = new Gson().fromJson((String) o,Coin.class);
                   System.out.println("list = " + list);
                   coinData.addAll(list);
                   coinData.addAll(items);
               }else coinData.addAll(items);
               //역직렬화 이슈를 피하기 위해 Json 형태로 저장  -> Unable to deserialize the execution context
               stepContext.put("COIN", new Gson().toJson(coinData));
           }
           @BeforeStep
           public  void saveStepExcution(StepExecution stepExecution){
               this.stepExecution = stepExecution;
           }
       };
    }

    @Bean
    public ItemProcessor<? super Coin,? extends Coin> CoinProcessor() {
        return new CustomItemProcessorCoinToHistory();
    }

    @Bean
    public JpaPagingItemReader<Coin> CoinItemReader() {

        return new JpaPagingItemReaderBuilder()
                .name("CoinItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(10)
                .queryString("select c from Coin c where is_krw=1")
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
                List<Coin> list = Arrays.asList(new GsonBuilder().create().fromJson((String) someObject, Coin[].class));
                String jsonElements = new Gson().toJson(list);
                JSONArray jsonElements1 = new JSONArray(jsonElements.toString());
                if (!checkRestCall) {//한번도 호출 않았는지 체크

                    String path ="?";
                    int j = jsonElements1.length();
                    for (int i = requestIndex*requestSize; i < requestSize+requestIndex*requestSize; i++) {
                        if (i==j) break;
                        JSONObject jsonObject = (JSONObject) jsonElements1.get(i);
                        System.out.println("jsonObject.get(\"is_krw\") = " + jsonObject.get("is_krw"));
                        if ((boolean)jsonObject.get("is_krw")){
                            path +="markets=KRW-" +jsonObject.get("symbol")+ "&";
                        }
//

                    }
                    requestIndex++;
                    System.out.println("j = " + j);
                    System.out.println("path = " + path);
                    System.out.println("checkRestCall = false");
                    WebClient webClient = WebClient.builder()
                            .baseUrl("https://api.upbit.com/v1/ticker")
                            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .build();
                    upbitCoinHistoryApiList = webClient.get().uri(path)
                            .retrieve().bodyToFlux(UpbitCoinHistoryApi.class).toStream().collect(Collectors.toList());

                    log.info("Rest Call result : >>>>>>>" + upbitCoinHistoryApiList);

                    checkRestCall = true;//다음 read() 부터는 재호출 방지하기 위해 true로 변경
                }
                System.out.println("checkRestCall ="+checkRestCall);
                UpbitCoinHistoryApi nextCollect = null; //ItemReader는 반복문으로 동작한다. 하나씩 Writer로 전달해야 한다.s
                System.out.println("requestIndex = " + requestIndex);
                System.out.println("upbitCoinHistoryApiList = " + upbitCoinHistoryApiList.size());
                System.out.println("requestIndex*requestSize+nextIndex = " + (nextIndex+requestIndex*requestSize-requestSize));
                if (nextIndex < upbitCoinHistoryApiList.size()) {//전체 리스트에서 하나씩 추출해서, 하나씩 Writer로 전달
                    nextCollect = upbitCoinHistoryApiList.get(nextIndex);
                    JSONObject jsonObject = (JSONObject) jsonElements1.get(requestIndex*requestSize+nextIndex-requestSize);
                    nextIndex++;
                    nextCollect.setCoin_id(Long.valueOf(String.valueOf(jsonObject.get("coinId"))));
                    System.out.println("nextCollect = " + nextCollect.getCoin_id());
                    System.out.println("nextIndex = " + nextIndex);
                    if (nextIndex==requestSize) {checkRestCall = false; nextIndex=0;};
                }else {
                    checkRestCall = false; nextIndex=0; requestIndex=0;
                }
                return nextCollect;//DTO 하나씩 반환한다. Rest 호출시 데이터가 없으면 null로 반환
            }
            @BeforeStep
            public void InsertDate(StepExecution stepExecution) {
                JobExecution jobExecution = stepExecution.getJobExecution();
                ExecutionContext jobContext = jobExecution.getExecutionContext();
                this.someObject = jobContext.get("COIN");
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
        executionContextPromotionListener.setKeys(new String[]{"COIN"});

        return executionContextPromotionListener;
    }


}
