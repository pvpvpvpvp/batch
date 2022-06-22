//package io.springbatch.springbatchlecture.batch.write;
//
//
//import io.springbatch.springbatchlecture.domain.UpbitCoinHistory;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.item.database.JdbcBatchItemWriter;
//import org.springframework.batch.item.database.JdbcCursorItemReader;
//import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
//import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Flux;
//
//import javax.sql.DataSource;
//
//@Slf4j
//@RequiredArgsConstructor
//@Configuration
//public class JdbcBatchItemWriterJobConfiguration {
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//    private final DataSource dataSource; // DataSource DI
//
//    private static final int chunkSize = 10;
//
//
//    @Bean
//    public Job jdbcBatchItemWriterJob() {
//        return jobBuilderFactory.get("jdbcBatchItemWriterJob")
//                .start(jdbcBatchItemWriterStep())
//                .build();
//    }
//    @Bean
//    public Step jdbcBatchItemWriterStep() {
//        log.info("jdbcBatchItemWriterStep");
//        return stepBuilderFactory.get("jdbcBatchItemWriterStep")
//                .<UpbitCoinHistory, UpbitCoinHistory>chunk(chunkSize)
//                .reader(jdbcBatchItemWriterReader())
//                .writer(jdbcBatchItemWriter())
//                .build();
//    }
//
//    @Bean
//    public JdbcCursorItemReader<UpbitCoinHistory> jdbcBatchItemWriterReader() {
//        log.info("jdbcBatchItemWriterReader");
//        return new JdbcCursorItemReaderBuilder<UpbitCoinHistory>()
//                .fetchSize(chunkSize)
//                .dataSource(dataSource)
//                .rowMapper(new BeanPropertyRowMapper<>(UpbitCoinHistory.class))
//                .sql("select * from upbit_coin_history")
//                .name("jdbcBatchItemWriter")
//                .build();
//
//    }
//
////    @Bean
//    public  JdbcBatchItemWriter<UpbitCoinHistory> jdbcBatchItemWriter() {
//        log.info("jdbcBatchItemWriter");
//
//        return new JdbcBatchItemWriterBuilder<UpbitCoinHistory>()
//                .dataSource(dataSource)
//                .sql("insert into upbit_coin_history2(coin_id) values (:coinId)")
//                .beanMapped()
//                .build();
//    }
//
//}
