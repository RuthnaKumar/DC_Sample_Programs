import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpAPIRequest {

    public static void main(String[] args) {
        // Replace with your actual API endpoint
        String apiUrl = "http://ems-auto2:8020/emsapi/queueCount/refreshCount";

        // Replace with your actual queue details
        Map<String, Object> queueDetails = new HashMap<>();
        queueDetails.put("tableName", "invqueueinfo");
        queueDetails.put("queueName", "testQueue");
        queueDetails.put("queueId", "1");

        try {
            sendRefreshCountRequest(apiUrl, queueDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendRefreshCountRequest(String apiUrl, Map<String, Object> queueDetails) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set the request method to PUT
        connection.setRequestMethod("PUT");

        // Set headers
        connection.setRequestProperty("Content-Type", "application/queueDetails.v1+json");
        connection.setRequestProperty("Authorization", "565E721A-33EF-43CA-BF9E-3E092F7B3650");

        // Enable input/output streams
        connection.setDoOutput(true);

        // Convert Map to JSON and write to the request body
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.writeBytes(mapToJson(queueDetails));
            wr.flush();
        }

        // Get the response code
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // Read the response
        // Note: You might want to use BufferedReader for a large response
        System.out.println("Response Body: " + connection.getResponseMessage());

        // Close the connection
        connection.disconnect();
    }

    // Convert the Map to JSON string (You can use a library like Jackson for better handling)
    private static String mapToJson(Map<String, Object> map) {
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
        }
        if (json.length() > 1) {
            json.deleteCharAt(json.length() - 1); // Remove the trailing comma
        }
        json.append("}");
        return json.toString();
    }
}
