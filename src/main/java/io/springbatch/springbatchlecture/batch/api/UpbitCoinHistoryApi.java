package io.springbatch.springbatchlecture.batch.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class UpbitCoinHistoryApi {

    public void history(){
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.upbit.com/v1/ticker?markets=KRW-BTC")
                .defaultHeader(MediaType.APPLICATION_JSON_VALUE)
                .build();

        System.out.println();
    }
}
