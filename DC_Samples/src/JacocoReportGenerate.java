import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JacocoReportGenerate {
    private static final Logger logger = Logger.getLogger(JacocoReportGenerate.class.getName());

    public static void main(String[] args) {
        executeCMDCommand();
    }

    public static void executeCMDCommand() {
        try {
            String batchFilePath = "C:\\Jenkins_slave\\workspace\\ruthna-12510-JOB\\AdventNetTestSuite\\TestBuild_135164\\ManageEngine\\DesktopCentral_Server\\bin\\GenerateJacocoReport.bat";
            String param1 = "8383";
            String param2 = "ALL";
            String param3 = "ALL";
            String param4 = "C:\\GitLab";

            String command = String.format("cmd.exe /c \"%s\" %s %s %s %s",
                    batchFilePath, param1, param2, param3, param4);

            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            logProcessOutput(process);
            int exitCode = process.waitFor();
            System.out.println("Batch file execution completed with exit code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, "Error executing CMD command", e);
        }
    }

    private static void logProcessOutput(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        logger.info("CMD Output:");
        while ((line = reader.readLine()) != null) {
            logger.info(line);
        }
        reader.close();
    }
}
