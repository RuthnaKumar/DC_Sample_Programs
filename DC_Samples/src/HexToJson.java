import java.nio.charset.StandardCharsets;

public class HexToJson {
    public static void main(String[] args) {
        // The provided hex string
        String hexData = "c30c040901024fe41deceefec76dd28f015af6a7e9ffbad88836caae9cccb42f5b7f64e63c896c5f3b4a5b089e20b0ec296490254156a810c1383037147b64ff0e7141de3a01cf4c2994001f975c8c91fdadda8e2d1d4b8b028be15abe56823412caa45a9c16f4940907f2a5053a298cc38319fb82a265848c9425432190c3041c31f180ea249402dccc61956dc3c18f1132ee3a315e3e00b660897b7bf1da";

        // Convert hex string to byte array
        byte[] byteData = hexStringToByteArray(hexData);

        // Attempt to decode bytes to string using UTF-8 encoding
        try {
            String jsonString = new String(byteData, StandardCharsets.UTF_8);
            System.out.println("Decoded JSON String: " + jsonString);
        } catch (Exception e) {
            System.out.println("Failed to decode with UTF-8");
            e.printStackTrace();
        }
    }

    // Convert hex string to byte array
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
