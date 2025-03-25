package com.zoho.detective.agent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;
import java.io.FileWriter;
import java.io.IOException;

public class CompareTestStatusAcrossDatabases {

    // Configuration constants
    private static final String PREVIOUS_SCHEMA = "db487db";
    private static final String CURRENT_SCHEMA = "db485db";
    private static final String[] EXCLUDED_MODULES = {"MSP", "MDM", "EDR"};

    public static void main(String[] args) {
        // Database connection details
        DatabaseConfig previousDB = new DatabaseConfig(
                "jdbc:postgresql://uems-auto-k8s1.csez.zohocorpin.com:31866/sasdb",
                "root",
                "posgres",
                PREVIOUS_SCHEMA
        );

        DatabaseConfig currentDB = new DatabaseConfig(
                "jdbc:postgresql://uems-auto-k8s1.csez.zohocorpin.com:31364/sasdb",
                "root",
                "posgres",
                CURRENT_SCHEMA
        );

        // Data structures
        Map<String, Map<Long, String>> previousTagDBResults = new HashMap<>();
        Map<String, Map<Long, String>> currentTagDBResults = new HashMap<>();
        Map<String, Map<String, Set<Long>>> combinationWiseTestIds = new HashMap<>();

        // Fetch data
        fetchData(previousDB, previousTagDBResults);
        fetchData(currentDB, currentTagDBResults);

        // Summarize results
        Map<String, Map<String, Integer>> combinationWiseSummary = summarizeResults(
                previousTagDBResults,
                currentTagDBResults,
                combinationWiseTestIds
        );

        // 1. Print Overall Comparison Summary in descending order
        printOverallSummary(combinationWiseSummary);

        // 2. Print Combination-Module-Count summary to console
        printCombinationModuleCountSummary(combinationWiseSummary);

        // 3. Print Test IDs array grouped by combination
        printCombinationTestIdsArray(combinationWiseTestIds);

        // 4. Save Combination-wise summary with test IDs to CSV
        saveCombinationWiseSummaryToCSV(combinationWiseSummary, combinationWiseTestIds);
    }

    private static class DatabaseConfig {
        String url;
        String user;
        String password;
        String schema;

        public DatabaseConfig(String url, String user, String password, String schema) {
            this.url = url;
            this.user = user;
            this.password = password;
            this.schema = schema;
        }
    }

