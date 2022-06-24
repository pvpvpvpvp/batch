//package io.springbatch.springbatchlecture.batch.write;
//
//
//
//import io.springbatch.springbatchlecture.batch.api.UpbitCoinHistoryApi;
//import io.springbatch.springbatchlecture.domain.UpbitCoinHistory;
//import io.springbatch.springbatchlecture.repository.UpbitApiCoinRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Flux;
//
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Slf4j
//public class JdbcBatchItemWriterJobConfigurationTest {
//    @Autowired
//    UpbitApiCoinRepository upbitCoinRepository;
//
//    @Test
//    @Transactional
//    @Rollback(false)
//    public void a() throws Exception {
//        System.out.println("!");
//        //given
//
//        WebClient webClient =  WebClient.builder()
//                .baseUrl("https://api.upbit.com/v1/ticker")
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
//        List<UpbitCoinHistoryApi> upbitCoinHistoryApiList = webClient.get().uri("?markets=KRW-BTC&markets=BTC-ETH")
//                .retrieve().bodyToFlux(UpbitCoinHistoryApi.class).toStream().collect(Collectors.toList());
//
//        UpbitCoinHistoryApi upbitCoinHistoryApi = new UpbitCoinHistoryApi();
//        UpbitCoinHistory upbitCoinHistory;
//
//        for (int i=0; i<upbitCoinHistoryApiList.size();i++){
//            upbitCoinHistory=upbitCoinHistoryApi.change_UpbitCoinHistory(upbitCoinHistoryApiList.get(i));
//            upbitCoinRepository.save(upbitCoinHistory);
//        }
////        System.out.println(upbitCoinHistoryApiList.get(2));
////        upbitCoinHistory=upbitCoinHistoryApi.change_UpbitCoinHistory(upbitCoinHistoryApiList.get(0));
////        for(int i=1; i<=upbitCoinHistoryApiList.size(); i++){
////            return upbitCoinHistory1 -> upbitCoinHistoryApi.change_UpbitCoinHistory(upbitCoinHistoryApiList.get(i));
////        }
////        upbitCoinRepository.save(upbitCoinHistory);
//
////        upbitCoinRepository.save(upbitCoinHistory);
//        //when
////        DateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
////        Long timestamp = block.getLong("timestamp");
////        BigDecimal trade_price = BigDecimal.valueOf(block.getDouble("trade_price"));
////        BigDecimal acc_trade_price = BigDecimal.valueOf(block.getDouble("acc_trade_price"));
////        Date trade_date_kst = simpleDateFormat.       parse(block.getString("trade_date_kst"));
////
////        BigDecimal low_price = BigDecimal.valueOf(block.getDouble("low_price"));
////        BigDecimal high_price = BigDecimal.valueOf(block.getDouble("high_price"));
////
////        UpbitCoinHistory upbitCoinHistory =
////                new UpbitCoinHistory(timestamp, trade_price, acc_trade_price, trade_date_kst, low_price, high_price);
////        //then
////        upbitCoinRepository.save(upbitCoinHistory);
//    }
//}