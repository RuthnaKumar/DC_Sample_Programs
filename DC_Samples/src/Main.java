import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if(!"1234456778899".contains("-?[0-9]{1,20}"))
        {
            System.out.println("Hello Zoho");
        }

//        {
//            "regex": "-?[0-9]{1,20}",
//                "replace_string": "1234456778899"
//        }
    }

}