    private static void fetchData(DatabaseConfig dbConfig, Map<String, Map<Long, String>> results) {
        String query = buildQuery(dbConfig.schema);

        try (Connection conn = DriverManager.getConnection(dbConfig.url, dbConfig.user, dbConfig.password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("Connected to database: " + dbConfig.url);
            while (rs.next()) {
                long id = rs.getLong("id");
                String moduleName = rs.getString("module_name");
                String testStatus = rs.getString("teststatus");
                results.computeIfAbsent(moduleName, k -> new HashMap<>()).put(id, testStatus);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String buildQuery(String schema) {
        String exclusionClause = Arrays.stream(EXCLUDED_MODULES)
                .map(module -> "gtg.title not like '%" + module + "-%'")
                .collect(Collectors.joining(" and "));

        return "SELECT gt.id, " +
                "SPLIT_PART(gtg.title, '-', 1) AS module_name, " +
                "CASE " +
                "  WHEN gt.teststatus = 1 THEN 'Success' " +
                "  WHEN gt.teststatus IN (2, 3, 5) THEN 'Failure' " +
                "  WHEN gt.teststatus = 0 THEN 'Disable' " +
                "  WHEN gt.teststatus = 4 THEN 'NotRun' " +
                "  ELSE 'Unknown' " +
                "END AS teststatus " +
                "FROM " + schema + ".gettests gt " +
                "INNER JOIN " + schema + ".gettestgroup gtg ON gtg.testgroupid = gt.testgroupid " +
                "WHERE gtg.orgtype = 0 and " + exclusionClause;
    }

    private static Map<String, Map<String, Integer>> summarizeResults(
            Map<String, Map<Long, String>> previousTagDBResults,
            Map<String, Map<Long, String>> currentTagDBResults,
            Map<String, Map<String, Set<Long>>> combinationWiseTestIds) {

        Map<String, Map<String, Integer>> combinationWiseSummary = new HashMap<>();

        // Define all possible status combinations
        String[] previousStatuses = {"Success", "Failure", "Disable", "NotRun"};
        String[] currentStatuses = {"Success", "Failure", "Disable", "NotRun"};

        // Initialize structures for all combinations
        for (String prevStatus : previousStatuses) {
            for (String currStatus : currentStatuses) {
                String combination = prevStatus + "-" + currStatus;
                combinationWiseSummary.put(combination, new HashMap<>());
                combinationWiseTestIds.put(combination, new HashMap<>());
            }
        }

        // Process results
        for (Map.Entry<String, Map<Long, String>> moduleEntry : previousTagDBResults.entrySet()) {
            String module = moduleEntry.getKey();
            Map<Long, String> previousResults = moduleEntry.getValue();
            Map<Long, String> currentResults = currentTagDBResults.getOrDefault(module, new HashMap<>());

            for (Map.Entry<Long, String> entry : previousResults.entrySet()) {
                long id = entry.getKey();
                String previousStatus = entry.getValue();
                String currentStatus = currentResults.getOrDefault(id, "NotRun");
                String key = previousStatus + "-" + currentStatus;

                // Update count
                combinationWiseSummary.get(key).merge(module, 1, Integer::sum);

                // Store test ID
                combinationWiseTestIds.get(key)
                        .computeIfAbsent(module, k -> new HashSet<>())
                        .add(id);
            }
        }

        return combinationWiseSummary;
    }

    private static void printOverallSummary(Map<String, Map<String, Integer>> combinationWiseSummary) {
        System.out.println("\nOverall Comparison Summary (Sorted by Count in Descending Order):");

        // Calculate total counts for each combination
        Map<String, Integer> overallCounts = new HashMap<>();
        combinationWiseSummary.forEach((combination, moduleCounts) -> {
            int total = moduleCounts.values().stream().mapToInt(Integer::intValue).sum();
            overallCounts.put(combination, total);
        });

        // Sort and print
        overallCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }

    private static void printCombinationModuleCountSummary(Map<String, Map<String, Integer>> combinationWiseSummary) {
        System.out.println("\nCombination Module Count Summary:");
        System.out.println("--------------------------------");

        combinationWiseSummary.entrySet().stream()
                .sorted((e1, e2) -> {
                    int total1 = e1.getValue().values().stream().mapToInt(Integer::intValue).sum();
                    int total2 = e2.getValue().values().stream().mapToInt(Integer::intValue).sum();
                    return Integer.compare(total2, total1);
                })
                .forEach(combinationEntry -> {
                    String combination = combinationEntry.getKey();
                    Map<String, Integer> moduleCounts = combinationEntry.getValue();

                    System.out.println("\n");
                    System.out.println(combination);
                    moduleCounts.entrySet().stream()
                            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                            .forEach(moduleEntry -> {
                                System.out.println(moduleEntry.getKey()+" : "+moduleEntry.getValue());
//                                System.out.printf("%-15s\t%-20s\t%d%n",
//                                        combination,
//                                        moduleEntry.getKey(),
//                                        moduleEntry.getValue());
                            });
                });
    }

    private static void printCombinationTestIdsArray(Map<String, Map<String, Set<Long>>> combinationWiseTestIds) {
        System.out.println("\nTest IDs Array Grouped by Combination:");
        System.out.println("--------------------------------------");

        combinationWiseTestIds.entrySet().stream()
                .sorted((e1, e2) -> {
                    int total1 = e1.getValue().values().stream().mapToInt(Set::size).sum();
                    int total2 = e2.getValue().values().stream().mapToInt(Set::size).sum();
                    return Integer.compare(total2, total1);
                })
                .forEach(combinationEntry -> {
                    String combination = combinationEntry.getKey();
                    Map<String, Set<Long>> moduleTestIds = combinationEntry.getValue();

                    // Collect all test IDs across modules for this combination
                    List<Long> allTestIds = moduleTestIds.values().stream()
                            .flatMap(Set::stream)
                            .sorted()
                            .collect(Collectors.toList());

                    if (!allTestIds.isEmpty()) {
                        System.out.println(combination+" : "+allTestIds.size());
                    }
                });
    }

    private static void saveCombinationWiseSummaryToCSV(
            Map<String, Map<String, Integer>> combinationWiseSummary,
            Map<String, Map<String, Set<Long>>> combinationWiseTestIds) {

        String csvFile = "/Users/ruthna-12510-t/Documents/ReportComparizationSummary/2508vs2512/TestStatusComparisonSummary.csv";

        try (FileWriter writer = new FileWriter(csvFile)) {
            // Write CSV header
            writer.append("Combination,Total Count,Test IDs (All Modules)\n");

            // Sort combinations by total count in descending order
            combinationWiseSummary.entrySet().stream()
                    .sorted((e1, e2) -> {
                        int total1 = e1.getValue().values().stream().mapToInt(Integer::intValue).sum();
                        int total2 = e2.getValue().values().stream().mapToInt(Integer::intValue).sum();
                        return Integer.compare(total2, total1);
                    })
                    .forEach(combinationEntry -> {
                        String combination = combinationEntry.getKey();
                        int totalCount = combinationEntry.getValue().values().stream().mapToInt(Integer::intValue).sum();

                        // Get all test IDs across all modules for this combination
                        Set<Long> allTestIds = combinationWiseTestIds.getOrDefault(combination, Collections.emptyMap())
                                .values().stream()
                                .flatMap(Set::stream)
                                .collect(Collectors.toSet());

                        // Convert test IDs to sorted array string
                        String testIdsArray = allTestIds.stream()
                                .sorted()
                                .map(Object::toString)
                                .collect(Collectors.joining(",", "[", "]"));

                        try {
                            // Write CSV line
                            writer.append(String.format("\"%s\",%d,\"%s\"%n",
                                    combination, totalCount, testIdsArray));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            System.out.println("\nCombination-wise summary with test IDs has been saved to: " + csvFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}