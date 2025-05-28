package com.zoho.detective.agent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class DetectiveTriggerGETTesting {

    public static void main(String[] args) {
        try {
            // Step 1: Hit the first API (GET request)
            int pageValue = 1;
            String firstApiUrl = "https://detective.localzoho.com/portalapi/uems/qa/results?page="+pageValue+"&app=&trainer_id=&title=&search_text=&failure_status=&type=&replay_status=";
            String firstApiResponse = sendGetRequestWithHeaders(firstApiUrl);
            Thread.sleep(2000);

            // Parse the JSON response
            if(firstApiResponse!=null){
                JSONObject jsonObject = new JSONObject(firstApiResponse);
                JSONObject dataObject = jsonObject.getJSONObject("data");
                JSONArray testGroups = dataObject.getJSONArray("test_groups");

                // Iterate through the test groups
                for (int i = 0; i < testGroups.length(); i++) {
                    JSONObject testGroup = testGroups.getJSONObject(i);

                    // Check if org_status is "recorded"
                    String orgStatus = testGroup.optString("org_status", "");
                    String title = testGroup.optString("title", "");
                    if ("active".equalsIgnoreCase(orgStatus) && title.startsWith("MDM-")) {
                        String groupId = testGroup.optString("group_id", "");
                        System.out.println("Group ID : "+ i + " = "+ groupId);

                        // Step 2: Hit the second API (POST request)
                        String secondApiUrl = "https://detective.localzoho.com/portalapi/uems/qa/rerun/" + groupId +
                                "?failure_only=false&user_name=ruthna.s&accounts_user_name=Ruthna%20Kumar%20Ruthna%20Kumar";
                       // https://detective.localzoho.com/portalapi/uems/qa/rerun/23000009651089?failure_only=false&user_name=ruthna.s&accounts_user_name=Ruthna Kumar Ruthna Kumar
                        String postBody = "{}"; // Body for the POST request
                        String rerunApiResponse = sendPostRequestWithHeaders(secondApiUrl, postBody);
                        System.out.println("Rerun API Response for Group ID " + groupId + ": " + rerunApiResponse);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function to send a GET request with required headers
    private static String sendGetRequestWithHeaders(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            System.out.println("Failed to fetch data. HTTP Response Code: " + responseCode);
            return null;
        }
    }

    // Function to send a POST request with required headers
    private static String sendPostRequestWithHeaders(String apiUrl, String postBody) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Set the required headers
        setHeaders(connection);

        // Write the POST body
        try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
            out.writeBytes(postBody);
            out.flush();
        }

        // Get the response
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            System.out.println("Failed to fetch data. HTTP Response Code: " + responseCode);
            return null;
        }
    }

    // Helper method to set common headers
    private static void setHeaders(HttpURLConnection connection) {
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("accept-encoding", "gzip, deflate, br, zstd");
        connection.setRequestProperty("accept-language", "en-GB,en-US;q=0.9,en;q=0.8");
        connection.setRequestProperty("connection", "keep-alive");
        connection.setRequestProperty("content-type", "application/json");
        connection.setRequestProperty("cookie", "detective-username=ruthna.s; zabUserId=1740651575830zabu0.24997422121252222; zabBotScore=100; DSESSIONID=DSESSIONID8e6ae327ef3a6ef924090cae97b91e82; zalb_bd8611aa93=7f62d47c8d085a56ebfe900d61d62ee7; zfocscook=c1bcfe3c9e893e7724913c7be4a5194836cb490bee15dfb228f716729cbe8de36ac980d2be4120b91da527e2ffff7726a0002f7ee31d657498ead368dfb1cde0; _zcsr_tmp=c1bcfe3c9e893e7724913c7be4a5194836cb490bee15dfb228f716729cbe8de36ac980d2be4120b91da527e2ffff7726a0002f7ee31d657498ead368dfb1cde0; JSESSIONID=1BEB2364453A5F85DDCD43086989DE7C; zalb_bb65c58de0=ffc5122861141a93a75033ef47ad81b1; ignore_jboss_db_connection=true; notification_session_id=a7c30db89z; _iamadt=c08acd127baed33baa0f1b2d0150660e70454bef244c5f9d3b3d8a326571cd3298d611ebfabf837a6d89b01918e066b2; _iambdt=147f8fefd6267d2309cec07bdad7f95126fd17b939a06898961ff08ef14132dd8aff11466d5edfe5fadb8e0db16a91b97a0848f7b2b30158561e95dded5b22f8; wms-tkp-token=70837860-ee50d463-341e8854cd3c6c6b19d7c3947d2e4d6e; danton_method_profiling=true; zsc6868ae0676ff401e8d59414388870027=1745851973250zsc0.8186554247154016; zft-sdc=isef%3Dtrue-isfr%3Dtrue-source%3Ddirect; zps-tgr-dts=sc%3D35-expAppOnNewSession%3D%5B%5D-pc%3D5-sesst%3D1745851973251");
        connection.setRequestProperty("host", "detective.localzoho.com");
        connection.setRequestProperty("origin", "https://detective.localzoho.com");
        connection.setRequestProperty("referer", "https://detective.localzoho.com/portal/uems/app");
        connection.setRequestProperty("sec-ch-ua", "\"Google Chrome\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"");
        connection.setRequestProperty("sec-ch-ua-mobile", "?0");
        connection.setRequestProperty("sec-ch-ua-platform", "\"Windows\"");
        connection.setRequestProperty("sec-fetch-dest", "empty");
        connection.setRequestProperty("sec-fetch-mode", "cors");
        connection.setRequestProperty("sec-fetch-site", "same-origin");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:136.0) Gecko/20100101 Firefox/136.0");
    }
}
