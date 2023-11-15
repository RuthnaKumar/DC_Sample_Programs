import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class PropertyFileReader {
    public static void main(String[] args) {
       HashMap getPPMHistoryMap = getPPMHistoryProperties();
       if(getPPMHistoryMap.containsKey("112233501")){
           System.out.println("PPM Status for the Build : " +getPPMHistoryMap.getOrDefault(String.valueOf(112233501),"Not Available"));
       }else{
           System.out.println("PPM Status not avaliable");
       }
    }
    public static HashMap getPPMHistoryProperties(){
        String filePath = "E:\\test\\ppm-history.props";
        String targetKey = "Existing_build_Number"; // Change this to your desired key
        String remarksKey = "Remarks"; // Change this to your desired key
        HashMap<String, String> ppmStatusHistoryMap = null;
        File pppmHistoryFile = new File(filePath);
        if(pppmHistoryFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(pppmHistoryFile))) {
                String line;
                ppmStatusHistoryMap = new HashMap<>();
                String ppmBuildNumber = null;
                String ppmStatus = null;

                while ((line = reader.readLine()) != null) {
                    String[] propertyValue = line.split("=");
                    if (propertyValue.length == 2) {
                        String key = propertyValue[0].trim();
                        String value = propertyValue[1].trim();

                        if (key.equals(targetKey)) {
                            ppmBuildNumber = value;
                        }
                        if (key.equals(remarksKey)) {
                            ppmStatus = value;
                        }
                    }
                    if (ppmBuildNumber != null && ppmStatus != null) {
                        ppmStatusHistoryMap.put(ppmBuildNumber, ppmStatus);
                        ppmBuildNumber = null;
                        ppmStatus = null;
                    }
                }
                LOGGER.log(Level.INFO,"PPM Status Map Value : {0}", ppmStatusHistoryMap);
                LOGGER.log(Level.INFO,"PPM Status Map Size: {0}", ppmStatusHistoryMap.size());

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            LOGGER.log(Level.INFO,"PPM Status Map Not Available in  : {0}", pppmHistoryFile);
        }
        return ppmStatusHistoryMap;
    }
}
