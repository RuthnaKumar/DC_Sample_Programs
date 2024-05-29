import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DuplicateFileFinder {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java DuplicateFileFinder <directory>");
            return; // Exit the program
        }

        // Get the directory path from the command line arguments
        String directoryPath = args[0];
        findDuplicateFiles(directoryPath);
    }

    public static void findDuplicateFiles(String directoryPath) {
        File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles();
        Map<String, Set<String>> fileNamesMap = new HashMap<>();
        Map<String, File> latestModifiedFiles = new HashMap<>();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".class")) {
                    String fileName = file.getName();
                    String fileKey = fileName.split("\\.")[0]; // Extracting file name without extension

                    // Check if the file name already exists in the map
                    if (fileNamesMap.containsKey(fileKey)) {
                        fileNamesMap.get(fileKey).add(file.getAbsolutePath());
                        if (file.lastModified() > latestModifiedFiles.get(fileKey).lastModified()) {
                            latestModifiedFiles.put(fileKey, file);
                        }
                    } else {
                        Set<String> fileSet = new HashSet<>();
                        fileSet.add(file.getAbsolutePath());
                        fileNamesMap.put(fileKey, fileSet);
                        latestModifiedFiles.put(fileKey, file);
                    }
                } else if (file.isDirectory()) {
                    findDuplicateFiles(file.getAbsolutePath()); // Recursively search in nested directories
                }
            }

            // Delete duplicate files excluding the latest modified file for each file name
            for (Map.Entry<String, Set<String>> entry : fileNamesMap.entrySet()) {
                if (entry.getValue().size() > 1) {
                    File latestModifiedFile = latestModifiedFiles.get(entry.getKey());
                    for (String filePath : entry.getValue()) {
                        File file = new File(filePath);
                        if (!file.equals(latestModifiedFile)) {
                            try {
                                Files.delete(file.toPath()); // Delete the duplicate file
                                System.out.println("Deleted duplicate file: " + filePath);
                            } catch (IOException e) {
                                System.out.println("Error deleting file: " + filePath);
                            }
                        } else {
                            System.out.println("Excluded for duplicate : " + filePath);
                        }
                    }
                    System.out.println();
                }
            }
        } else {
            System.out.println("Directory is empty or does not exist.");
        }
    }
}
