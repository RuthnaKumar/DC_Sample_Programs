import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.asm.ClassParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;
public class ClassParserExample {
    public static void main(String[] args) throws IOException, SAXException, TikaException {
        BodyContentHandler handler   = new BodyContentHandler();
        ClassParser parser           = new ClassParser();
        Metadata metadata            = new Metadata();
        ParseContext pcontext        = new ParseContext();
        try (InputStream stream = new FileInputStream(new File("C:\\Users\\ruthna-12510\\Downloads\\importClassFinder\\AdventNetTestSuite\\lib\\com\\me\\dc\\appCtrl\\agentServletCalls\\preliminary\\test\\ACPServletCallTest.class"))) {
            parser.parse(stream, handler, metadata, pcontext);
            System.out.println("Document Content:" + handler.toString());
            System.out.println("Document Metadata:");
            String[] metadatas = metadata.names();
            for(String data : metadatas) {
                System.out.println(data + ":   " + metadata.get(data));
            }
        }catch(Exception e) {System.out.println("Exception message: "+ e.getMessage());}
    }
}