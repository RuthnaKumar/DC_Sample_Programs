package com.zoho.detective.agent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DetectiveReportSummary {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy HH:mm");

    public static void main(String[] args) {
        int pageValue = 4; // Define the number of pages to fetch
        String apiBaseUrl = "https://detective.localzoho.com/portalapi/uems/qa/results?page={page}&app=&trainer_id=&title=&search_text=&failure_status=&type=active&replay_status=";
//        String apiBaseUrl = "https://uems-auto-k8s1.csez.zohocorpin.com:32247/portalapi/uems/qa/results?page={page}&app=&trainer_id=&title=&search_text=&failure_status=&type=active&replay_status=";
        String startTime = "March 03, 2025 16:00";

        // Define module-wise MO values
        Map<String, String> moduleMOMap = new HashMap<>();
        moduleMOMap.put("MDM", "[Vishwa](zohoid:658653206)");
        moduleMOMap.put("BSP", "[Yasar](zohoid:639823604)");
        moduleMOMap.put("DC_Core", "[Pradeep Kumar R](zohoid:695919618)");
        moduleMOMap.put("SoM", "[Yuvaraj](zohoid:23653593)");
        moduleMOMap.put("BLM", "[Shyam](zohoid:669196746)");
        moduleMOMap.put("DCP", "[Shyam](zohoid:669196746)");
        moduleMOMap.put("ACP", "[Shyam](zohoid:669196746)");
        moduleMOMap.put("Secure_USB", "[Shyam](zohoid:669196746)");
        moduleMOMap.put("RAP", "[Sandeep Kumar K](zohoid:34993510)");
        moduleMOMap.put("EDR", "[Jai](zohoid:65062597)");
        moduleMOMap.put("FWSecurity", "[Gogul Ram R B](zohoid:62186928)");
        moduleMOMap.put("UM", "[Gokulnath R](zohoid:635174875)");
        moduleMOMap.put("License", "[Gokulnath R](zohoid:635174875)");
        moduleMOMap.put("Reports", "[Mani](zohoid:768736836)");
        moduleMOMap.put("CustomGroup", "[Udhay](zohoid:681421781)");
        moduleMOMap.put("CloudFW", "[Ilfas Dilpasand](zohoid:666730116)");
        moduleMOMap.put("VMDR", "[Akbar Ali Ahamed N](zohoid:655642180)");
        moduleMOMap.put("OSD", "[Nisanth C](zohoid:62186925)");
        moduleMOMap.put("MSP", "[Kavin R](zohoid:720859235)");
        moduleMOMap.put("SummaryServer", "[Aarthi](zohoid:52760486)");

        try {
            Map<String, ModuleSummary> moduleSummaries = new HashMap<>();
            int totalTestGroups = 0, totalTestCases = 0, totalSuccess = 0, totalFailure = 0;
            long maxTestingTime = 0;

            for (int currentPage = 1; currentPage <= pageValue; currentPage++) {
                String apiUrl = apiBaseUrl.replace("{page}", String.valueOf(currentPage));
                String response = fetchApiResponse(apiUrl);

                JSONObject jsonResponse = new JSONObject(response);
                if (jsonResponse.getInt("code") == 0 && jsonResponse.has("data")) {
                    JSONObject data = jsonResponse.getJSONObject("data");

                    if (data.has("test_groups")) {
                        JSONArray testGroups = data.getJSONArray("test_groups");

                        // Process test groups
                        for (int i = 0; i < testGroups.length(); i++) {
                            JSONObject testGroup = testGroups.getJSONObject(i);

                            if ("active".equalsIgnoreCase(testGroup.getString("org_status"))) {
                                int totalCount = testGroup.getInt("total_count") - testGroup.getInt("disabled_count");
                                int successCount = testGroup.getInt("success_count");
                                int failureCount = testGroup.getInt("failure_count") +
                                        testGroup.getInt("warning_count") +
                                        testGroup.getInt("notrun_count");

                                String title = testGroup.getString("title");
                                String moduleName = title.contains("-") ? title.split("-")[0].trim() : title.trim();

                                String endTime = testGroup.getString("last_executed_time");
                                long totalTimeTaken = calculateTimeDifference(startTime, endTime);

                                moduleSummaries.putIfAbsent(moduleName, new ModuleSummary(moduleName));
                                ModuleSummary summary = moduleSummaries.get(moduleName);
                                summary.addCounts(totalCount, successCount, failureCount);
                                summary.incrementTestGroupCount();
                                summary.updateTimeDetails(startTime, endTime, totalTimeTaken);

                                totalTestGroups++;
                                totalTestCases += totalCount;
                                totalSuccess += successCount;
                                totalFailure += failureCount;

                                maxTestingTime = Math.max(maxTestingTime, totalTimeTaken);
                            }
                        }
                    }
                }
            }

            // Convert to JSON format with sorting by Total Cases
            List<JSONObject> resultList = new ArrayList<>();
            int serialNumber = 1;

            for (String moduleName : moduleSummaries.keySet()) {
                ModuleSummary summary = moduleSummaries.get(moduleName);
                JSONObject moduleJson = new JSONObject();

                moduleJson.put("SI.No", serialNumber++);
                moduleJson.put("Module", moduleName);
                moduleJson.put("Total TG", summary.getTestGroupCount());
                moduleJson.put("Total Cases", summary.getTotalCount());
                moduleJson.put("Success Count", summary.getSuccessCount());
                moduleJson.put("Failure Count", summary.getFailureCount());
                moduleJson.put("Failure Percentage", calculateFailurePercentage(summary.getFailureCount(), totalFailure));
                moduleJson.put("MO", moduleMOMap.getOrDefault(moduleName, "RK"));
                moduleJson.put("Failure Status", summary.getFailureCount() == 0 ? "All cases are success" : "Yet to Update");
                //moduleJson.put("Start Time", summary.getStartTime());
                //moduleJson.put("End Time", summary.getEndTime());
                //moduleJson.put("Total Time Taken", summary.getTotalTimeTaken() + " Minutes");

                resultList.add(moduleJson);
            }

            // Sort the result list by "Total Cases" in descending order
            resultList.sort((o1, o2) -> Integer.compare(o2.getInt("Total Cases"), o1.getInt("Total Cases")));

            // Reassign serial numbers after sorting
            serialNumber = 1;
            for (JSONObject obj : resultList) {
                obj.put("SI.No", serialNumber++);
            }

            // Add overall summary
            JSONObject overallSummary = new JSONObject();
            overallSummary.put("SI.No", "Overall");
            overallSummary.put("Module", "Summary");
            overallSummary.put("Total TG", totalTestGroups);
            overallSummary.put("Total Cases", totalTestCases);
            overallSummary.put("Success Count", totalSuccess);
            overallSummary.put("Failure Count", totalFailure);
            overallSummary.put("Failure Percentage", calculateFailurePercentage(totalFailure, totalFailure));
            overallSummary.put("MO", "All Modules");
            overallSummary.put("Failure Status", totalFailure > 0 ? "Yet to Update" : "All cases are success");
            overallSummary.put("Total Testing Time", maxTestingTime / 60 + "." + maxTestingTime % 60 + " Hours");

            resultList.add(0, overallSummary); // Add the summary as the first element

            // Print the sorted JSON array
            System.out.println(new JSONArray(resultList).toString(4));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long calculateTimeDifference(String startTime, String endTime) throws Exception {
        long startMillis = DATE_FORMAT.parse(startTime).getTime();
        long endMillis = DATE_FORMAT.parse(endTime).getTime();
        return TimeUnit.MILLISECONDS.toMinutes(endMillis - startMillis);
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

    private static String  calculateFailurePercentage(int moduleFailureCount, int totalFailure) {
        if (totalFailure == 0) {
            return "0";
        }
        double percentage = (double) (moduleFailureCount * 100) / totalFailure;
        if (percentage > 0.5) {
            long roundedPercentage = Math.round(percentage);
            return String.valueOf(roundedPercentage)+"%";
        } else {
            return String.format("%.1f", percentage)+"%";
        }
    }
}

class ModuleSummary {
    private final String moduleName;
    private int totalCount = 0;
    private int successCount = 0;
    private int failureCount = 0;
    private int testGroupCount = 0;
    private String startTime = null;
    private String endTime = null;
    private long totalTimeTaken = 0;

    public ModuleSummary(String moduleName) {
        this.moduleName = moduleName;
    }

    public void addCounts(int total, int success, int failure) {
        this.totalCount += total;
        this.successCount += success;
        this.failureCount += failure;
    }

    public void incrementTestGroupCount() {
        this.testGroupCount++;
    }

    public void updateTimeDetails(String startTime, String endTime, long timeTaken) {
        this.startTime = this.startTime == null || startTime.compareTo(this.startTime) < 0 ? startTime : this.startTime;
        this.endTime = this.endTime == null || endTime.compareTo(this.endTime) > 0 ? endTime : this.endTime;
        this.totalTimeTaken = Math.max(this.totalTimeTaken, timeTaken);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public int getTestGroupCount() {
        return testGroupCount;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public long getTotalTimeTaken() {
        return totalTimeTaken;
    }
}