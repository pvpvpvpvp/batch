package io.springbatch.springbatchlecture.batch.read;

import io.springbatch.springbatchlecture.domain.UpbitCoinHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.persistence.EntityManagerFactory;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
public class JpaPagingItemReaderJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 10;

    @Bean
    public Job jpaPagingItemReaderJob() {
        return jobBuilderFactory.get("jpaPagingItemReaderJob")
                .start(jpaPagingItemReaderStep())
                .build();
    }


    @Bean
    public Step jpaPagingItemReaderStep() {
        log.info("jpaPagingItemReaderStep");
        return stepBuilderFactory.get("jpaPagingItemReaderStep")
                .<UpbitCoinHistory, UpbitCoinHistory>chunk(chunkSize)
                .reader(jpaPagingItemReader())
                .writer(jpaPagingItemWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<UpbitCoinHistory> jpaPagingItemReader() {
        log.info("jpaPagingItemReader");
        return new JpaPagingItemReaderBuilder<UpbitCoinHistory>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("select m from upbit_coin_history m")
                .build();

    }

    private ItemWriter<UpbitCoinHistory> jpaPagingItemWriter() {
        log.info("jpaPagingItemWriter");

        return list -> {
            for (UpbitCoinHistory upbitCoinHistory : list) {
                log.info("Current history={}", upbitCoinHistory);
            }
        };
    }
}
