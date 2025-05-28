package com.auto.ai;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class RepoExtractor {

    // Multiple valid URL prefixes
    private static final List<Pattern> URL_PATTERNS = Arrays.asList(
            Pattern.compile("^https://build\\.zohocorp\\.com/zoho/([\\w-]+)"),
            Pattern.compile("^https://zrepository\\.zoho\\.com/zohocorp/([\\w-]+)"),
            Pattern.compile("^https://build\\.zohocorp\\.com/me/([\\w-]+)")
    );

    public static Set<String> extractReposFromFile(String filePath) {
        Set repoList = new HashSet();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Skip comments and blank lines
                if (line.isEmpty() || line.startsWith("#")) continue;

                int eqIndex = line.indexOf('=');
                if (eqIndex == -1) continue;

                String value = line.substring(eqIndex + 1).trim();

                for (Pattern pattern : URL_PATTERNS) {
                    Matcher matcher = pattern.matcher(value);
                    if (matcher.find()) {
                        String repoName = matcher.group(1);
                        repoList.add(repoName);
                        break; // Stop checking other patterns once matched
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return repoList;
    }

    public static void main(String[] args) {
//        if (args.length != 1) {
//            System.out.println("Usage: java RepoExtractor <path-to-ant.properties>");
//            return;
//        }

        String filePath = "/Users/ruthna-12510-t/Downloads/ant_dc_cloud.properties";
        Set<String> repos = extractReposFromFile(filePath);

        System.out.println("Repositories found : " + repos.size());
        for (String repo : repos) {
            System.out.println("- " + repo);
        }
    }
}
