import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Create a list to store the filtered email IDs.
        List<String> emailIds = new ArrayList<>();
        // URL for the detective API.
        String emailListUrl = "https://detective.localzoho.com/portalapi/uems/admin/test/user/list?test_type=get";

        try {
            HttpURLConnection connection = createConnection(emailListUrl, "GET");
            // Use try-with-resources to ensure the reader is closed.
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                // Parse the JSON response.
                JSONObject jsonResponse = new JSONObject(responseBuilder.toString());
                JSONObject dataObject = jsonResponse.getJSONObject("data");
                JSONArray userList = dataObject.getJSONArray("user_list");

                // *******************************
                // Step B: Process each user whose email starts with "ems-testingautomation+"
                // *******************************
                for (int i = 0; i < userList.length(); i++) {
                    JSONObject user = userList.getJSONObject(i);
                    String email = user.getString("email");
                    if (email.startsWith("ems-testingautomation+")) {
                        emailIds.add(email);
                    }
                }
            }
            // Print the list of filtered email IDs.
            System.out.println("Filtered Email IDs: " + emailIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HttpURLConnection createConnection(String urlString, String method) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        if ("POST".equalsIgnoreCase(method)) {
            connection.setDoOutput(true);
        }
        return connection;
    }
}
