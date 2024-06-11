import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ParseZohoLogs {

    private static final String ACCESS_TOKEN = "Zoho-oauthtoken 1000.06bacca36bc4afdafda14c180b6d0df5.3a801e6215f166973d6332af1eef4289";
    private static final String BASE_URL = "https://logs.localzoho.com/search";
    private static final String APP_ID = "10772528";
    private static final String SERVICE = "ECReleaseMgm";
    private static final String RANGE = "1-500000";
    private static final String CSV_FILE_PATH = "E:\\Zoho Logs\\";

    public static void main(String[] args) throws UnsupportedEncodingException {
        String fromDateTime = "30/05/2024 18:50:00";
        String toDateTime = "30/05/2024 18:55:00";
//        String applicationLogQuery = "logtype=\"application\" and message contains \"SelectQuery\" or message contains \"WritableDataObject\" or message contains \"exception\" or _zl_size >= \"30000\"";
//        String getApplicationLogAPI = buildLogAPIUrl(applicationLogQuery, fromDateTime, toDateTime);

//        fetchAndWriteApplicationLogs(getApplicationLogAPI, fromDateTime, toDateTime);
        String applicationLogQuery;
        String getApplicationLogAPI;
        int loopCount = 2;
        int i;
        for(i = 0; i<loopCount; i++){
            if(i==0){
                applicationLogQuery = "logtype=\"application\" and message contains \"SelectQuery\" or message contains \"WritableDataObject\" or message contains \"exception\" or _zl_size >= \"30000\"";
                getApplicationLogAPI = buildLogAPIUrl(applicationLogQuery, fromDateTime, toDateTime);
                fetchAndWriteApplicationLogs(getApplicationLogAPI, fromDateTime, toDateTime, "ApplicationLogQueryDetails.csv");
            } if(i==1){
                applicationLogQuery = "logtype=\"application\" and message contains \"iscsignature\" or message contains \"longitude\" or message contains \"enroll?encapiKey=\" or message contains \"authkey=\"";
                getApplicationLogAPI = buildLogAPIUrl(applicationLogQuery, fromDateTime, toDateTime);
                fetchAndWriteApplicationLogs(getApplicationLogAPI, fromDateTime, toDateTime, "ApplicationSensitiveLogs.csv");
            }
        }
    }

    private static String fetchJsonFromApi(String apiUrl) throws IOException, InterruptedException {
        if(apiUrl.contains("thread_id")){
            Thread.sleep(1000);
        }
        HttpURLConnection connection = null;
        try {
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", ACCESS_TOKEN);

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder content = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    return content.toString();
                }
            } else {
                throw new IOException("Failed to fetch data: HTTP response code " + responseCode);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static void fetchAndWriteApplicationLogs(String applicationLogAPI, String fromDateTime, String toDateTime, String fileName) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(CSV_FILE_PATH+fileName))) {
            String jsonResponse = fetchJsonFromApi(applicationLogAPI);
            JsonNode rootNode = new ObjectMapper().readTree(jsonResponse);
            JsonNode docsNode = rootNode.path("docs");

            String[] header = {"SI.No", "request_url", "class_name", "method", "_zlf__zl_size","log size MB", "message", "_requested_time"};
            csvWriter.writeNext(header);

            int serialNumber = 1;
            for (JsonNode docNode : docsNode) {
                String className = docNode.path("class_name").asText();
                String method = docNode.path("method").asText();
                String size = docNode.path("_zlf__zl_size").asText();
                String sizeMD = String.valueOf(convertToMb(size));
                String message = docNode.path("message").asText();
                String requestedTime = docNode.path("_zlf__zl_timestamp").path("_requested_time").asText();

                String requestURL = fetchAccessLogUrl(docNode, fromDateTime, toDateTime);

                String[] row = {
                        String.valueOf(serialNumber++),
                        requestURL,
                        className,
                        method,
                        size,
                        sizeMD,
                        message,
                        requestedTime
                };
                csvWriter.writeNext(row);
            }

            System.out.println("CSV file created successfully: " + CSV_FILE_PATH);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String fetchAccessLogUrl(JsonNode docNode, String fromDateTime, String toDateTime) throws IOException, InterruptedException {
        String threadId = docNode.path("thread_id").asText();
        String reqId = docNode.path("req_id").asText();
        String zlHost = docNode.path("_zl_host").asText();
        String zuid = docNode.path("zuid").asText();

        String accessLogQuery = String.format("logtype=\"access\" AND (thread_id=\"%s\" AND req_id contains \"%s\" AND account=\"root\" AND _zl_host contains \"%s\" AND zuid=\"%s\")",
                threadId, reqId, zlHost, zuid);

        String accessLogAPI = buildLogAPIUrl(accessLogQuery, fromDateTime, toDateTime);
        return fetchAccessLogPath(accessLogAPI);
    }

    private static String fetchAccessLogPath(String apiUrl) {
        try {
            String jsonResponse = fetchJsonFromApi(apiUrl);
            JsonNode rootNode = new ObjectMapper().readTree(jsonResponse);
            JsonNode docsNode = rootNode.path("docs");

            for (JsonNode docNode : docsNode) {
                String path = docNode.path("path").asText();
                if (!path.isEmpty()) {
                    return path;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String buildLogAPIUrl(String query, String fromDateTime, String toDateTime) throws UnsupportedEncodingException {
        return String.format("%s?fromDateTime=%s&toDateTime=%s&query=%s&appid=%s&service=%s&range=%s",
                BASE_URL,
                URLEncoder.encode(fromDateTime, StandardCharsets.UTF_8.toString()),
                URLEncoder.encode(toDateTime, StandardCharsets.UTF_8.toString()),
                URLEncoder.encode(query, StandardCharsets.UTF_8.toString()),
                URLEncoder.encode(APP_ID, StandardCharsets.UTF_8.toString()),
                URLEncoder.encode(SERVICE, StandardCharsets.UTF_8.toString()),
                URLEncoder.encode(RANGE, StandardCharsets.UTF_8.toString()));
    }

    public static double convertToMb(String valueWithUnit) {
        double value;
        String unit = valueWithUnit.replaceAll("[0-9.]", "").toLowerCase();
        valueWithUnit = valueWithUnit.replaceAll("[^0-9.]", "");

        switch (unit) {
            case "kb":
                value = Double.parseDouble(valueWithUnit);
                return value / 1024;
            case "b":
                value = Double.parseDouble(valueWithUnit);
                return value / (1024 * 1024);
            case "mb":
                value = Double.parseDouble(valueWithUnit);
                return value;
            default:
                throw new IllegalArgumentException("Invalid unit. Please provide value with 'b', 'kb', or 'mb'.");
        }
    }
}
