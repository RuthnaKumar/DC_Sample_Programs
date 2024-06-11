import com.me.test.test_framework.properties.OnPremisesPath;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {
        String temp = "-----BEGIN CERTIFICATE-----\n" +
                "MIIFdjCCBF6gAwIBAgIIaA57ZjAFpCMwDQYJKoZIhvcNAQELBQAwgYwxQDA+BgNV\n" +
                "BAMMN0FwcGxlIEFwcGxpY2F0aW9uIEludGVncmF0aW9uIDIgQ2VydGlmaWNhdGlv\n" +
                "biBBdXRob3JpdHkxJjAkBgNVBAsMHUFwcGxlIENlcnRpZmljYXRpb24gQXV0aG9y\n" +
                "aXR5MRMwEQYDVQQKDApBcHBsZSBJbmMuMQswCQYDVQQGEwJVUzAeFw0yNDA1MjYx\n" +
                "NDM1NDdaFw0yNTA1MjYxNDM1NDZaMIGPMUwwSgYKCZImiZPyLGQBAQw8Y29tLmFw\n" +
                "cGxlLm1nbXQuRXh0ZXJuYWwuYjQzODAxNzgtNzFiMC00NmM0LWJjZjEtNzczNDUy\n" +
                "OGE3Yjk0MTIwMAYDVQQDDClBUFNQOmI0MzgwMTc4LTcxYjAtNDZjNC1iY2YxLTc3\n" +
                "MzQ1MjhhN2I5NDELMAkGA1UEBhMCVVMwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAw\n" +
                "ggEKAoIBAQCjIbWDMogIcxtj/Q5A814KIL3lVRRQXi0NHsYCr6PnpJc8PYUtiiJK\n" +
                "vsjLcOYfzoFtLg87AO1cKWRGml7ramxcR8/jC40vFGEV7q6vFdqp284hcBieI/MY\n" +
                "UqAjfTnKVioBnCYyeFglK8jihSVhKJyjymgKu1wr3aIYGVjsDSbJ+SB3+4KAYj74\n" +
                "DgJIhlssCI7MUpR1EQE0NkDUdsOYWUXvp1tuFJXBPkMCAhiub2KN2UZSU7HK38ZQ\n" +
                "rdLmhtKyQO7zrYAj/XyoWJ0cH47ALGrev42A9AiuhZFeBmPgxwBgOznG1B5IgJ3r\n" +
                "F5OqjFuJ1B/gdrDnmYPVNRhZIeplpJ5JAgMBAAGjggHVMIIB0TAJBgNVHRMEAjAA\n" +
                "MB8GA1UdIwQYMBaAFPe+fCFgkds9G3vYOjKBad+ebH+bMIIBHAYDVR0gBIIBEzCC\n" +
                "AQ8wggELBgkqhkiG92NkBQEwgf0wgcMGCCsGAQUFBwICMIG2DIGzUmVsaWFuY2Ug\n" +
                "b24gdGhpcyBjZXJ0aWZpY2F0ZSBieSBhbnkgcGFydHkgYXNzdW1lcyBhY2NlcHRh\n" +
                "bmNlIG9mIHRoZSB0aGVuIGFwcGxpY2FibGUgc3RhbmRhcmQgdGVybXMgYW5kIGNv\n" +
                "bmRpdGlvbnMgb2YgdXNlLCBjZXJ0aWZpY2F0ZSBwb2xpY3kgYW5kIGNlcnRpZmlj\n" +
                "YXRpb24gcHJhY3RpY2Ugc3RhdGVtZW50cy4wNQYIKwYBBQUHAgEWKWh0dHA6Ly93\n" +
                "d3cuYXBwbGUuY29tL2NlcnRpZmljYXRlYXV0aG9yaXR5MBMGA1UdJQQMMAoGCCsG\n" +
                "AQUFBwMCMDAGA1UdHwQpMCcwJaAjoCGGH2h0dHA6Ly9jcmwuYXBwbGUuY29tL2Fh\n" +
                "aTJjYS5jcmwwHQYDVR0OBBYEFKgo+TFW7LVy4S2TF24R6qq8gDfhMAsGA1UdDwQE\n" +
                "AwIHgDAQBgoqhkiG92NkBgMCBAIFADANBgkqhkiG9w0BAQsFAAOCAQEAmnltATGy\n" +
                "64SkikKJZ41K1R5LppG25XjTUVn6i6bJmdA2y4nu0lrGpujm75VyFyFiPOGGTseF\n" +
                "bfRbXeYvCyKV6OkrN0RcMAbEyTZjBxkxD5uLuAEygVDngAzUu+WA88uKZ0rkBTzu\n" +
                "O5tUqamrAXXB++ANhQZOOE//PGlyHZS/+GS+5ly0i5STwYAkGTijH3Lz5630g7P2\n" +
                "c+4Z30UarsYZLhbAtbvqwnGMQP22tGK1Xc6OOA66dHL7GUrnIqBZGu6bqnZtV7lk\n" +
                "aEqRowfjeaEcuLpE4xXFUFnhrCcl09hipBgWj/q0++4wTygmivc1ySemC22gMg7e\n" +
                "KXGMUVfWh1NTDw==\n" +
                "-----END CERTIFICATE-----";
        System.out.println(temp);
    }

 }
