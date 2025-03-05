package com.zoho.detective.agent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetectiveReportSummary_old {

    public static void main(String[] args) {
        String apiUrl = "https://detective.localzoho.com/portalapi/endpointcentral/qa/results?page=1&app=&trainer_id=&title=&search_text=&failure_status=&type=active&replay_status=";

        try {
            // Call API and get response
            String response = fetchApiResponse(apiUrl);

            // Process response
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.getInt("code") == 0 && jsonResponse.has("data")) {
                JSONObject data = jsonResponse.getJSONObject("data");

                if (data.has("test_groups")) {
                    JSONArray testGroups = data.getJSONArray("test_groups");

                    int totalCountSummary = 0;
                    int successCountSummary = 0;
                    int failureCountSummary = 0;

                    System.out.println("Module Summary:");
                    System.out.println("================");
                    for (int i = 0; i < testGroups.length(); i++) {
                        JSONObject testGroup = testGroups.getJSONObject(i);

                        if ("recorded".equalsIgnoreCase(testGroup.getString("org_status"))) {
                            int totalCount = testGroup.getInt("total_count") - testGroup.getInt("disabled_count");
                            int successCount = testGroup.getInt("success_count");
                            int failureCount = testGroup.getInt("failure_count");

                            // Extract module name from title
                            String title = testGroup.getString("title");
                            String moduleName = title.contains("-") ? title.split("-")[0] : title;

                            System.out.println("Module: " + moduleName);
                            System.out.println("Total Count: " + totalCount);
                            System.out.println("Success Count: " + successCount);
                            System.out.println("Failure Count: " + failureCount);
                            System.out.println("----------------");

                            // Update summary
                            totalCountSummary += totalCount;
                            successCountSummary += successCount;
                            failureCountSummary += failureCount;
                        }
                    }

                    // Print overall summary
                    System.out.println("Overall Summary:");
                    System.out.println("================");
                    System.out.println("Total Count: " + totalCountSummary);
                    System.out.println("Success Count: " + successCountSummary);
                    System.out.println("Failure Count: " + failureCountSummary);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String fetchApiResponse(String apiUrl) throws Exception {
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

        return responseBuilder.toString();
    }
}