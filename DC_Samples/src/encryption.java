import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class encryption {
    public static void main(String args[]){
        String password = "Testing@123#";
        String data = "{\n" +
                "    \"currentTime\": 1738877725162,\n" +
                "    \"publicKey\": \"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuaMcULHTUB+0K6T2SYdDR00Dqtp2OwIRkaGxH\\/1lt1XECwABvZ8h1UjsT6ayCzDe62ixbX+2ra3psdMsiat28CDJaALlfOkead1f9Z\\/GBadHqm76S5yNbRXV5api1+uVySueANawX5Gu3wfbL90SC63sX6HU0TaTkTeDE5hV4uZJgHlLFTtbMVOgLijjnMJ6yl3zpwdIOYNmwwHfkQbaYMmB4qHtgsMG5MqEgTd2hsShPRtgGBU1WFqjKM7OUgALeU2sN99NRimzrHeVV4s3CpnC6dfRKU9Zh7m4wgSx48A2fLT4pK7gmQfYX2fZoA564sO36r6Un+ypXb9+3PQ+mwIDAQAB\",\n" +
                "    \"isPayloadEncryptionEnabled\": true\n" +
                "}";
        JSONObject publickeyData = new JSONObject(data);
        System.out.println(encrypt(password,publickeyData));
    }
    public static List<String> encrypt(String data, JSONObject publicKeyData) {
        try {
            if (publicKeyData.getBoolean("isPayloadEncryptionEnabled")) {
                KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                keyGen.init(256);
                SecretKey aesKey = keyGen.generateKey();
                byte[] key = aesKey.getEncoded();
                byte[] iv = new byte[12];
                SecureRandom random = new SecureRandom();
                random.nextBytes(iv);
                Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
                GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
                cipher.init(1, aesKey, gcmSpec);
                byte[] encryptedDataBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
                String encryptedData = Base64.getEncoder().encodeToString(encryptedDataBytes);
                String publickey = publicKeyData.getString("publicKey");
                byte[] encodedPublicKeyBytes = Base64.getDecoder().decode(publickey);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedPublicKeyBytes));
                byte[] aeskeyiv = new byte[9 + key.length + iv.length];
                ByteBuffer buffer = ByteBuffer.wrap(aeskeyiv);
                buffer.putLong(System.currentTimeMillis());
                buffer.put((byte)45);
                buffer.put(key);
                buffer.put(iv);
                aeskeyiv = buffer.array();
                Cipher cipher2 = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
                OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);
                cipher2.init(1, publicKey, oaepParams);
                byte[] encryptedKeyAndIv = cipher2.doFinal(aeskeyiv);
                String encryptionKey = Base64.getEncoder().encodeToString(encryptedKeyAndIv);
                List<String> result = new ArrayList();
                result.add(encryptedData);
                result.add(encryptionKey);
                return result;
            }
        } catch (Exception var22) {
            System.out.println("Exception while encrypting");
        }

        return null;
    }
}

