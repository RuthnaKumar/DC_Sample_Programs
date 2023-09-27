import com.me.test.test_framework.junit5.custom_xml_report_generator.SAXXMLParser;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;

public class JMeterRawResult {
    public static void main(String args[]) throws XMLStreamException, FileNotFoundException {

        String rawResultFilePath = "E:\\test\\JMeterResult\\MDM_Enrollment_Raw.xml";
        String customizedResultFilePath = "E:\\test\\JMeterResult\\MDM_Enrollment_JMeter.xml";
        String fileName = "bsp_profile_JMeter.xml";
        SAXXMLParser.customizeXMLReport(rawResultFilePath, customizedResultFilePath, fileName);

    }
}
