import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;
import java.security.*;
import java.util.stream.*;
import java.util.Base64;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class SimilarJarFinder {
    private static final double NAME_SIMILARITY_THRESHOLD = 0.7;
    private static final double CLASS_SIMILARITY_THRESHOLD = 0.8;

    public static void main(String[] args) throws IOException {
//        if (args.length == 0) {
//            System.out.println("Usage: java SimilarJarFinder <directory>");
//            return;
//        }

        Path dir = Paths.get("/Users/ruthna-12510-t/Documents/2520/AdventNet/Sas/tomcat/webapps/ROOT/WEB-INF/lib/");
        if (!Files.isDirectory(dir)) {
            System.out.println("Invalid directory: " + dir);
            return;
        }

        List<Path> jarFiles = Files.walk(dir)
                .filter(p -> p.toString().toLowerCase().endsWith(".jar"))
                .collect(Collectors.toList());

        if (jarFiles.isEmpty()) {
            System.out.println("No JAR files found in " + dir);
            return;
        }

        System.out.println("Found " + jarFiles.size() + " JAR files. Analyzing...");
        findSimilarJars(jarFiles);
    }

    private static void findSimilarJars(List<Path> jarFiles) throws IOException {
        // Compare JAR names with version verification
        System.out.println("\n=== Similar JAR Names with Version Verification ===");
        List<NameSimilarityResult> nameSimilarities = new ArrayList<>();
        for (int i = 0; i < jarFiles.size(); i++) {
            for (int j = i + 1; j < jarFiles.size(); j++) {
                Path jar1 = jarFiles.get(i);
                Path jar2 = jarFiles.get(j);

                String name1 = jar1.getFileName().toString();
                String name2 = jar2.getFileName().toString();

                double similarity = stringSimilarity(name1, name2);
                if (similarity >= NAME_SIMILARITY_THRESHOLD) {
                    String version1 = getJarVersion(jar1);
                    String version2 = getJarVersion(jar2);
                    boolean compatibleVersions = areVersionsCompatible(version1, version2);

                    nameSimilarities.add(new NameSimilarityResult(
                            name1, name2, similarity,
                            version1, version2, compatibleVersions));
                }
            }
        }

        nameSimilarities.sort(Comparator.comparingDouble(NameSimilarityResult::getSimilarity).reversed());
        for (NameSimilarityResult result : nameSimilarities) {
            System.out.printf("\nSimilar names (%.2f%%): %s <-> %s%n",
                    result.getSimilarity() * 100,
                    result.getJar1(),
                    result.getJar2());
            System.out.println("  Version 1: " + result.getVersion1());
            System.out.println("  Version 2: " + result.getVersion2());
            System.out.println("  Compatible: " + (result.isCompatible() ? "Yes" : "No"));
        }

        // Compare JAR contents at class level only
        System.out.println("\n=== Analyzing JAR Class Contents ===");
        Map<Path, Set<String>> jarClassContents = new HashMap<>();
        for (Path jar : jarFiles) {
            System.out.println("Processing: " + jar.getFileName());
            jarClassContents.put(jar, getJarClassContents(jar));
        }

        System.out.println("\n=== Similar JAR Contents (Class Level Only) ===");
        List<ClassSimilarityResult> classSimilarities = new ArrayList<>();
        for (int i = 0; i < jarFiles.size(); i++) {
            for (int j = i + 1; j < jarFiles.size(); j++) {
                Path jar1 = jarFiles.get(i);
                Path jar2 = jarFiles.get(j);

                Set<String> classContent1 = jarClassContents.get(jar1);
                Set<String> classContent2 = jarClassContents.get(jar2);

                double similarity = contentSimilarity(classContent1, classContent2);
                if (similarity >= CLASS_SIMILARITY_THRESHOLD) {
                    Set<String> commonClasses = getCommonClasses(classContent1, classContent2);
                    classSimilarities.add(new ClassSimilarityResult(
                            jar1.getFileName().toString(),
                            jar2.getFileName().toString(),
                            similarity,
                            commonClasses));
                }
            }
        }

        classSimilarities.sort(Comparator.comparingDouble(ClassSimilarityResult::getSimilarity).reversed());
        for (ClassSimilarityResult result : classSimilarities) {
            System.out.printf("\nSimilar class contents (%.2f%%): %s <-> %s%n",
                    result.getSimilarity() * 100,
                    result.getJar1(),
                    result.getJar2());

            System.out.println("Common classes (" + result.getCommonClasses().size() + "):");
            result.getCommonClasses().stream()
                    .limit(20)
                    .forEach(cls -> System.out.println("  " + cls));

            if (result.getCommonClasses().size() > 20) {
                System.out.println("  ... and " + (result.getCommonClasses().size() - 20) + " more");
            }
        }
    }

    private static String getJarVersion(Path jarFile) throws IOException {
        try (JarFile jar = new JarFile(jarFile.toFile())) {
            Manifest manifest = jar.getManifest();
            if (manifest != null) {
                Attributes attrs = manifest.getMainAttributes();
                String version = attrs.getValue("Implementation-Version");
                if (version != null) return version;
                version = attrs.getValue("Bundle-Version");
                if (version != null) return version;
            }

            // Fallback to filename parsing
            String filename = jarFile.getFileName().toString();
            if (filename.matches(".*\\d+\\.\\d+.*")) {
                return filename.replaceAll(".*?(\\d+\\.\\d+).*", "$1");
            }
        }
        return "UNKNOWN";
    }

    private static boolean areVersionsCompatible(String version1, String version2) {
        if (version1.equals("UNKNOWN") || version2.equals("UNKNOWN")) return false;

        try {
            // Simple version compatibility check (major.minor)
            String[] v1Parts = version1.split("\\.");
            String[] v2Parts = version2.split("\\.");

            if (v1Parts.length >= 2 && v2Parts.length >= 2) {
                return v1Parts[0].equals(v2Parts[0]) &&
                        Math.abs(Integer.parseInt(v1Parts[1]) - Integer.parseInt(v2Parts[1])) <= 1;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    private static Set<String> getCommonClasses(Set<String> set1, Set<String> set2) {
        Set<String> common = new HashSet<>(set1);
        common.retainAll(set2);
        return common;
    }

    private static Set<String> getJarClassContents(Path jarFile) throws IOException {
        Set<String> contents = new HashSet<>();
        try (JarFile jar = new JarFile(jarFile.toFile())) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    String className = entry.getName()
                            .replace("/", ".")
                            .replace(".class", "");
                    contents.add(className);
                }
            }
        }
        return contents;
    }

    private static double stringSimilarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) return 1.0;

        int distance = levenshteinDistance(longer, shorter);
        return (longerLength - distance) / (double) longerLength;
    }

    private static int levenshteinDistance(String s, String t) {
        int[][] dp = new int[s.length() + 1][t.length() + 1];

        for (int i = 0; i <= s.length(); i++) {
            for (int j = 0; j <= t.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(
                            dp[i - 1][j - 1] + costOfSubstitution(s.charAt(i - 1), t.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }
        return dp[s.length()][t.length()];
    }
    private static int min(int... numbers) {
        return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }
    private static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }
    private static class NameSimilarityResult {
        private final String jar1;
        private final String jar2;
        private final double similarity;
        private final String version1;
        private final String version2;
        private final boolean compatible;

        public NameSimilarityResult(String jar1, String jar2, double similarity,
                                    String version1, String version2, boolean compatible) {
            this.jar1 = jar1;
            this.jar2 = jar2;
            this.similarity = similarity;
            this.version1 = version1;
            this.version2 = version2;
            this.compatible = compatible;
        }

        public double getSimilarity() { return similarity; }
        public String getJar1() { return jar1; }
        public String getJar2() { return jar2; }
        public String getVersion1() { return version1; }
        public String getVersion2() { return version2; }
        public boolean isCompatible() { return compatible; }
    }

    private static class ClassSimilarityResult {
        private final String jar1;
        private final String jar2;
        private final double similarity;
        private final Set<String> commonClasses;

        public ClassSimilarityResult(String jar1, String jar2, double similarity,
                                     Set<String> commonClasses) {
            this.jar1 = jar1;
            this.jar2 = jar2;
            this.similarity = similarity;
            this.commonClasses = commonClasses;
        }

        public double getSimilarity() { return similarity; }
        public String getJar1() { return jar1; }
        public String getJar2() { return jar2; }
        public Set<String> getCommonClasses() { return commonClasses; }
    }
    private static double contentSimilarity(Set<String> set1, Set<String> set2) {
        if (set1.isEmpty() && set2.isEmpty()) return 1.0;

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }
}