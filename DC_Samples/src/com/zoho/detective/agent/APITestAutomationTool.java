package com.zoho.detective.agent;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import org.json.*;

public class APITestAutomationTool {
    private static final String BASE_URL = "https://detective.localzoho.com/portalapi/uems/qa";
    private static final Map<String, String> HEADERS = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    private static final String[] SECURITY_TEST_USERS = {
            "ruthna.s+get_osd@zohotest.com",
            "ruthna.s+get_rap@zohotest.com",
            "ruthna.s+get_acp@zohotest.com",
            "ruthna.s.get_invalid@zohotest.com"
    };

    static {
        HEADERS.put("Host", "detective.localzoho.com");
        HEADERS.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:137.0) Gecko/20100101 Firefox/137.0");
        HEADERS.put("Accept", "*/*");
        HEADERS.put("Accept-Language", "en-US,en;q=0.5");
        HEADERS.put("Accept-Encoding", "gzip, deflate, br, zstd");
        HEADERS.put("Referer", "https://detective.localzoho.com/portal/uems/app");
        HEADERS.put("Content-Type", "application/json");
        HEADERS.put("Connection", "keep-alive");
        HEADERS.put("Cookie", "detective-username=ruthna.s; zabUserId=1740651575830zabu0.24997422121252222; zabBotScore=100; DSESSIONID=DSESSIONID101d00bbed29e47d5f27bdee42011cc6; zalb_bd8611aa93=3d965648accc61668ec2b7561d5b3f6b; zfocscook=5bd5289acab3feabb95b4e6061de08fc00354ff37bbb84e7796426f880c72adbd314cc901f49e21067ac0416082173c21c22348f453382c64cca142d3ed71ad6; _zcsr_tmp=5bd5289acab3feabb95b4e6061de08fc00354ff37bbb84e7796426f880c72adbd314cc901f49e21067ac0416082173c21c22348f453382c64cca142d3ed71ad6; JSESSIONID=1794EDA0B7CD94B8704332AA00BD06FD; zalb_bb65c58de0=ffc5122861141a93a75033ef47ad81b1; notification_session_id=a373a73c1z; _iamadt=6263ea3d5a7d415a5dbf67a515a6d847bbd68ffb785a850070eb75d73eb58c4fc4e80b8cfd25e426e069391f9044eccb; _iambdt=b4a663b51e90ff505f3f5ea131b301cf119a26361cafa234377e4b08fc016fc316ac88d1cd03f614b34f2eee75e631ee9446b002150c9c4e9c851e872089f651; zsc6868ae0676ff401e8d59414388870027=1747293643923zsc0.48094545073560824; zft-sdc=isef%3Dtrue-isfr%3Dtrue-source%3Done.zoho.com; wms-tkp-token=70837860-47819838-b0cab7ff76c68638760c8749a5717ae3; zps-tgr-dts=sc%3D92-expAppOnNewSession%3D%5B%5D-pc%3D3-sesst%3D1747293643924");
        HEADERS.put("Sec-Fetch-Dest", "empty");
        HEADERS.put("Sec-Fetch-Mode", "cors");
        HEADERS.put("Sec-Fetch-Site", "same-origin");
        HEADERS.put("Priority", "u=0");
    }

    public static void main(String[] args) {
        System.out.println("API Test Automation Tool");
        System.out.println("=======================");
        System.out.println("1. Parameterized Testing");
        System.out.println("2. Security Testing (User Email)");
        System.out.print("Select testing type (1 or 2): ");

        int testType = 1;
        try {
            testType = Integer.parseInt(scanner.nextLine());
            if (testType < 1 || testType > 2) {
                System.out.println("Invalid choice, defaulting to Parameterized Testing");
                testType = 1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input, defaulting to Parameterized Testing");
            testType = 1;
        }

        try {
            System.out.print("Enter Source TG ID: ");
            String sourceTgId = scanner.nextLine();

            System.out.print("Enter Source Testcase ID: ");
            String sourceTestId = scanner.nextLine();

            System.out.print("Enter Target TG ID: ");
            String targetTgId = scanner.nextLine();

            if (testType == 1) {
                executeParameterizedTesting(sourceTgId, sourceTestId, targetTgId);
            } else {
                executeSecurityTesting(sourceTgId, sourceTestId, targetTgId);
            }

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void executeParameterizedTesting(String sourceTgId, String sourceTestId, String targetTgId) throws IOException {
        System.out.println("\nStep 1: Copying test case...");
        String copyResponse = copyTestCase(sourceTgId, sourceTestId, targetTgId);

        System.out.println("\nStep 2: Getting copied test case ID...");
        String copiedTestId = getCopiedTestId(copyResponse);

        if (copiedTestId == null || copiedTestId.isEmpty()) {
            System.out.println("Failed to get copied test ID. Exiting...");
            return;
        }

        System.out.println("\nStep 3: Getting test case details...");
        JSONObject testDetails = getTestDetails(copiedTestId);

        System.out.println("\nTest Details:");
        System.out.println("API ID: " + testDetails.optString("api_id", "N/A"));
        System.out.println("Test ID: " + testDetails.optString("test_id", "N/A"));

        System.out.println("\nAvailable Parameters:");
        displayParams(testDetails);

        System.out.println("\nAvailable Request Data:");
        displayRequestData(testDetails);

        System.out.println("\nStep 4: Select what to modify:");
        System.out.println("1. Parameters only");
        System.out.println("2. Request data only");
        System.out.println("3. Both parameters and request data");
        System.out.print("Enter your choice (1-3): ");

        int modifyChoice = 1;
        try {
            modifyChoice = Integer.parseInt(scanner.nextLine());
            if (modifyChoice < 1 || modifyChoice > 3) {
                System.out.println("Invalid choice, defaulting to Parameters only");
                modifyChoice = 1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input, defaulting to Parameters only");
            modifyChoice = 1;
        }

        boolean modifyParams = (modifyChoice == 1 || modifyChoice == 3);
        boolean modifyRequestData = (modifyChoice == 2 || modifyChoice == 3);

        if (modifyParams && !testDetails.isNull("params")) {
            System.out.println("\nModifying parameters (type=2)...");
            modifySelectedParams(copiedTestId, targetTgId, testDetails.get("params"));
        } else if (modifyParams) {
            System.out.println("No parameters to modify");
        }

        if (modifyRequestData && !testDetails.isNull("request_data")) {
            System.out.println("\nModifying request data (type=3)...");
            modifySelectedRequestData(copiedTestId, targetTgId, testDetails.get("request_data"));
        } else if (modifyRequestData) {
            System.out.println("No request data to modify");
        }

        System.out.println("\nParameterized testing completed successfully!");
    }

    private static void executeSecurityTesting(String sourceTgId, String sourceTestId, String targetTgId) throws IOException {
        System.out.println("\nGetting original test details...");
        JSONObject originalTestDetails = getTestDetails(sourceTestId);

        if (originalTestDetails.isNull("params")) {
            System.out.println("No parameters found in test details. Cannot perform security testing.");
            return;
        }

        Map<String, String> paramsMap = parseParamsToMap(originalTestDetails.get("params"));
        boolean hasUserEmail = paramsMap.containsKey("user_email");

        if (!hasUserEmail) {
            System.out.println("No user_email parameter found in test case. Cannot perform security testing.");
            return;
        }

        System.out.println("\nStarting Security Testing with different user emails...");

        for (String testUser : SECURITY_TEST_USERS) {
            String copyResponse = copyTestCase(sourceTgId, sourceTestId, targetTgId);
            String newTestId = getCopiedTestId(copyResponse);

            if (newTestId == null || newTestId.isEmpty()) {
                System.out.println("Failed to get copied test ID for user " + testUser);
                continue;
            }

            JSONObject testDetails = getTestDetails(newTestId);
            if (testDetails == null) {
                System.out.println("Failed to get details for new test case");
                continue;
            }

            Object paramsObj = testDetails.get("params");
            Map<String, String> newParamsMap = parseParamsToMap(paramsObj);

            newParamsMap.put("user_email", testUser);

            updateTestParams(newTestId, paramsObj, newParamsMap, "Security test with user: " + testUser);
            System.out.println("Created test case " + newTestId + " for user " + testUser);
        }

        System.out.println("\nSecurity testing completed successfully!");
    }

    private static String copyTestCase(String sourceTgId, String sourceTestId, String targetTgId) throws IOException {
        String url = BASE_URL + "/test/" + sourceTgId + "/copy?copy_to_new_tg=false&paste_tg_id=" + targetTgId + "&testgroup_name=&user_name=ruthna.s";

        JSONObject requestBody = new JSONObject();
        requestBody.put("test_ids", sourceTestId);

        return sendPostRequest(url, requestBody.toString());
    }

    private static String getCopiedTestId(String copyResponse) throws IOException {
        try {
            JSONObject jsonResponse = new JSONObject(copyResponse);
            if (jsonResponse.has("data")) {
                JSONObject data = jsonResponse.getJSONObject("data");
                if (data.has("copied_test_ids")) {
                    JSONArray copiedTestIds = data.getJSONArray("copied_test_ids");
                    if (copiedTestIds.length() > 0) {
                        return copiedTestIds.getString(0);
                    }
                } else if (data.has("test_id")) {
                    // Fallback in case response format is different
                    return data.getString("test_id");
                }
            }
        } catch (JSONException e) {
            System.out.println("Failed to parse copy response as JSON, trying pattern matching");
            // Fallback to pattern matching if JSON parsing fails
            Pattern pattern = Pattern.compile("\"copied_test_ids\"\\s*:\\s*\\[\"([^\"]+)\"\\]");
            Matcher matcher = pattern.matcher(copyResponse);
            if (matcher.find()) {
                return matcher.group(1);
            }

            // Try alternative pattern if the first one fails
            pattern = Pattern.compile("\"test_id\"\\s*:\\s*\"(\\d+)\"");
            matcher = pattern.matcher(copyResponse);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        // If all else fails, return null or throw an exception
        System.out.println("Could not extract test ID from copy response");
        return null;
    }

    private static JSONObject getTestDetails(String testId) throws IOException {
        String url = BASE_URL + "/results/tests/" + testId;
        String response = sendGetRequest(url);
        JSONObject jsonResponse = new JSONObject(response);
        JSONObject data = jsonResponse.getJSONObject("data");

        Object testsObj = data.get("tests");

        if (testsObj instanceof JSONArray) {
            JSONArray testsArray = (JSONArray) testsObj;
            for (int i = 0; i < testsArray.length(); i++) {
                JSONObject test = testsArray.getJSONObject(i);
                if (test.getString("test_id").equals(testId)) {
                    return test;
                }
            }
        } else if (testsObj instanceof JSONObject) {
            return (JSONObject) testsObj;
        }

        throw new IOException("Test details not found for ID: " + testId);
    }

    private static void displayParams(JSONObject testDetails) {
        if (testDetails.isNull("params")) {
            System.out.println("No parameters found in test details");
            return;
        }

        Object paramsObj = testDetails.get("params");

        if (paramsObj instanceof JSONObject) {
            JSONObject params = (JSONObject) paramsObj;
            System.out.println("Parameters (JSON format):");
            for (String key : params.keySet()) {
                System.out.println(key + ": " + params.get(key));
            }
        } else if (paramsObj instanceof String) {
            String paramsStr = (String) paramsObj;
            System.out.println("Parameters (query string format):");
            System.out.println(paramsStr);

            try {
                System.out.println("\nParsed parameters:");
                String[] pairs = paramsStr.split("&");
                for (String pair : pairs) {
                    int idx = pair.indexOf("=");
                    String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
                    String value = idx > 0 && pair.length() > idx + 1 ?
                            URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : "";
                    System.out.println(key + ": " + value);
                }
            } catch (UnsupportedEncodingException e) {
                System.out.println("Failed to decode parameters: " + e.getMessage());
            }
        } else {
            System.out.println("Unknown parameters format: " + paramsObj.getClass().getName());
        }
    }

    private static void displayRequestData(JSONObject testDetails) {
        if (testDetails.isNull("request_data")) {
            System.out.println("No request data found");
            return;
        }

        Object requestDataObj = testDetails.get("request_data");

        if (requestDataObj instanceof JSONObject) {
            System.out.println("Request Data (JSON format):");
            System.out.println(((JSONObject) requestDataObj).toString(2));
        } else if (requestDataObj instanceof String) {
            String requestDataStr = (String) requestDataObj;

            if (requestDataStr.trim().startsWith("{")) {
                try {
                    JSONObject jsonData = new JSONObject(requestDataStr);
                    System.out.println("Request Data (JSON format):");
                    System.out.println(jsonData.toString(2));
                } catch (JSONException e) {
                    System.out.println("Request Data (form data format):");
                    System.out.println(requestDataStr);

                    try {
                        System.out.println("\nParsed form data:");
                        String[] pairs = requestDataStr.split("&");
                        for (String pair : pairs) {
                            int idx = pair.indexOf("=");
                            String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
                            String value = idx > 0 && pair.length() > idx + 1 ?
                                    URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : "";
                            System.out.println(key + ": " + value);
                        }
                    } catch (UnsupportedEncodingException ex) {
                        System.out.println("Failed to decode form data: " + ex.getMessage());
                    }
                }
            } else {
                System.out.println("Request Data (form data format):");
                System.out.println(requestDataStr);

                try {
                    System.out.println("\nParsed form data:");
                    String[] pairs = requestDataStr.split("&");
                    for (String pair : pairs) {
                        int idx = pair.indexOf("=");
                        String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
                        String value = idx > 0 && pair.length() > idx + 1 ?
                                URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : "";
                        System.out.println(key + ": " + value);
                    }
                } catch (UnsupportedEncodingException ex) {
                    System.out.println("Failed to decode form data: " + ex.getMessage());
                }
            }
        } else {
            System.out.println("Unknown request data format: " + requestDataObj.getClass().getName());
        }
    }

    private static void modifySelectedParams(String originalTestId, String targetTgId, Object originalParams) throws IOException {
        Map<String, String> paramsMap = parseParamsToMap(originalParams);

        if (paramsMap.isEmpty()) {
            System.out.println("No parameters available to modify");
            return;
        }

        System.out.println("\nSelect parameters to modify (comma-separated numbers):");
        List<String> paramKeys = new ArrayList<>(paramsMap.keySet());
        for (int i = 0; i < paramKeys.size(); i++) {
            System.out.println((i + 1) + ". " + paramKeys.get(i) + ": " + paramsMap.get(paramKeys.get(i)));
        }

        System.out.print("\nEnter parameter numbers to modify (e.g., 1,3): ");
        String selection = scanner.nextLine();
        String[] selectedNumbers = selection.split(",");

        Set<String> selectedParams = new HashSet<>();
        for (String numStr : selectedNumbers) {
            try {
                int num = Integer.parseInt(numStr.trim());
                if (num > 0 && num <= paramKeys.size()) {
                    selectedParams.add(paramKeys.get(num - 1));
                }
            } catch (NumberFormatException e) {
                // Ignore invalid numbers
            }
        }

        if (selectedParams.isEmpty()) {
            System.out.println("No valid parameters selected");
            return;
        }

        System.out.println("\nSelect data type to test:");
        System.out.println("1. String");
        System.out.println("2. Integer (0)");
        System.out.println("3. Float (0.0)");
        System.out.println("4. Long (1234567890987654321)");
        System.out.println("5. Double (1.23456789)");
        System.out.println("6. Special characters (!@#$)");
        System.out.println("all. Test all data types (creates new test cases for each)");
        System.out.print("\nEnter data type number or 'all': ");

        String dataTypeChoice = scanner.nextLine().trim().toLowerCase();

        if (dataTypeChoice.equals("all")) {
            testAllDataTypes(originalTestId, targetTgId, paramsMap, selectedParams);
        } else {
            int choice;
            try {
                choice = Integer.parseInt(dataTypeChoice);
                if (choice < 1 || choice > 6) {
                    System.out.println("Invalid choice, using String by default");
                    choice = 1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, using String by default");
                choice = 1;
            }

            String testValue = getTestValueForDataType(choice);
            for (String param : selectedParams) {
                paramsMap.put(param, testValue);
            }

            updateTestParams(originalTestId, originalParams, paramsMap, "Modified with data type " + choice);
        }
    }

    private static void modifySelectedRequestData(String originalTestId, String targetTgId, Object originalRequestData) throws IOException {
        System.out.println("\nAnalyzing request data format...");

        RequestDataInfo requestDataInfo = parseRequestData(originalRequestData);

        if (requestDataInfo.isEmpty()) {
            System.out.println("No valid request data found to modify");
            return;
        }

        System.out.println("\nRequest Data Fields:");
        List<String> fieldPaths = new ArrayList<>();

        if (requestDataInfo.isJson) {
            collectJsonFieldPaths(requestDataInfo.jsonData, "", fieldPaths);
        } else {
            for (String key : requestDataInfo.formData.keySet()) {
                fieldPaths.add(key);
            }
        }

        for (int i = 0; i < fieldPaths.size(); i++) {
            System.out.println((i + 1) + ". " + fieldPaths.get(i));
        }

        System.out.print("\nEnter field numbers to modify (comma-separated): ");
        String selection = scanner.nextLine();
        String[] selectedNumbers = selection.split(",");

        Set<String> selectedFields = new HashSet<>();
        for (String numStr : selectedNumbers) {
            try {
                int num = Integer.parseInt(numStr.trim());
                if (num > 0 && num <= fieldPaths.size()) {
                    selectedFields.add(fieldPaths.get(num - 1));
                }
            } catch (NumberFormatException e) {
                // Ignore invalid numbers
            }
        }

        if (selectedFields.isEmpty()) {
            System.out.println("No valid fields selected");
            return;
        }

        System.out.println("\nSelect data type to test:");
        System.out.println("1. String");
        System.out.println("2. Integer (0)");
        System.out.println("3. Float (0.0)");
        System.out.println("4. Long (1234567890987654321)");
        System.out.println("5. Double (1.23456789)");
        System.out.println("6. Special characters (!@#$)");
        System.out.println("all. Test all data types (creates new test cases for each)");
        System.out.print("\nEnter data type number or 'all': ");

        String dataTypeChoice = scanner.nextLine().trim().toLowerCase();

        if (dataTypeChoice.equals("all")) {
            testAllRequestDataTypes(originalTestId, targetTgId, requestDataInfo, selectedFields);
        } else {
            int choice;
            try {
                choice = Integer.parseInt(dataTypeChoice);
                if (choice < 1 || choice > 6) {
                    System.out.println("Invalid choice, using String by default");
                    choice = 1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, using String by default");
                choice = 1;
            }

            String testValue = getTestValueForDataType(choice);
            RequestDataInfo modifiedRequestData = modifyRequestDataFields(requestDataInfo, selectedFields, testValue);

            updateTestRequestData(originalTestId, modifiedRequestData, "Modified with data type " + choice);
        }
    }

    private static class RequestDataInfo {
        boolean isJson;
        JSONObject jsonData;
        Map<String, String> formData;
        String originalData;

        boolean isEmpty() {
            return !isJson && (formData == null || formData.isEmpty());
        }
    }

    private static RequestDataInfo parseRequestData(Object requestData) {
        RequestDataInfo info = new RequestDataInfo();

        if (requestData == null || requestData.toString().isEmpty()) {
            return info;
        }

        info.originalData = requestData.toString();

        try {
            // First try to parse as JSON
            if (requestData instanceof JSONObject) {
                info.isJson = true;
                info.jsonData = (JSONObject) requestData;
                return info;
            } else if (requestData instanceof String) {
                String dataStr = (String) requestData;
                if (dataStr.trim().startsWith("{")) {
                    info.isJson = true;
                    info.jsonData = new JSONObject(dataStr);
                    return info;
                } else {
                    info.isJson = false;
                    info.formData = new LinkedHashMap<>();

                    String[] pairs = dataStr.split("&");
                    for (String pair : pairs) {
                        int idx = pair.indexOf("=");
                        String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
                        String value = idx > 0 && pair.length() > idx + 1 ?
                                URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : "";
                        info.formData.put(key, value);
                    }
                    return info;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to parse request data: " + e.getMessage());
        }

        return info;
    }

    private static void collectJsonFieldPaths(JSONObject json, String currentPath, List<String> fieldPaths) {
        for (String key : json.keySet()) {
            String newPath = currentPath.isEmpty() ? key : currentPath + "." + key;
            Object value = json.get(key);

            if (value instanceof JSONObject) {
                collectJsonFieldPaths((JSONObject) value, newPath, fieldPaths);
            } else {
                fieldPaths.add(newPath);
            }
        }
    }

    private static RequestDataInfo modifyRequestDataFields(RequestDataInfo original, Set<String> fieldPaths, String newValue) {
        RequestDataInfo modified = new RequestDataInfo();
        modified.isJson = original.isJson;

        if (original.isJson) {
            modified.jsonData = new JSONObject(original.jsonData.toString());

            for (String path : fieldPaths) {
                String[] parts = path.split("\\.");
                JSONObject current = modified.jsonData;

                for (int i = 0; i < parts.length - 1; i++) {
                    if (!current.has(parts[i])) {
                        break;
                    }
                    current = current.getJSONObject(parts[i]);
                }

                if (current.has(parts[parts.length - 1])) {
                    current.put(parts[parts.length - 1], newValue);
                }
            }
        } else {
            modified.formData = new LinkedHashMap<>(original.formData);

            for (String field : fieldPaths) {
                if (modified.formData.containsKey(field)) {
                    modified.formData.put(field, newValue);
                }
            }
        }

        return modified;
    }

    private static void testAllRequestDataTypes(String originalTestId, String targetTgId,
                                                RequestDataInfo originalRequestData, Set<String> selectedFields) throws IOException {
        String[] dataTypes = {"String", "Integer", "Float", "Long", "Double", "Special Characters"};
        String[] testValues = {
                "testString",        // String
                "0",                 // Integer
                "0.0",               // Float
                "1234567890987654321", // Long
                "1.23456789",        // Double
                "!@#$"               // Special Characters
        };

        for (int i = 0; i < dataTypes.length; i++) {
            String newTestId = createNewTestCase(originalTestId, targetTgId);
            if (newTestId == null) {
                System.out.println("Failed to create test case for " + dataTypes[i]);
                continue;
            }

            JSONObject testDetails = getTestDetails(newTestId);
            if (testDetails == null) {
                System.out.println("Failed to get details for new test case");
                continue;
            }

            Object requestDataObj = testDetails.get("request_data");
            RequestDataInfo requestDataInfo = parseRequestData(requestDataObj);

            RequestDataInfo modifiedRequestData = modifyRequestDataFields(requestDataInfo, selectedFields, testValues[i]);

            updateTestRequestData(newTestId, modifiedRequestData, "Request data test: " + dataTypes[i]);
            System.out.println("Created test case " + newTestId + " for " + dataTypes[i]);
        }
    }

    private static String createNewTestCase(String sourceTestId, String targetTgId) throws IOException {
        String url = BASE_URL + "/test/" + targetTgId + "/copy?copy_to_new_tg=false&paste_tg_id=" + targetTgId + "&testgroup_name=&user_name=ruthna.s";

        JSONObject requestBody = new JSONObject();
        requestBody.put("test_ids", sourceTestId);

        String response = sendPostRequest(url, requestBody.toString());
//        try {
//            JSONObject jsonResponse = new JSONObject(response);
//            if (jsonResponse.has("data")) {
//                JSONObject data = jsonResponse.getJSONObject("data");
//                if (data.has("test_id")) {
//                    return data.getString("test_id");
//                }
//            }
//        } catch (JSONException e) {
//            System.out.println("Non-JSON response received, attempting to extract test ID directly");
//            Pattern pattern = Pattern.compile("\"test_id\"\\s*:\\s*\"(\\d+)\"");
//            Matcher matcher = pattern.matcher(response);
//            if (matcher.find()) {
//                return matcher.group(1);
//            }
//        }
        return getCopiedTestId(response);
    }

    private static String copyTestCase(String sourceTestId, String targetTgId) throws IOException {
        String url = BASE_URL + "/test/" + targetTgId + "/copy?copy_to_new_tg=false&paste_tg_id=" + targetTgId + "&testgroup_name=&user_name=ruthna.s";

        JSONObject requestBody = new JSONObject();
        requestBody.put("test_ids", sourceTestId);

        String response = sendPostRequest(url, requestBody.toString());

//        try {
//            JSONObject jsonResponse = new JSONObject(response);
//            if (jsonResponse.has("data")) {
//                JSONObject data = jsonResponse.getJSONObject("data");
//                if (data.has("test_id")) {
//                    return data.getString("test_id");
//                }
//            }
//        } catch (JSONException e) {
//            System.out.println("Failed to parse copy response: " + e.getMessage());
//        }

        return getCopiedTestId(response);
    }

    private static void updateTestParams(String testId, Object originalParams,
                                         Map<String, String> paramsMap, String description) throws IOException {
        String modifiedParamsStr;
        if (originalParams instanceof JSONObject) {
            JSONObject modifiedParams = new JSONObject();
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                modifiedParams.put(entry.getKey(), entry.getValue());
            }
            modifiedParamsStr = jsonToQueryString(modifiedParams);
        } else {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                if (sb.length() > 0) sb.append("&");
                sb.append(entry.getKey()).append("=").append(entry.getValue());
            }
            modifiedParamsStr = sb.toString();
        }

        JSONObject requestBody = new JSONObject();
        requestBody.put("value", modifiedParamsStr);
        requestBody.put("description", description);

        String url = "https://detective.localzoho.com/portalapi/uems/totido/tests/request/modify?test_id=" + testId + "&type=2&test_type=get&user_name=ruthna.s";
        String response = sendPostRequest(url, requestBody.toString());
    }

    private static void updateTestRequestData(String testId, RequestDataInfo requestData, String description) throws IOException {
        JSONObject requestBody = new JSONObject();

        if (requestData.isJson) {
            requestBody.put("value", requestData.jsonData.toString());
        } else {
            // For form data, we need to construct the key-value pairs string
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : requestData.formData.entrySet()) {
                if (sb.length() > 0) sb.append("&");
                sb.append(entry.getKey()).append("=").append(entry.getValue());
            }
            requestBody.put("value", sb.toString());
        }

        requestBody.put("description", description);

        String url = "https://detective.localzoho.com/portalapi/uems/totido/tests/request/modify?test_id=" + testId +
                "&type=3&test_type=get&user_name=ruthna.s";
        String response = sendPostRequest(url, requestBody.toString());

        //System.out.println("Modification response: " + response);
    }

    private static Map<String, String> parseParamsToMap(Object params) throws UnsupportedEncodingException {
        Map<String, String> paramsMap = new LinkedHashMap<>();

        if (params instanceof JSONObject) {
            JSONObject jsonParams = (JSONObject) params;
            for (String key : jsonParams.keySet()) {
                paramsMap.put(key, jsonParams.get(key).toString());
            }
        } else if (params instanceof String) {
            String[] pairs = ((String) params).split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
                String value = idx > 0 && pair.length() > idx + 1 ?
                        URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : "";
                paramsMap.put(key, value);
            }
        }
        return paramsMap;
    }

    private static void testAllDataTypes(String originalTestId, String targetTgId,
                                         Map<String, String> originalParams, Set<String> selectedParams) throws IOException {
        String[] dataTypes = {"String", "Integer", "Float", "Long", "Double", "Special Characters"};
        String[] testValues = {
                "testString",        // String
                "0",                 // Integer
                "0.0",               // Float
                "1234567890987654321", // Long
                "1.23456789",        // Double
                "!@#$"               // Special Characters
        };

        for (int i = 0; i < dataTypes.length; i++) {
            String newTestId = copyTestCase(originalTestId, targetTgId);
            if (newTestId == null) {
                System.out.println("Failed to create test case for " + dataTypes[i]);
                continue;
            }

            JSONObject testDetails = getTestDetails(newTestId);
            if (testDetails == null) {
                System.out.println("Failed to get details for new test case");
                continue;
            }

            Object paramsObj = testDetails.get("params");
            Map<String, String> paramsMap = parseParamsToMap(paramsObj);

            for (String param : selectedParams) {
                paramsMap.put(param, testValues[i]);
            }

            updateTestParams(newTestId, paramsObj, paramsMap, "Data type test: " + dataTypes[i]);
            System.out.println("Created test case " + newTestId + " for " + dataTypes[i]);
        }
    }

    private static String getTestValueForDataType(int choice) {
        switch (choice) {
            case 1: return "testString";
            case 2: return "0";
            case 3: return "0.0";
            case 4: return "1234567890987654321";
            case 5: return "1.23456789";
            case 6: return "!@#$";
            default: return "testString";
        }
    }

    private static String jsonToQueryString(JSONObject json) {
        StringBuilder sb = new StringBuilder();
        for (String key : json.keySet()) {
            if (sb.length() > 0) sb.append("&");
            sb.append(key).append("=").append(json.get(key).toString());
        }
        return sb.toString();
    }

    private static String sendGetRequest(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        for (Map.Entry<String, String> entry : HEADERS.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }

        connection.setRequestProperty("Accept-Encoding", "gzip");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream;
            if ("gzip".equalsIgnoreCase(connection.getContentEncoding())) {
                inputStream = new GZIPInputStream(connection.getInputStream());
            } else {
                inputStream = connection.getInputStream();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            throw new IOException("GET request failed with response code: " + responseCode);
        }
    }

    private static String sendPostRequest(String url, String requestBody) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        for (Map.Entry<String, String> entry : HEADERS.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream;
            if ("gzip".equalsIgnoreCase(connection.getContentEncoding())) {
                inputStream = new GZIPInputStream(connection.getInputStream());
            } else {
                inputStream = connection.getInputStream();
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } else {
            throw new IOException("POST request failed with response code: " + responseCode);
        }
    }
}