import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class readClassNameInJar {
    public static void main(String args[]) throws IOException {
        Set<String> classNames = new HashSet<>();
        String givenFile = "E:\\DependencyFinder\\DC_DF_Data\\DC_112233001\\AdventNetDesktopCentral.jar";
        try (JarFile jarFile = new JarFile(givenFile)) {
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName()
                            .replace("/", ".")
                            .replace(".class", "");
                    classNames.add(className);
                }
            }
            System.out.println(classNames);
        }
    }
}
