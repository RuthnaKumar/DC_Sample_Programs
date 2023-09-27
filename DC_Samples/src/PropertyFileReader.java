import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class PropertyFileReader {
    public static void main(String[] args) {
       HashMap getPPMHistoryMap = getPPMHistoryProperties();
       if(getPPMHistoryMap.containsKey("1012119011")){
           System.out.println("PPM Status for the Build : " +getPPMHistoryMap.getOrDefault("9056",null));
       }else{
           System.out.println("PPM Status not avaliable");
       }
    }
    public static HashMap getPPMHistoryProperties(){
        String filePath = "C:\\Users\\ruthna-12510\\Downloads\\logs\\ppm-history.props";
        String targetKey = "Existing_build_Number"; // Change this to your desired key
        String remarksKey = "Remarks"; // Change this to your desired key
        HashMap<String, String> ppmStatusHistoryMap = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
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
            System.out.println("PPM Status Map Value: " + ppmStatusHistoryMap);
            System.out.println("PPM Status Map Size: " + ppmStatusHistoryMap.size());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return ppmStatusHistoryMap;
    }
}
