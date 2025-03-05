package com.zoho.detective.agent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetectiveTestcaseLastExecutedTime {

    public static void main(String[] args) throws IOException {
        int pageLimit = 50;
        String testGroupId = "23000007911263";
        long testStartTime = 1740065520000L;
        int count = 0;
        for(int pl = 0; pl < pageLimit; pl++){
            String apiUrl = "https://detective.localzoho.com/portalapi/uems/qa/results/"+testGroupId+"?page="+pl+"&status=all&status_code_range=&type=all&api_id=&owner_id=&failure_status=&labels=";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();

           // System.out.println(responseBuilder.toString());
            String response = String.valueOf(responseBuilder);
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.getInt("code") == 0 && jsonResponse.has("data")) {
                JSONObject data = jsonResponse.getJSONObject("data");
                if (data.has("tests")) {
                    JSONArray testsArray = data.getJSONArray("tests");

                    for (int i = 0; i < testsArray.length(); i++) {
                        JSONObject tests = testsArray.getJSONObject(i);
                        String last_executed_status = tests.getString("last_executed_status");
                        if(!last_executed_status.contains("disabled")){
                            Long test_id = tests.getLong("test_id");
                            String api = tests.getString("api");
                            Long last_executed_time = tests.getLong("last_executed_time");
                            if(last_executed_time<testStartTime){
                                Date date = new Date(last_executed_time);
                                System.out.println(test_id+" : "+api+" : "+date);
                                count++;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Total Not Run Cases : "+count);

}

}