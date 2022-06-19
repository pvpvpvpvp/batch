package io.springbatch.springbatchlecture.domain;

import io.springbatch.springbatchlecture.repository.UpbitCoinRepository;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UpbitCoinHistoryTest {

    @Autowired UpbitCoinRepository upbitCoinRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void save() throws Exception {
        //given
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.upbit.com/v1/ticker?markets=KRW-BTC")
                .get()
                .addHeader("Accept", "application/json")
                .build();

        Response response = client.newCall(request).execute();

        JSONArray jsonArray = new JSONArray(response.body().string());
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);

        DateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");

        Long timestamp = jsonObject.getLong("timestamp");
        BigDecimal trade_price = BigDecimal.valueOf(jsonObject.getDouble("trade_price"));
        BigDecimal acc_trade_volume_24h = BigDecimal.valueOf(jsonObject.getDouble("acc_trade_volume_24h"));
        Date trade_date_kst = simpleDateFormat.parse(jsonObject.getString("trade_date_kst"));

        BigDecimal low_price = BigDecimal.valueOf(jsonObject.getDouble("low_price"));
        BigDecimal high_price = BigDecimal.valueOf(jsonObject.getDouble("high_price"));

        UpbitCoinHistory upbitCoinHistory = new UpbitCoinHistory();
        log.info(String.valueOf(upbitCoinHistory.getIdx()));

//        UpbitCoinHistory upbitCoinHistory =
//                new UpbitCoinHistory(timestamp, trade_price, acc_trade_volume_24h, trade_date_kst, low_price, high_price);
//        log.info("!!!!!!"+upbitCoinHistory.toString());
//        upbitCoinRepository.save(upbitCoinHistory);
        //when


        //then

    }

}