package io.springbatch.springbatchlecture.domain;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Bitcoin {
    public static void main(String[] args) throws IOException, JSONException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.upbit.com/v1/orderbook?markets=KRW-BTC")
                .get()
                .addHeader("Accept", "application/json")
                .build();

        Response response = client.newCall(request).execute();

        JSONArray jsonArray = new JSONArray(response.body().string());
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
        String timestamp = jsonObject.getString("timestamp");

        JSONArray orderbook_units = new JSONArray(jsonObject.getString("orderbook_units"));

        UpbitCoinHistory upbitCoinHistory = new UpbitCoinHistory();


        HashMap<String, Object> order= new LinkedHashMap<>();


        ArrayList<Object> temp = new ArrayList<>();
        for (int i=0; i<orderbook_units.length(); i++){
            temp.add(orderbook_units.get(i));
        }
        order.put(timestamp,temp);

        System.out.println(order);


    }

}
