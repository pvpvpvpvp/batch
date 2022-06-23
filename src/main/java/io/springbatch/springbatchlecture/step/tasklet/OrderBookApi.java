package io.springbatch.springbatchlecture.step.tasklet;

import com.google.gson.Gson;
import io.springbatch.springbatchlecture.api.OrderBook;
import io.springbatch.springbatchlecture.client.ExampleClient;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
public class OrderBookApi implements Tasklet {
    
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
         System.out.println("step2 has executed");
         WebClient webClient = WebClient.builder()
                .baseUrl("https://api.upbit.com/v1/orderbook")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();


        ExampleClient c = new ExampleClient(new URI(
                "ws://localhost:9080/orderBook?batch"));
        // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
        c.connect();

//        비동기 처리
//        Flux<OrderBook> orderBookFlux = webClient.get().uri("?markets=KRW-BTC&markets=KRW-ETH")
//                .retrieve().bodyToFlux(OrderBook.class);
//        orderBookFlux.subscribe(s -> {
//            log.info("s : "+s);
//            Gson json = new Gson();
//            String messge = json.toJson(s);
//            c.send(messge);
//        });
        //toStream
        Object[] orderBookStream = webClient.get().uri("?markets=KRW-BTC&markets=KRW-ETH")
                .retrieve().bodyToFlux(OrderBook.class).toStream().toArray();
        Gson json = new Gson();
        String message = json.toJson(orderBookStream);
        c.send(message);
//        for (Object o:orderBookStream) {
//            String message = json.toJson(o);
//            c.send(message);
//        }
        //        동기 방식 (block 처리)
//        String stringMono = webClient.get().uri("?markets=KRW-BTC&markets=KRW-ETH")
//                .retrieve().bodyToMono(String.class).block();
//
//        Gson json = new Gson();
//        String message = json.toJson(stringMono);
//        c.send(message);


        return RepeatStatus.FINISHED;
    }



}
