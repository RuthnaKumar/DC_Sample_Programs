import java.io.*;
import java.util.*;

public class ConfFileUpdater {

    private static final Map<String, String> updates = new HashMap<>();
    static {
        updates.put("dclogs.dir.deny", "none");
        updates.put("dclogs.dir.allow", "All");
        updates.put("nginx.autoindex.value", "on");
    }

    public static void main(String[] args) {
        String filePath = "C:\\Jenkins_slave\\workspace\\ruthna-12510-JOB\\AdventNetTestSuite\\TestBuild_162654\\ManageEngine\\DesktopCentral_Server\\conf\\websettings.conf";

        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            return;
        }

        Properties properties = new Properties();
        boolean updated = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            properties.load(reader);
        } catch (IOException e) {
            System.err.println("Error reading the configuration file: " + e.getMessage());
            return;
        }
        for (Map.Entry<String, String> entry : updates.entrySet()) {
            String key = entry.getKey();
            String newValue = entry.getValue();
            String currentValue = properties.getProperty(key);

            if (currentValue != null && !currentValue.equals(newValue)) {
                properties.setProperty(key, newValue);
                updated = true;
            }
        }
        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                properties.store(writer, null);
                System.out.println("Configuration file updated successfully.");
            } catch (IOException e) {
                System.err.println("Error writing to the configuration file: " + e.getMessage());
            }
        } else {
            System.out.println("No changes were made to the configuration file.");
        }
    }
}
