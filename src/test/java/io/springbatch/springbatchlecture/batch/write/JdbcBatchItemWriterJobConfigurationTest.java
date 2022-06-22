package io.springbatch.springbatchlecture.batch.write;



import io.springbatch.springbatchlecture.batch.api.UpbitCoinHistoryApi;
import io.springbatch.springbatchlecture.domain.UpbitCoinHistory;
import io.springbatch.springbatchlecture.repository.UpbitCoinRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class JdbcBatchItemWriterJobConfigurationTest {
    @Autowired
    UpbitCoinRepository upbitCoinRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void a() throws Exception {
        System.out.println("!");
        //given

        WebClient webClient =  WebClient.builder()
                .baseUrl("https://api.upbit.com/v1/ticker")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        List<UpbitCoinHistoryApi> block = webClient.get().uri("?markets=KRW-BTC").retrieve().bodyToFlux(UpbitCoinHistoryApi.class).collectList().block();

        log.info("!!!!!!!!!!"+String.valueOf(block));

        UpbitCoinHistoryApi upbitCoinHistoryApi = new UpbitCoinHistoryApi();
        assert block != null;
        System.out.println(block.get(1).getCoinId());

        upbitCoinRepository.save(upbitCoinHistory)
        //when
//        DateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
//        Long timestamp = block.getLong("timestamp");
//        BigDecimal trade_price = BigDecimal.valueOf(block.getDouble("trade_price"));
//        BigDecimal acc_trade_price = BigDecimal.valueOf(block.getDouble("acc_trade_price"));
//        Date trade_date_kst = simpleDateFormat.parse(block.getString("trade_date_kst"));
//
//        BigDecimal low_price = BigDecimal.valueOf(block.getDouble("low_price"));
//        BigDecimal high_price = BigDecimal.valueOf(block.getDouble("high_price"));
//
//        UpbitCoinHistory upbitCoinHistory =
//                new UpbitCoinHistory(timestamp, trade_price, acc_trade_price, trade_date_kst, low_price, high_price);
//        //then
//        upbitCoinRepository.save(upbitCoinHistory);
    }
}