


import com.adventnet.persistence.PersistenceException;
import com.adventnet.persistence.PersistenceInitializer;
import com.adventnet.persistence.PersistenceUtil;
import com.zoho.mickey.exception.PasswordException;
import org.json.JSONObject;

import javax.ws.rs.HttpMethod;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) throws PersistenceException, PasswordException {
        String encryPassword = PersistenceUtil.getDBPasswordProvider().getPassword("test");
        System.out.println(encryPassword);
    }



 }
