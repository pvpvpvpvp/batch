//package io.springbatch.springbatchlecture.batch.write;
//
//import io.springbatch.springbatchlecture.domain.UpbitCoinHistory;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.codehaus.jettison.json.JSONArray;
//import org.codehaus.jettison.json.JSONException;
//import org.codehaus.jettison.json.JSONObject;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.database.JpaItemWriter;
//import org.springframework.batch.item.database.JpaPagingItemReader;
//import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import javax.persistence.EntityManagerFactory;
//import java.math.BigDecimal;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//@RequiredArgsConstructor
//@Configuration
//public class JpaItemWriterJobConfiguration2 {
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//    private final EntityManagerFactory entityManagerFactory;
//
//    private static final int chunkSize = 10;
//
//    @Bean
//    public Job jpaItemWriterJob() throws JSONException, ParseException {
//        return jobBuilderFactory.get("jpaItemWriterJob")
//                .start(jpaItemWriterStep())
//                .build();
//    }
//    @Bean
//    public Step jpaItemWriterStep() throws JSONException, ParseException {
//        log.info("jpaItemWriterStep");
//        return stepBuilderFactory.get("jpaItemWriterStep")
//                .<UpbitCoinHistory, UpbitCoinHistory>chunk(chunkSize)
//                .reader(jpaItemWriterReader())
//                .processor(jpaItemProcessor())
//                .writer(jpaItemWriter())
//                .build();
//    }
//
//    @Bean
//    public JpaPagingItemReader<UpbitCoinHistory> jpaItemWriterReader() {
//        log.info("jdbcBatchItemWriterReader");
//        return new JpaPagingItemReaderBuilder<UpbitCoinHistory>()
//                .name("jpaItemWriterReader")
//                .entityManagerFactory(entityManagerFactory)
//                .pageSize(chunkSize)
//                .queryString("select u from UpbitCoinHistory u")
//                .build();
//    }
//    @Bean
//    public ItemProcessor<UpbitCoinHistory, UpbitCoinHistory> jpaItemProcessor() throws JSONException, ParseException {
//        return upbitCoinHistory -> new UpbitCoinHistory(ge);
//    }
//    @Bean
//    public JpaItemWriter<UpbitCoinHistory> jpaItemWriter() {
//        log.info("jpaItemWriter");
//        JpaItemWriter<UpbitCoinHistory> jpaItemWriter = new JpaItemWriter<>();
//        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
//        return jpaItemWriter;
//    }
//
//}
