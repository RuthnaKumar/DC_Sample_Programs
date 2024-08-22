import java.io.*;
import java.util.jar.*;
import java.util.Enumeration;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LineNumberAttribute;
import javassist.bytecode.MethodInfo;

public class GetClassDetailsInJar {

    public static void main(String[] args) {


        String basePath = "E:\\DependencyFinder\\DC_DF_Data\\EC_113242001\\";
        String jarFilePath = basePath+"AdventNetDesktopCentral.jar";
        String csvFilePath = basePath+"AdventNetDesktopCentral.csv";
        String jarFileName = new File(jarFilePath).getName();

        try (JarFile jarFile = new JarFile(jarFilePath);
             BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {

            writer.write("Jar File,Class Name,Line Count\n");

            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace('/', '.').replace(".class", "");
                    int lineCount = getClassLineCount(jarFile, entry);
                    writer.write(String.format("%s,%s,%d\n", jarFileName, className, lineCount));
                }
            }

        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
        }
    }

    private static int getClassLineCount(JarFile jarFile, JarEntry entry) throws IOException, NotFoundException {
        try (InputStream classInputStream = jarFile.getInputStream(entry)) {
            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.makeClass(classInputStream);

            CtMethod[] methods = ctClass.getDeclaredMethods();
            int lineCount = 0;
            for (CtMethod method : methods) {
                MethodInfo methodInfo = method.getMethodInfo();
                CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
                if (codeAttribute != null) {
                    LineNumberAttribute lineNumberAttribute = (LineNumberAttribute) codeAttribute.getAttribute(LineNumberAttribute.tag);
                    if (lineNumberAttribute != null) {
                        lineCount += lineNumberAttribute.tableLength();
                    }
                }
            }
            return lineCount;
        }
    }
}
