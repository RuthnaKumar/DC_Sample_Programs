import com.me.test.test_framework.junit5.custom_xml_report_generator.SAXXMLParser;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;

public class JMeterRawResult {
    public static void main(String args[]) throws XMLStreamException, FileNotFoundException {

        String rawResultFilePath = "E:\\JMeter\\JMeterBackup_Main\\JMeter_API_Prelim_POC\\userRoleBasedTestingReport_Complete.xml";
        String customizedResultFilePath = "E:\\test\\JMeterResult\\userRoleBasedTestingReportAllConfig_JMeter.xml";
        String fileName = "JMeterRoleBasedTesting.xml";
        SAXXMLParser.customizeXMLReport(rawResultFilePath, customizedResultFilePath, fileName);

    }
}
