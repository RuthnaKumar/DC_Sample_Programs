import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.*;
import java.util.*;
import java.lang.reflect.*;
import com.opencsv.CSVWriter;

public class JarParser {

    static String basePath = "E:\\DependencyFinder\\DC_DF_Data\\EC_113242001\\";

    public static void main(String[] args) {
        String[] jarFileNames = {
                "AdventNetDesktopCentral.jar",
                // Add more JAR file names here if needed
        };

        for (String jarFileName : jarFileNames) {
            File jarFile = new File(basePath + jarFileName);
            parseJar(jarFile, "Prefix"); // Change "Prefix" to the desired prefix
        }
    }

    public static void parseJar(File jarFile, String prefix) {
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            URL[] urls = { jarFile.toURI().toURL() };
            URLClassLoader classLoader = new URLClassLoader(urls);

            // Load classes from other JAR files in the same directory
            File directory = jarFile.getParentFile();
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".jar") && !name.equals(jarFile.getName()));
            for (File file : files) {
                urls = Arrays.copyOf(urls, urls.length + 1);
                urls[urls.length - 1] = file.toURI().toURL();
            }
            URLClassLoader dependencyClassLoader = new URLClassLoader(urls, classLoader);

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace("/", ".").replace(".class", "");
                    try {
                        System.out.println("Loading class: " + className);
                        // Load the class using URLClassLoader
                        Class<?> clazz = dependencyClassLoader.loadClass(className);

                        // Get all methods of the class
                        Method[] methods = clazz.getDeclaredMethods();

                        // Count lines of code for the class
                        int classLines = getClassLines(clazz);

                        // Open the CSV file for writing
                        try (CSVWriter writer = new CSVWriter(new FileWriter(basePath + File.separator + "Adv_Class_Method.csv", true))) {
                            // Write the header if the file is empty
                            if (new File(basePath + File.separator + "Adv_Class_Method.csv").length() == 0) {
                                writer.writeNext(new String[]{"JARNAME", "CLASSNAME", "METHODNAME", "CLASSLINE"});
                            }
                            // Write the class name, method names, and line counts to the CSV file
                            for (Method method : methods) {
                                writer.writeNext(new String[]{jarFile.getName(), className, method.getName(), String.valueOf(classLines)});
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (ClassNotFoundException e) {
                        logException(jarFile, className, "ClassNotFoundException");
                    } catch (NoClassDefFoundError e) {
                        logException(jarFile, className, "NoClassDefFoundError");
                    } catch (IllegalAccessError e) {
                        logException(jarFile, className, "IllegalAccessError");
                    } catch (IncompatibleClassChangeError e) {
                        logException(jarFile, className, "IncompatibleClassChangeError");
                    } catch (Exception e) {
                        logException(jarFile, className, e.getClass().getSimpleName());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading JAR file: " + jarFile.getAbsolutePath());
            e.printStackTrace();
        }
    }

    private static int getClassLines(Class<?> clazz) {
        try (InputStream classStream = clazz.getResourceAsStream(clazz.getSimpleName() + ".class")) {
            int lines = 0;
            BufferedReader reader = new BufferedReader(new InputStreamReader(classStream));
            while (reader.readLine() != null) {
                lines++;
            }
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static void logException(File jarFile, String className, String exceptionType) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(basePath + File.separator + "output.csv", true))) {
            writer.writeNext(new String[]{jarFile.getName(), className, exceptionType, "0"});
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.err.println("Exception " + exceptionType + " occurred while processing class: " + className);
    }
}
