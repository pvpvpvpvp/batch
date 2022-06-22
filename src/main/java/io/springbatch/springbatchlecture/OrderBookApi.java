package io.springbatch.springbatchlecture;

import com.google.gson.Gson;
import io.springbatch.springbatchlecture.api.OrderBook;
import io.springbatch.springbatchlecture.client.ExampleClient;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


import java.net.URI;
import java.net.URISyntaxException;

@Component
public class OrderBookApi implements Tasklet {


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
         System.out.println("step2 has executed");
         WebClient webClient = WebClient.builder()
                .baseUrl("https://api.upbit.com/v1/orderbook")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

//        동시 방식 (block 처리)
//        OrderBook stringMono = webClient.get().uri("?markets=KRW-BTC&markets=KRW-ETH")
//                .retrieve().bodyToFlux(OrderBook.class).blockFirst();
//        System.out.println("response = " + stringMono.toString());

        

//        비동기 처리
        Flux<OrderBook> orderBookFlux = webClient.get().uri("?markets=KRW-BTC&markets=KRW-ETH")
                .retrieve().bodyToFlux(OrderBook.class);
        orderBookFlux.subscribe(s -> {
            System.out.println("s = " + s);
            ExampleClient c = null; // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
            try {
                c = new ExampleClient(new URI(
                        "ws://localhost:9080/chatt"));
                c.connectBlocking();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Gson json = new Gson();
            String messge = json.toJson(s);
            c.send(messge);
        });

        return RepeatStatus.FINISHED;
    }



}
