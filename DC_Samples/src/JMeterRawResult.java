import com.me.test.test_framework.junit5.custom_xml_report_generator.SAXXMLParser;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;

public class JMeterRawResult {
    public static void main(String args[]) throws XMLStreamException, FileNotFoundException {

        String rawResultFilePath = "E:\\test\\JMeterResult\\Patch_APDRaw.xml";
        String customizedResultFilePath = "E:\\test\\JMeterResult\\Patch_APD.xml";
        String fileName = "Patch_APD.xml";
        SAXXMLParser.customizeXMLReport(rawResultFilePath, customizedResultFilePath, fileName);

    }
}
