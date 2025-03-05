import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class MultiUserLogin {
    static String detectiveServerURL = "https://detective.localzoho.com/";
    static String ecServerURL = "endpointcentral.localmanageengine.com/";
    public static void main(String[] args) {
        List<String> emailIds = new ArrayList<>();
        String emailListUrl = detectiveServerURL+"portalapi/uems/admin/test/user/list?test_type=get";

        try {
            HttpURLConnection connection = createConnection(emailListUrl, "GET");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                JSONObject jsonResponse = new JSONObject(responseBuilder.toString());
                JSONObject dataObject = jsonResponse.getJSONObject("data");
                JSONArray userList = dataObject.getJSONArray("user_list");
                for (int i = 0; i < userList.length(); i++) {
                    JSONObject user = userList.getJSONObject(i);
                    String email = user.getString("email");
                    if (email.startsWith("ems-testingautomation+get_inv")) {
                        emailIds.add(email);
                    }
                }
            }
            System.out.println("Filtered Email IDs: " + emailIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String email : emailIds) {
            String password = "";
            if (email.equals("ems-testingautomation+get_patch37@zohotest.com") || email.equals("ems-testingautomation+get_patch36@zohotest.com") || email.equals("ems-testingautomation+get_patch41@zohotest.com") ) {
                password = "Xm7W8Jnao7?Au1";
            } else if (email.startsWith("ems-testingautomation")) {
                password = "Testing@123#";
            } else {
                System.out.println("No password rule for email: " + email + ". Skipping.");
                continue;
            }
            System.out.println("Processing login for: " + email);
            try {
                loginUser(email, password);
            } catch (Exception e) {
                System.out.println("Exception while processing " + email);
                e.printStackTrace();
            }
            System.out.println("========================================");
        }
    }
    private static void loginUser(String email, String password) throws Exception {
        String apiUrl1 = "https://accounts.localzoho.com/signin?servicename=DesktopCentralCloud&serviceurl=https%3A%2F%2F"+ecServerURL+"%2Fwebclient&signupurl=https://"+ecServerURL+"/signup.html";
        HttpURLConnection connection1 = createConnection(apiUrl1, "GET");

        Map<String, List<String>> headers1 = connection1.getHeaderFields();
        String cookiesFromApi1 = extractCookies(headers1);
        String iamcsrToken = extractIamcsrToken(headers1);

        String apiUrl2 = "https://accounts.localzoho.com/signin/v2/lookup/" + email;
        HttpURLConnection connection2 = createConnection(apiUrl2, "POST");

        connection2.setRequestProperty("X-ZCSRF-TOKEN", "iamcsrcoo=" + iamcsrToken);
        connection2.setRequestProperty("Cookie", cookiesFromApi1);
        connection2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        String postData = "mode=primary&cli_time=1738850279163&servicename=DesktopCentralCloud&serviceurl=https%3A%2F%2F"+ecServerURL+"%2Fwebclient&signupurl=https%3A%2F%2F"+ecServerURL+"/signup.html";
        sendPostData(connection2, postData);

        Map<String, List<String>> headers2 = connection2.getHeaderFields();
        String cookiesFromApi2 = extractCookies(headers2);
        String api2Response = readResponse(connection2);
        JSONObject api2JsonResponse = new JSONObject(api2Response);

        JSONObject lookupObject = api2JsonResponse.getJSONObject("lookup");
        String digest = lookupObject.getString("digest");
        String identifier = lookupObject.getString("identifier");


        String apiUrl3 = "https://accounts.localzoho.com/getPayloadEncryptionPublicKey";
        HttpURLConnection connection3 = createConnection(apiUrl3, "POST");

        connection3.setRequestProperty("X-ZCSRF-TOKEN", "iamcsrcoo=" + iamcsrToken);
        connection3.setRequestProperty("Cookie", cookiesFromApi1);
        connection3.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        String postData3 = "scope=common&iamcsrcoo=" + iamcsrToken + "&isSystemTimeNeeded=true";
        sendPostData(connection3, postData3);

        String publicKeyResponse = readResponse(connection3);
        JSONObject publicKeyData = new JSONObject(publicKeyResponse);

        List<String> encryptedData = encrypt(password, publicKeyData);

        String apiUrl4 = "https://accounts.localzoho.com/signin/v2/primary/" + identifier + "/password?digest=" + digest
                + "&cli_time=1738850279163&servicename=DesktopCentralCloud&serviceurl=https://"+ecServerURL+"/webclient&signupurl=https://"+ecServerURL+"/signup.html";
        HttpURLConnection connection4 = createConnection(apiUrl4, "POST");

        connection4.setRequestProperty("X-ZCSRF-TOKEN", "iamcsrcoo=" + iamcsrToken);
        connection4.setRequestProperty("Cookie", cookiesFromApi1);
        connection4.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        if(encryptedData.size()>1) {
            connection4.setRequestProperty("waf-encryption-key", encryptedData.get(1));
        }
        connection4.setRequestProperty("origin", "https://accounts.localzoho.com");
        connection4.setRequestProperty("host", "accounts.localzoho.com");
        connection4.setRequestProperty("referer", "https://accounts.localzoho.com/signin?servicename=DesktopCentralCloud&signupurl=https://"+ecServerURL+":443/signup.html&serviceurl=https%3A%2F%2F"+ecServerURL+"%2Fwebclient");

        String postData4 = "{\"passwordauth\":{\"password\":\"" + encryptedData.get(0) + "\"}}";
        sendPostData(connection4, postData4);

        Map<String, List<String>> headers4 = connection4.getHeaderFields();
        String iamadtCookie = extractSpecificCookie(headers4, "_iamadt", 1);
        String iambdtCookie = extractSpecificCookie(headers4, "_iambdt", 1);
        String zalb_ = extractSpecificCookie(headers4, "zalb_", 1);
        String iamCookies = iamadtCookie + iambdtCookie + zalb_;

        String endpointCsrfToken = extractCookieValueFromCookieString(iamCookies, "_zcsr_tmp");

        // API 4.1 - Authorize Domain
        String apiUrl4_1 = "https://accounts.localzoho.com/authorize-domain?serviceurl=https%3A%2F%2F" + ecServerURL + "%2Fwebclient&servicename=DesktopCentralCloud";
        HttpURLConnection connection4_1 = createConnection(apiUrl4_1, "GET");
        connection4_1.setRequestProperty("Cookie", iamCookies);

        // Extract cookies from API 4.1 response
        Map<String, List<String>> headers4_1 = connection4_1.getHeaderFields();
        String cookiesFromApi4_1 = extractCookies(headers4_1);
        System.out.println("Cookies from API 4.1: " + cookiesFromApi4_1);
        String iamcsr_trust = extractSpecificCookie(headers4_1, "iamcsr", 1);
        String _zcsr_tmp_trust = iamcsr_trust.replace("iamcsr","_zcsr_tmp");
        String temp_iamCookies = iamCookies+iamcsr_trust+_zcsr_tmp_trust;
        String accountsCsrfToken = extractCookieValueFromCookieString(temp_iamCookies, "_zcsr_tmp");
        System.out.println("Cookies from API 4.1 iamcsr_trust : " + iamcsr_trust);

        //API 4.2 - Trusted Domain
        String apiUrl4_2 = "https://accounts.localzoho.com/webclient/v1/account/self/user/self/trusteddomain";
        HttpURLConnection connection4_2 = createConnection(apiUrl4_2, "POST");

        // Set request headers for API 4.2
        connection4_2.setRequestProperty("Cookie", temp_iamCookies);
        connection4_2.setRequestProperty("Referer", "https://" + ecServerURL);
        connection4_2.setRequestProperty("Origin", "https://" + ecServerURL);
        connection4_2.setRequestProperty("Host", ecServerURL);
        if (!accountsCsrfToken.isEmpty()) {
            connection4_2.setRequestProperty("X-ZCSRF-TOKEN", "iamcsrcoo=" + accountsCsrfToken);
        }

        // Set request body for API 4.2
        String postData4_2 = "{\"trusteddomain\":{\"serviceurl\":\"https://" + ecServerURL + "/webclient\",\"digest\":\"null\",\"domain\":\"" + ecServerURL + "\",\"servicename\":\"DesktopCentralCloud\",\"atd\":true}}";
        sendPostData(connection4_2, postData4_2);

        // Read response from API 4.2
        if(connection4_2.getResponseCode()!=400){
            String api4_2Response = readResponse(connection4_2);
        } else {
           System.out.println("Account " +email+ " already trusted the domain : "+ecServerURL);
        }

        String apiUrl5 = "https://"+ecServerURL+"/dcapi/org/orgDetail";
//        String apiUrl5 = "https://"+ecServerURL+"emsapi/productMeta";
        HttpURLConnection connection5 = createConnection(apiUrl5, "GET");

        connection5.setRequestProperty("Cookie", iamCookies);
        connection5.setRequestProperty("Referer", "https://"+ecServerURL+"");
        connection5.setRequestProperty("Origin", "https://"+ecServerURL+"");
        connection5.setRequestProperty("Host", ""+ecServerURL+"");
        if (!endpointCsrfToken.isEmpty()) {
            connection5.setRequestProperty("X-ZCSRF-TOKEN", "dcparamcsr=" + endpointCsrfToken);
        }
        connection5.setInstanceFollowRedirects(false);

        String api5Response = readResponse(connection5);

        Map<String, List<String>> headers5 = connection5.getHeaderFields();
        String dccookcsr = extractSpecificCookie(headers5, "dccookcsr", 1);
        String iamcsr = dccookcsr.replace("dccookcsr","iamcsr");
        String newZcsrTmp = extractSpecificCookie(headers5, "_zcsr_tmp", 1);
        String DMCloudLabVersion = extractSpecificCookie(headers5, "DMCloudLabVersion", 1);
        String browserCookies = dccookcsr + newZcsrTmp + iamcsr;
        String dcCookies = iamCookies + browserCookies + DMCloudLabVersion;
        String finalZcsrfToken = "";
        if (!newZcsrTmp.isEmpty()) {
            finalZcsrfToken = extractCookieValue(newZcsrTmp);
        }
        // System.out.println("Browser Cookies: " + browserCookies);
        System.out.println("================================================================================================");
        System.out.println("User Mail : " + email);
        System.out.println("Cookies : " + dcCookies);
        System.out.println("X-ZCSRF-TOKEN : dcparamcsr=" + finalZcsrfToken);
//        try (FileWriter csvWriter = new FileWriter("E:\\Cookies\\output.csv", true)) {
//            csvWriter.append(email)
//                    .append(",")
//                    .append(dcCookies)
//                    .append(",")
//                    .append(finalZcsrfToken)
//                    .append("\n");
//            csvWriter.flush();
//        }
        updateCookie(email,dcCookies);
        Thread.sleep(1000);
        System.out.println("================================================================================================");
    }

    private static HttpURLConnection createConnection(String url, String method) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        if ("POST".equals(method)) {
            connection.setDoOutput(true);
        }
        return connection;
    }

    private static String extractCookies(Map<String, List<String>> headers) {
        StringBuilder cookieBuilder = new StringBuilder();
        if (headers.containsKey("Set-Cookie")) {
            for (String cookie : headers.get("Set-Cookie")) {
                String formattedCookie = cookie.split(";")[0];
                cookieBuilder.append(formattedCookie).append("; ");
            }
        }
        return cookieBuilder.toString();
    }

    private static String extractIamcsrToken(Map<String, List<String>> headers) {
        if (headers.containsKey("Set-Cookie")) {
            for (String cookie : headers.get("Set-Cookie")) {
                if (cookie.startsWith("iamcsr=")) {
                    return cookie.split(";")[0].split("=")[1];
                }
            }
        }
        return "";
    }

    private static void sendPostData(HttpURLConnection connection, String postData) throws IOException {
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = postData.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
    }

    private static String readResponse(HttpURLConnection connection) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        return response.toString();
    }

    private static String extractSpecificCookie(Map<String, List<String>> headers, String cookieName, int matchNumber) {
        if (headers.containsKey("Set-Cookie")) {
            int count = 0;
            for (String cookie : headers.get("Set-Cookie")) {
                if (cookie.startsWith(cookieName + "=") || cookie.startsWith("zalb_")) {
                    count++;
                    if (count == matchNumber) {
                        return cookie.split(";")[0] + "; ";
                    }
                }
            }
        }
        return "";
    }

    private static String extractCookieValue(String cookie) {
        if (cookie != null && cookie.contains("=")) {
            return cookie.split("=")[1].split(";")[0].trim();
        }
        return "";
    }

    private static String extractCookieValueFromCookieString(String cookieString, String key) {
        String[] cookies = cookieString.split(";");
        for (String cookie : cookies) {
            cookie = cookie.trim();
            if (cookie.startsWith(key + "=")) {
                return cookie.substring((key + "=").length());
            }
        }
        return "";
    }

    public static List<String> encrypt(String data, JSONObject publicKeyData) {
        try {
            List<String> result = new ArrayList<>();
            if (publicKeyData.getBoolean("isPayloadEncryptionEnabled")) {
                KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                keyGen.init(256);
                SecretKey aesKey = keyGen.generateKey();
                byte[] key = aesKey.getEncoded();
                byte[] iv = new byte[12];
                SecureRandom random = new SecureRandom();
                random.nextBytes(iv);

                Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
                cipher.init(Cipher.ENCRYPT_MODE, aesKey, gcmSpec);
                byte[] encryptedDataBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
                String encryptedData = Base64.getEncoder().encodeToString(encryptedDataBytes);

                String publicKey = publicKeyData.getString("publicKey");
                byte[] encodedPublicKeyBytes = Base64.getDecoder().decode(publicKey);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey rsaPublicKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedPublicKeyBytes));

                byte[] aeskeyiv = new byte[9 + key.length + iv.length];
                ByteBuffer buffer = ByteBuffer.wrap(aeskeyiv);
                buffer.putLong(System.currentTimeMillis());
                buffer.put((byte) 45);
                buffer.put(key);
                buffer.put(iv);
                aeskeyiv = buffer.array();

                Cipher cipher2 = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
                OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1",
                        new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);
                cipher2.init(Cipher.ENCRYPT_MODE, rsaPublicKey, oaepParams);
                byte[] encryptedKeyAndIv = cipher2.doFinal(aeskeyiv);
                String encryptionKey = Base64.getEncoder().encodeToString(encryptedKeyAndIv);

                result.add(encryptedData);
                result.add(encryptionKey);
            } else {
                result.add(data);
            }
            return result;
        } catch (Exception e) {
            System.out.println("Exception while encrypting: " + e.getMessage());
        }
        return null;
    }
    public static void updateCookie(String mailID, String cookie) throws UnsupportedEncodingException {
        String baseUrl = detectiveServerURL+"portalapi/uems/admin/test/user/update";
        String testType = "get";
        String appConfigId = "23000000008005";
        String environmentId = "";

        String encodedMailId = URLEncoder.encode(mailID, "UTF-8");
        String encodedTestType = URLEncoder.encode(testType, "UTF-8");
        String encodedCookie = URLEncoder.encode(cookie, "UTF-8");
        String encodedAppConfigId = URLEncoder.encode(appConfigId, "UTF-8");
        String encodedEnvironmentId = URLEncoder.encode(environmentId, "UTF-8");
        String queryString = String.format("mail_id=%s&test_type=%s&cookie=%s&app_config_id=%s&environment_id=%s", encodedMailId, encodedTestType, encodedCookie, encodedAppConfigId, encodedEnvironmentId);
        String urlString = baseUrl + "?" + queryString;
        System.out.println("Request URL: " + urlString);
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code for Cookie Updation : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

           // System.out.println("Response: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
