//package io.springbatch.springbatchlecture.batch.write;
//
//import io.springbatch.springbatchlecture.domain.UpbitCoinHistory;
//import io.springbatch.springbatchlecture.domain.UpbitCoinHistory;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.database.JdbcBatchItemWriter;
//import org.springframework.batch.item.database.JdbcCursorItemReader;
//import org.springframework.batch.item.database.JpaItemWriter;
//import org.springframework.batch.item.database.JpaPagingItemReader;
//import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
//import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
//import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.Table;
//
//@Slf4j
//@RequiredArgsConstructor
//@Configuration
//public class JpaItemWriterJobConfiguration {
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//    private final EntityManagerFactory entityManagerFactory;
//
//    private static final int chunkSize = 10;
//
//    @Bean
//    public Job jpaItemWriterJob() {
//        return jobBuilderFactory.get("jpaItemWriterJob")
//                .start(jpaItemWriterStep())
//                .build();
//    }
//    @Bean
//    public Step jpaItemWriterStep() {
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
//    public ItemProcessor<UpbitCoinHistory, UpbitCoinHistory> jpaItemProcessor() {
//        return upbitCoinHistory -> new UpbitCoinHistory(upbitCoinHistory.getCoinId(),upbitCoinHistory.getPrice(),upbitCoinHistory.getLastUpdate());
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
