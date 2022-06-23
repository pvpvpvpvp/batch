package io.springbatch.springbatchlecture.batch.read;

import io.springbatch.springbatchlecture.domain.CoinName;
import io.springbatch.springbatchlecture.jobparmeter.CustomJobParameterIncrementer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcBatchItemWriterJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource; // DataSource DI

    private static final int chunkSize = 10;


    @Bean
    public Job jdbcBatchItemWriterJob() {
        return jobBuilderFactory.get("jdbcBatchItemWriterJob")
                .start(jdbcBatchItemWriterStep())
                .incrementer(new CustomJobParameterIncrementer())
                .build();
    }
    @Bean
    public Step jdbcBatchItemWriterStep() {
        log.info("jdbcBatchItemWriterStep");
        return stepBuilderFactory.get("jdbcBatchItemWriterStep")
                .<CoinName, CoinName>chunk(chunkSize)
                .reader(jdbcBatchItemWriterReader())
                .writer(jdbcBatchItemWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<CoinName> jdbcBatchItemWriterReader() {
        log.info("jdbcBatchItemWriterReader");
        return new JdbcCursorItemReaderBuilder<CoinName>()
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(CoinName.class))
                .sql("select * from coin_name")
                .name("jdbcBatchItemWriter")
                .build();
    }

    @Bean
    public  JdbcBatchItemWriter<CoinName> jdbcBatchItemWriter() {
        log.info("jdbcBatchItemWriter");

        return new JdbcBatchItemWriterBuilder<CoinName>()
                .dataSource(dataSource)
                .sql("insert into coin_name(name) values (:name)")
                .beanMapped()
                .build();
    }

}
