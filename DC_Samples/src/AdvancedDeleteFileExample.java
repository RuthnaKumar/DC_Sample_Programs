import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class AdvancedDeleteFileExample {
    public static void main(String[] args) {
        String filePath = "E:\\Jacoco\\JacocoReportMerge\\jacoco.exec";
        File file = new File(filePath);
        deleteFile(file);

    }
    private static void deleteFile(File filePath){
        Path path = Paths.get(filePath.toURI());
        try {
            if (!Files.exists(path)) {
System.out.println("File not available");
            } else {
                Files.delete(path);
                System.out.println("File available");
            }
        }catch (IOException e) {
            System.err.println("Failed to delete the file. Error: " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Access denied. Check file permissions.");
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
