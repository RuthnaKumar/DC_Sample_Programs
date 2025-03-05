import java.io.*;
import java.net.*;
import java.util.*;

public class FetchCookies {
    public static void main(String[] args) {
        try {
            // API 1: Fetch initial cookies
            String apiUrl1 = "https://accounts.localzoho.com/signin?servicename=DesktopCentralCloud&serviceurl=https%3A%2F%2Fendpointcentral.localmanageengine.com%2Fwebclient&signupurl=https://endpointcentral.localmanageengine.com/signup.html";
            HttpURLConnection connection1 = (HttpURLConnection) new URL(apiUrl1).openConnection();
            connection1.setRequestMethod("GET");

            Map<String, List<String>> headers1 = connection1.getHeaderFields();
            StringBuilder cookieBuilder = new StringBuilder();
            String iamcsr = "";

            if (headers1.containsKey("Set-Cookie")) {
                for (String cookie : headers1.get("Set-Cookie")) {
                    String formattedCookie = cookie.split(";")[0]; // Extract only the key=value part
                    cookieBuilder.append(formattedCookie).append(";");

                    // Extract iamcsr value
                    if (formattedCookie.startsWith("iamcsr=")) {
                        iamcsr = formattedCookie.split("=")[1];
                    }
                }
            }

            cookieBuilder.append(" httponly; secure;");
            String constructedCookie = cookieBuilder.toString();

            // API 2: POST request with cookies and iamcsr token
            String apiUrl2 = "https://accounts.localzoho.com/signin/v2/lookup/ems-testingautomation+post6_rk@zohotest.com";
            HttpURLConnection connection2 = (HttpURLConnection) new URL(apiUrl2).openConnection();
            connection2.setRequestMethod("POST");
            connection2.setDoOutput(true);

            // Set request headers
            connection2.setRequestProperty("X-ZCSRF-TOKEN", "iamcsrcoo=" + iamcsr);
            connection2.setRequestProperty("Cookie", constructedCookie);
            connection2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Set request parameters
            String params = "mode=primary&cli_time=1738850279163&servicename=DesktopCentralCloud&serviceurl=https%3A%2F%2Fendpointcentral.localmanageengine.com%2Fwebclient&signupurl=https%3A%2F%2Fendpointcentral.localmanageengine.com%2Fsignup.html";
            try (OutputStream os = connection2.getOutputStream()) {
                byte[] input = params.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Fetch response headers from API 2
            Map<String, List<String>> headers2 = connection2.getHeaderFields();
            StringBuilder finalCookieBuilder = new StringBuilder();

            if (headers2.containsKey("Set-Cookie")) {
                for (String cookie : headers2.get("Set-Cookie")) {
                    String formattedCookie = cookie.split(";")[0]; // Extract only the key=value part
                    finalCookieBuilder.append(formattedCookie).append(";");
                }
            }

            finalCookieBuilder.append(" httponly; secure;");
            System.out.println(finalCookieBuilder.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
