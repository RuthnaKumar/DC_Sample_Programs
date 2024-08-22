import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateXLSFile {
    public static void main(String[] args) throws IOException {

        Map<String, String[]> data = readJMXFileData();
        writeXLSFile(data);
        // Create some data rows
    }

    private static void writeXLSFile(Map<String, String[]> data) {
        try (Workbook workbook = new HSSFWorkbook()) {
            // Create a new Sheet
            Sheet sheet = workbook.createSheet("Sheet1");

            // Create a Row
            Row headerRow = sheet.createRow(0);

            // Create header cells
            String[] headers = {"TestCaseDescription", "Department", "Modules", "Functionality", "TestSteps", "TestCaseID"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            int rowNum = 1;
            for (Map.Entry<String, String[]> entry : data.entrySet()) {
                ArrayList<String> values = new ArrayList<>();
                Row row = sheet.createRow(rowNum++);
                values.add(entry.getKey());
                for (String st : entry.getValue()){
                    values.add(st);
                }
                int colNum = 0;
                for (String field : values) {
                    Cell cell = row.createCell(colNum++);
                    cell.setCellValue(field);
                }
            }

            // Write the workbook content to a file
            try (FileOutputStream fileOut = new FileOutputStream("C:\\Users\\ruthna-12510\\Downloads\\example.xls")) {
                workbook.write(fileOut);
            }

            System.out.println("Excel file has been created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Map<String, String[]> readJMXFileData() {
        Map<String, String[]> testNameComments = new HashMap<>();

        String currentWorkingDirectory = System.getProperty("user.dir");
        String filePath = "C:\\Users\\ruthna-12510\\Downloads\\Configuration_API_Standard.jmx";

        String testNamePattern = "testname=\"(.*?)\"";
        String commentsPattern = "<stringProp[^>]*>(.*?)</stringProp>";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder content = new StringBuilder();
            String line;
            boolean flag = false;
            String testName = "";
            while ((line = reader.readLine()) != null) {
                if(line.contains("<HTTPSamplerProxy")) {
                    flag = true;
                    testName = getDataFromLine(line,testNamePattern);
                    content.append(line).append("\n");
                }
                if (line.contains("</HTTPSamplerProxy>")) {
                    flag = false;
                }
                if (line.contains("TestPlan.comments") && flag) {
                    String comment = getDataFromLine(line,commentsPattern);
                    String[] comments = comment.split("-&gt;");
                    testNameComments.put(testName,comments);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return testNameComments;
    }

    private static String getDataFromLine(String line, String comparePattern) {
        String testNameValue = "";
        Pattern pattern = Pattern.compile(comparePattern);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            testNameValue = matcher.group(1);
        }
        return testNameValue;
    }
}
