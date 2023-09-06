import com.me.test.test_framework.UemsUri;
import com.me.test.test_framework.common_testutils.InitialTestConfiguration;
import com.me.test.test_framework.properties.OnPremisesPath;
import com.me.test.test_framework.properties.TestProperties;
import jdk.nashorn.internal.objects.NativeMath;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class modifyDCWrapperConf {
    private final static Logger LOGGER = Logger.getLogger(modifyDCWrapperConf.class.getName());

    public static void main(String args[]) {
        modifyWrapperConf();
    }

    public static void modifyWrapperConf() {
        String newLineString = "\n";
        String jacocoConfigCommand = "wrapper.java.additional.231=-javaagent:../lib/jacocoagent.jar=destfile=../jacoco/jacoco.exec,classdumpdir=../jacoco/class,output=tcpserver";
        String serverPortNumber = "8383";
        String jacocoPort = "1".concat(serverPortNumber);
        String jacocoAddress =  "*";
        String jacocoIncludes = "com.*";
        String jacocoFullCommand = jacocoConfigCommand.concat(",port=").concat(jacocoPort).concat(",address=").concat(jacocoAddress).concat(",includes=").concat(jacocoIncludes);
        LOGGER.log(Level.INFO, "Jacoco Configuration Command : {0}", jacocoFullCommand);
        File file = new File("E:\\test\\Test\\wrapper.conf");
        if (!file.exists()) {
            LOGGER.log(Level.INFO, " Wrapper.conf File does not exist.");
        }
        boolean lineExists = false;
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("wrapper.java.additional.231=-javaagent:")) {
                    LOGGER.log(Level.INFO, "Jacoco Configuration already exist line : {0}", line);
                    lineExists = true;
                }
            }
            if (!lineExists) {
                sb.append(newLineString.concat(jacocoFullCommand));
                sb.append(System.lineSeparator());
                bw = new BufferedWriter(new FileWriter(file, true));
                bw.write(sb.toString());
                LOGGER.log(Level.INFO, "Jacoco Configuration Command appended successfully");
            } else {
                LOGGER.log(Level.INFO, "Jacoco Configuration Command already exists. No changes made in wrapper.conf.");
            }
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Exception while modifying the wrapper.conf : {0}", e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                LOGGER.log(Level.INFO, "Exception while close streams in modify wrapper.conf : {0}", e);
            }
        }
    }
}
