import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DC_ServerPasswordEncodeing {
    public static void main(String args[]){
        byte[] encodedutf8Bytes = "Em$automation".getBytes(StandardCharsets.UTF_8);
        String encodedutf16String = new String(encodedutf8Bytes, StandardCharsets.UTF_8);
        byte[] encodedutf16Bytes = encodedutf16String.getBytes(StandardCharsets.UTF_16LE);
        String encodedString = Base64.getEncoder().encodeToString(encodedutf16Bytes);
        System.out.println("encodedString : "+encodedString);
    }

}
