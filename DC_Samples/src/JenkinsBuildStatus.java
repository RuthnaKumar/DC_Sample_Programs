import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class JenkinsBuildStatus {

    public static void main(String[] args) {
        String jenkinsUrl = "http://pit-server:9020/jenkins/job/indhu-JOB/lastBuild/api/json";
        String username = "admin";
        String apiToken = "M@thi";

        try {
            // Create an HTTP connection
            URL url = new URL(jenkinsUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set up the request
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Basic " + encodeCredentials(username, apiToken));

            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                System.out.println("jsonResponse : "+jsonResponse);

                // Get the build status
                String buildStatus = (String) jsonResponse.get("result");
                System.out.println("Build Status: " + buildStatus);
            } else {
                System.out.println("Error: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Encode the Jenkins username and API token for Basic Authentication
    private static String encodeCredentials(String username, String apiToken) {
        String credentials = username + ":" + apiToken;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
