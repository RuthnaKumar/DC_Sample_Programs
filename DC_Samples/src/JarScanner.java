import io.github.classgraph.*;

import java.io.File;
import java.util.*;

public class JarScanner {
    public static void main(String[] args) {
        // Path to your JARs folder
        String jarDirectoryPath = "/Users/ruthna-12510-t/Documents/2520/";

        File jarDir = new File(jarDirectoryPath);
        if (!jarDir.exists() || !jarDir.isDirectory()) {
            System.err.println("Invalid directory: " + jarDirectoryPath);
            return;
        }

        // Collect all .jar files in the directory
        List<String> jarPaths = new ArrayList<>();
        for (File jarFile : Objects.requireNonNull(jarDir.listFiles((dir, name) -> name.endsWith(".jar")))) {
            jarPaths.add(jarFile.getAbsolutePath());
        }

        // Map to store class name -> list of JARs
        Map<String, List<String>> classToJarsMap = new HashMap<>();

        try (ScanResult result = new ClassGraph()
                .overrideClasspath(jarPaths)
                .enableClassInfo()
                .scan()) {

            for (ClassInfo classInfo : result.getAllClasses()) {
                String className = classInfo.getName();
                String jarFileName = classInfo.getClasspathElementFile().getName();

                classToJarsMap
                        .computeIfAbsent(className, k -> new ArrayList<>())
                        .add(jarFileName);
            }
        }

        // Print classes that appear in more than one JAR
        System.out.println("=== Duplicate Classes Found in Multiple JARs ===");

        for (Map.Entry<String, List<String>> entry : classToJarsMap.entrySet()) {
            List<String> jars = entry.getValue();
            System.out.println(jars);
            if (jars.size() > 1) {
                System.out.println("Class: " + entry.getKey());
                for (String jar : jars) {
                    System.out.println("  - " + jar);
                }
                System.out.println();
            }
        }
    }
}
