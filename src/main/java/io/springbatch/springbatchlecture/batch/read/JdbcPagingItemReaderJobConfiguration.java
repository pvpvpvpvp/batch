package io.springbatch.springbatchlecture.batch.read;

import io.springbatch.springbatchlecture.domain.UpbitCoinHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcPagingItemReaderJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource; // DataSource DI

    private static final int chunkSize = 10;

    @Bean
    public Job jdbcPagingItemReaderJob() throws Exception {
        return jobBuilderFactory.get("jdbcPagingItemReaderJob")
                .start(jdbcPagingItemReaderStep())
                .build();
    }
    @Bean
    public Step jdbcPagingItemReaderStep() throws Exception {
        log.info("jdbcPagingItemReaderStep");
        return stepBuilderFactory.get("jdbcPagingItemReaderStep")
                .<UpbitCoinHistory, UpbitCoinHistory>chunk(chunkSize)
                .reader(jdbcPagingItemReader())
                .writer(jdbcPagingItemWriter())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<UpbitCoinHistory> jdbcPagingItemReader() throws Exception {
        HashMap<String , Object> parameterValues  = new HashMap<>();
        parameterValues.put("price",10000);
        return new JdbcPagingItemReaderBuilder<UpbitCoinHistory>()
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(UpbitCoinHistory.class))
                .queryProvider(createQueryProvider())
                .parameterValues(parameterValues)
                .name("jdbcPagingItemReader")
                .build();

    }


    private ItemWriter<UpbitCoinHistory> jdbcPagingItemWriter() {
        log.info("jdbcCursorItemWriter");

        return list -> {
            for (UpbitCoinHistory upbitCoinHistory : list) {
                log.info("Current history={}", upbitCoinHistory);
            }
        };
    }

    private PagingQueryProvider createQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("idx,price");
        queryProvider.setFromClause("from upbit_coin_history");
        queryProvider.setWhereClause("where price> :price");

        Map<String, Order> sortkeys = new HashMap<>(1);
        sortkeys.put("idx", Order.ASCENDING);
        queryProvider.setSortKeys(sortkeys);

        return queryProvider.getObject();
    }
}