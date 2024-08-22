import com.me.test.test_framework.junit5.custom_xml_report_generator.SAXXMLParser;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;

public class JMeterRawResulttoFormatedResult {
    public static void main(String args[]) throws XMLStreamException, FileNotFoundException {

        SAXXMLParser.customizeXMLReport("E:\\OSD\\raw\\OSD_Zero_Touch_Deployment_152041_JMeter.xml","E:\\OSD\\formated\\OSD_Zero_Touch_Deployment_152041_JMeter.xml","OSD_Zero_Touch_Deployment_152041", "");

    }
}
