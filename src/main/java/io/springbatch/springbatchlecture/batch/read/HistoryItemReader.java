package io.springbatch.springbatchlecture.batch.read;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.springbatch.springbatchlecture.domain.UpbitCoinHistory;
import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;

import org.springframework.http.MediaType;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.sql.DataSource;


@RequiredArgsConstructor
@Component
@StepScope
public class HistoryItemReader implements ItemReader<UpbitCoinHistory> {


    @Override
    public UpbitCoinHistory read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        ExchangeStrategies strategies = ExchangeStrategies.builder().codecs(clientDefaultCodecsConfigurer -> {
            clientDefaultCodecsConfigurer.defaultCodecs()
                    .jackson2JsonEncoder(new Jackson2CborEncoder(new ObjectMapper(), MediaType.APPLICATION_JSON));
            clientDefaultCodecsConfigurer.defaultCodecs()
                    .jackson2JsonEncoder(new Jackson2CborEncoder(new ObjectMapper(), MediaType.APPLICATION_JSON));
        }).build();

        WebClient webClient = WebClient.builder().exchangeStrategies(strategies).build();

        Mono<UpbitCoinHistory> upbitCoinHistoryMono = webClient.get()
                .uri("https://api.upbit.com/v1/ticker?markets=KRW-BTC")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(UpbitCoinHistory.class)
                .log();
        UpbitCoinHistory result = upbitCoinHistoryMono.block();
        return result;
    }
}