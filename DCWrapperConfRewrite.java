import com.me.test.test_framework.UemsUri;
import com.me.test.test_framework.properties.OnPremisesPath;
import com.me.test.test_framework.properties.TestProperties;

import java.io.*;
import java.util.logging.Level;

public class DCWrapperConfRewrite {
    public static void main(String args[]) {
        modifyWrapperConf();
    }

    public static void modifyWrapperConf() {
        String newLineString = "\n";
        String getJacocoCommand = null;
        String getJacocoPort = null;
        String getJacocoAddress = null;
        String getJacocoIncludes = null;
        String serverPortNumber = "8383";
        String jacocoConfigCommand = getJacocoCommand!=null?getJacocoCommand:"wrapper.java.additional.231=-javaagent:../lib/jacocoagent.jar=destfile=../jacoco/jacoco.exec,classdumpdir=../jacoco/class,output=tcpserver";
        String jacocoPort = getJacocoPort!=null?getJacocoCommand:"1".concat(serverPortNumber);
        String jacocoAddress = getJacocoAddress!=null?getJacocoAddress:"*";
        String jacocoIncludes = getJacocoIncludes!=null?getJacocoIncludes:"com.*";
        String jacocoFullCommand = jacocoConfigCommand.concat(",port=").concat(jacocoPort).concat(",address=").concat(jacocoAddress).concat(",includes=").concat(jacocoIncludes);
        System.out.println("Jacoco Configuration Command : " + jacocoFullCommand);
        File file = new File(String.valueOf("D:\\test\\wrapper.conf"));
        if (!file.exists()) {
            System.out.println(" Wrapper.conf File does not exist.");
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
                    System.out.println("Jacoco Configuration already exist line : {0}" + line);
                    lineExists = true;
                }
//                sb.append(line);
//                sb.append(System.lineSeparator());
            }
            if (!lineExists) {
                sb.append(newLineString + jacocoFullCommand);
                sb.append(System.lineSeparator());
                bw = new BufferedWriter(new FileWriter(file, true));
                bw.write(sb.toString());
                System.out.println("Jacoco Configuration Command appended successfully.");
            } else {
                System.out.println("Jacoco Configuration Command already exists. No changes made in wrapper.conf.");
            }
        } catch (IOException e) {
            System.out.println("Exception while modifying the wrapper.conf : {0}" + e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                System.out.println("Exception while close streams in modify wrapper.conf : {0}" + e);
            }
        }
    }
}