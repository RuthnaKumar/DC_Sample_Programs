

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class uninstallPreviousBuild {

    //<--------------------------------PRODUCT_NAMES---------------------------->
    private static final String DC_PRODUCT_CODE = "DCEE";
    private static final String PMP_PRODUCT_CODE = "PMP";
    private static final String VMP_PRODUCT_CODE = "VMP";
    private static final String RMP_PRODUCT_CODE = "RMP";
    private static final String UES_PRODUCT_CODE = "UES";
    private static final String DCP_PRODUCT_CODE ="DCP";
    private static final String ACP_PRODUCT_CODE = "ACP";
    private static final String MSP_PRODUCT_CODE = "DCMSP";
    private static final String MDM_PRODUCT_CODE = "MDM";

    private static final String OSD_PRODUCT_CODE = "OSD";
    private static final String PRODUCT_CODE_NOT_FOUND = "NOT_FOUND";
    //<--------------------------------SERVICE_NAMES---------------------------->
    private static final String DESKTOP_CENTRAL_SERVER="DesktopCentralServer";
    private static final String VULNERABILITY_MANAGER_PLUS="VulnerabilityManagerPlus";
    private static final String PATCH_MANAGER_PLUS ="PatchManagerPlus";
    private static final String UEMS_SERVICE = "uems_service";
    private static final String MANAGE_ENGINE_DESKTOP_CENTRAL_MSP ="DesktopCentralServerMSP";
    private static final String MOBILE_DEVICE_MANAGER = "MDMServer";
    private static final String SUMMARY_SERVER = "summary_server_service";

    //<-------------------------SERVICE_LIST--------------------------------->
    private static HashMap<String,String[]> serviceList = new HashMap<>();

    //<------------------------------------------------------------------------->

    private static uninstallPreviousBuild instance = null;
    private static Logger LOGGER = Logger.getLogger(uninstallPreviousBuild.class.getName());
    public static void main(String args[]){
        uninstallation("C:\\Jenkins_slave\\workspace\\ruthna-12510-PPMJOB\\PPMTest\\11_Sep_23_21_15_21\\download\\ManageEngine\\DesktopCentral_Server");
    }


    public static void uninstallation(String serverHome) {
        //FileManipulation.initLog(LOGGER,System.getProperty("user.dir") + File.separator + "uninstall.log");
        try {
            LOGGER.log(Level.INFO, "Inside Uninstall the existing server");
            serviceList.put(DC_PRODUCT_CODE, new String[]{DESKTOP_CENTRAL_SERVER, VULNERABILITY_MANAGER_PLUS, PATCH_MANAGER_PLUS, UEMS_SERVICE, SUMMARY_SERVER});
            serviceList.put(UES_PRODUCT_CODE, new String[]{UEMS_SERVICE, DESKTOP_CENTRAL_SERVER, SUMMARY_SERVER});
            serviceList.put(PMP_PRODUCT_CODE, new String[]{UEMS_SERVICE, DESKTOP_CENTRAL_SERVER, PATCH_MANAGER_PLUS, SUMMARY_SERVER});
            serviceList.put(VMP_PRODUCT_CODE, new String[]{UEMS_SERVICE, DESKTOP_CENTRAL_SERVER, VULNERABILITY_MANAGER_PLUS, SUMMARY_SERVER});
            serviceList.put(RMP_PRODUCT_CODE, new String[]{UEMS_SERVICE, DESKTOP_CENTRAL_SERVER, SUMMARY_SERVER});
            serviceList.put(DCP_PRODUCT_CODE, new String[]{UEMS_SERVICE, DESKTOP_CENTRAL_SERVER, SUMMARY_SERVER});
            serviceList.put(ACP_PRODUCT_CODE, new String[]{UEMS_SERVICE, DESKTOP_CENTRAL_SERVER, SUMMARY_SERVER});
            serviceList.put(MSP_PRODUCT_CODE, new String[]{MANAGE_ENGINE_DESKTOP_CENTRAL_MSP});
            serviceList.put(MDM_PRODUCT_CODE, new String[]{MOBILE_DEVICE_MANAGER});
            serviceList.put(OSD_PRODUCT_CODE,new String[]{DESKTOP_CENTRAL_SERVER,VULNERABILITY_MANAGER_PLUS,PATCH_MANAGER_PLUS,UEMS_SERVICE,MANAGE_ENGINE_DESKTOP_CENTRAL_MSP});
            serviceList.put(PRODUCT_CODE_NOT_FOUND, new String[]{DESKTOP_CENTRAL_SERVER, VULNERABILITY_MANAGER_PLUS, PATCH_MANAGER_PLUS, UEMS_SERVICE, MANAGE_ENGINE_DESKTOP_CENTRAL_MSP}); //SPECIAL CASE TO AVOID ERRORS
            LOGGER.info("Pre Uninstaller Method Called ...!!!");
            String[] services = serviceList.get(findProductName(serverHome));
            for (String service : services) {
                int servicestatus = checkService(service);
                if (servicestatus != 0) {
                    LOGGER.info(service + " exist");
                    String installationBinDIR = getServiceDir(service);
                    LOGGER.info("Installation DIR: " + installationBinDIR);
//                    uninstallProd(installationBinDIR);
                } else {
                    LOGGER.info(service + " Service is not installed");
                }
            }
        }catch (Exception e){
            LOGGER.log(Level.INFO,"Exception while uninstall the existing server : {0}",e);
        }
    }

    public static int checkService(String serviceName) {
        try {
            String scOutput = runCommand(System.getenv("WINDIR")+"\\System32\\sc.exe" + " query " + serviceName);

            if (scOutput.contains("STATE")) {
                if (scOutput.contains("RUNNING")) {
                    return 2;           // Installed and in running status
                } else {
                    return 1;          // Installed but not in running status
                }
            } else {
                return 0;             // Unknown Service
            }
        } catch (IOException e) {
            return 0;
        }
    }

    public static String getServiceDir(String serviceName) {
        try {
            String scOutput = runCommand("wmic service \""+serviceName+"\" get PathName");
            scOutput = scOutput.replace("PathName", "");
            scOutput = scOutput.replace("\"","");
            scOutput = scOutput.trim();
            int binDirIndex = scOutput.indexOf("bin")+3;
            return scOutput.substring(0,binDirIndex);
        } catch(Exception e) {
            LOGGER.info("Exception: "+e);
            return "";
        }
    }

    public static void uninstallProd(String binDir) {
        try {
            String uninstallbat = "\"" + binDir + File.separator + "scripts" + File.separator + "uninstall.bat\"";
            LOGGER.info("uninstall str: " + uninstallbat);
            runCommand(uninstallbat);
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
    }

    public static String runCommand(String command) throws IOException {
        Process process = Runtime.getRuntime().exec(command);
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        String scOutput = "";

        while ((line = br.readLine()) != null) {
            scOutput +=  line + "\n" ;
        }
        LOGGER.info("output: "+scOutput);
        return scOutput;
    }

    public  static  String findProductName(String serverHomeDir) throws IOException {
        File confFile;
        try {
            if(serverHomeDir != null) {

                confFile = new File(serverHomeDir + File.separator + "conf" + File.separator + "product.conf");
                Properties prodProp = new Properties();
                prodProp.load(new FileReader(confFile));
                if(serverHomeDir.contains("MEMDMOnPremise_Development") ||prodProp.getProperty("productname").contains("Mobile Device Manager")) {return "MDM";}
                return prodProp.getProperty("activeproductcodes");
            }
        }
        catch (Exception e)
        {
            LOGGER.log(Level.SEVERE,"Exception: ",e);
        }
        return "NOT_FOUND";
    }

}
