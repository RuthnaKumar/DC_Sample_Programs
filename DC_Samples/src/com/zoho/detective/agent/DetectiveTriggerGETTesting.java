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
            int pageValue = 3;
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
                    if ("active".equalsIgnoreCase(orgStatus) && title.startsWith("DC_Core-")) {
                        String groupId = testGroup.optString("group_id", "");
                        System.out.println("Group ID : "+ i + " = "+ groupId);

                        // Step 2: Hit the second API (POST request)
                        String secondApiUrl = "https://detective.localzoho.com/portalapi/uems/qa/rerun/" + groupId +
                                "?failure_only=false&user_name=ruthna.s&accounts_user_name=Ruthna%20Kumar%20Ruthna%20Kumar";
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
        connection.setRequestProperty("cookie", "zabUserId=1722261886655zabu0.8320967512766113; detective-username=ruthna.s; zalb_bd8611aa93=8696657d3611b457ee7332e309d79252; zalb_bb65c58de0=ffc5122861141a93a75033ef47ad81b1; danton_method_profiling=true; _iamadt=81a8d0a27ab556053388996ef857339412c67feaa381f32b33fff0dbda37a65699353458c6e9051ebe86a411efe7e124; _iambdt=532cc3cdeb6c5fbd62854adddc1173c5fe2e8c72c21fe30d7645de0c942e2c337c0807d4af7f02fc6eb9d5e7f7595252afdaa712a821cb0f20ee47150e1bf445; wms-tkp-token=70837860-3e3902c4-d4558d9b59619f2f37af155930478e7b; zfocscook=608c6b222016d73e8b1b8ce89e5b161b614def9cf26a0e344c333cfb90d2109923c3876ae74a10c17d1b552a3b50fde8cb1a9fa9d95068e6aa044213d6f992c9; _zcsr_tmp=608c6b222016d73e8b1b8ce89e5b161b614def9cf26a0e344c333cfb90d2109923c3876ae74a10c17d1b552a3b50fde8cb1a9fa9d95068e6aa044213d6f992c9; zsc6868ae0676ff401e8d59414388870027=1741167087088zsc0.25743490886005804; zft-sdc=isef%3Dtrue-isfr%3Dtrue-source%3Ddirect; notification_session_id=ac8627f67z; JSESSIONID=E5C6471A7EF9CE1482DCE165770BC511; DSESSIONID=DSESSIONIDa89b8b86a8e892893c649b090345019e; zps-tgr-dts=sc%3D772-expAppOnNewSession%3D%5B%5D-pc%3D2-sesst%3D1741167087093");
        connection.setRequestProperty("host", "detective.localzoho.com");
        connection.setRequestProperty("origin", "https://detective.localzoho.com");
        connection.setRequestProperty("referer", "https://detective.localzoho.com/portal/uems/app");
        connection.setRequestProperty("sec-ch-ua", "\"Google Chrome\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"");
        connection.setRequestProperty("sec-ch-ua-mobile", "?0");
        connection.setRequestProperty("sec-ch-ua-platform", "\"Windows\"");
        connection.setRequestProperty("sec-fetch-dest", "empty");
        connection.setRequestProperty("sec-fetch-mode", "cors");
        connection.setRequestProperty("sec-fetch-site", "same-origin");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");
    }
}
