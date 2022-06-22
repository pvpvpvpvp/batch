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
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;



@RequiredArgsConstructor
@Slf4j
@Configuration

public class JdbcCursorItemReaderJobConfiguration  {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private static final int chunkSize = 10;


    @Bean
    public Job jdbcCursorItemReaderJob() {
        return jobBuilderFactory.get("jdbcCursorItemReaderJob")
                .start(jdbcCursorItemReaderStep())
                .build();
    }


    @Bean
    public Step jdbcCursorItemReaderStep() {
        log.info("jdbcCursorItemReaderStep");
        return stepBuilderFactory.get("jdbcCursorItemReaderStep")
                .<UpbitCoinHistory, UpbitCoinHistory>chunk(chunkSize)
                .reader(jdbcCursorItemReaderReader())
                .writer(jdbcCursorItemWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<UpbitCoinHistory> jdbcCursorItemReaderReader() {
        log.info("jdbcCursorItemReaderReader");
        return new JdbcCursorItemReaderBuilder<UpbitCoinHistory>()
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(UpbitCoinHistory.class))
                .sql("select * from upbit_coin_history")
                .name("jdbcCursorItemReader")
                .build();

    }

    private ItemWriter<UpbitCoinHistory> jdbcCursorItemWriter() {
        log.info("jdbcCursorItemWriter");

        return list -> {
            for (UpbitCoinHistory upbitCoinHistory : list) {
                log.info("Current history={}", upbitCoinHistory);
            }
        };
    }


}