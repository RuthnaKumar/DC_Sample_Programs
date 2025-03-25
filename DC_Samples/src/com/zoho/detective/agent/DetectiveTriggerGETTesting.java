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
                    if ("active".equalsIgnoreCase(orgStatus) && title.startsWith("DC_Core-")) {
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
        connection.setRequestProperty("cookie", "<value>");
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
