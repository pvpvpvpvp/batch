package io.springbatch.springbatchlecture.domain;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class PresentCoin {
    public static void main(String[] args) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.upbit.com/v1/ticker?markets=KRW-BTC")
                .get()
                .addHeader("Accept", "application/json")
                .build();

        Response response = client.newCall(request).execute();


        JSONArray jsonArray = new JSONArray(response.body().string());
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);

        Long timestamp = jsonObject.getLong("timestamp");
        BigDecimal trade_price = (BigDecimal) jsonObject.get("trade_price");
        BigDecimal acc_trade_volume_24h = (BigDecimal) jsonObject.get("acc_trade_volume_24h");
        Date candle_date_time_kst = (Date) jsonObject.get("candle_date_time_kst");
        String trade_time_kst = jsonObject.getString("trade_time_kst");
        BigDecimal low_price = (BigDecimal) jsonObject.get("low_price");
        BigDecimal high_price = (BigDecimal) jsonObject.get("high_price");


//        new UpbitCoinHistory(null,timestamp,trade_price,acc_trade_volume_24h,candle_date_time_kst,low_price,high_price);
    }


}
