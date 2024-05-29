import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.*;
import java.util.*;
import java.lang.reflect.*;
import com.opencsv.CSVWriter;

public class JarParser {

    static String basePath = "E:\\DependencyFinder\\DC_DF_Data\\DESKTOPCENTRAL_11_3_2414_1\\";

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
                    if (true) {
                        try {
                            // Load the class using URLClassLoader
                            Class<?> clazz = dependencyClassLoader.loadClass(className);

                            // Get all methods of the class
                            Method[] methods = clazz.getDeclaredMethods();

                            // Count lines of code for the class
                            int classLines = getClassLines(clazz);

                            // Open the CSV file for writing
                            try (CSVWriter writer = new CSVWriter(new FileWriter(basePath + File.separator + "output.csv", true))) {
                                // Write the header if the file is empty
                                if (jarFile.length() == 0) {
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
                            handleClassNotFound(jarFile, className);
                        } catch (NoClassDefFoundError e) {
                            handleMethodNotFound(jarFile, className);
                        } catch (IllegalAccessError e) {
                            handleMethodNotFound(jarFile, className);
                        } catch (IncompatibleClassChangeError e) {
                            handleIncompatibleClassChange(jarFile, className);
                        } catch (Exception e) {
                            handleMethodNotFound(jarFile, className);
                        }
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

    private static void handleClassNotFound(File jarFile, String className) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(basePath + File.separator + "output.csv", true))) {
            writer.writeNext(new String[]{jarFile.getName(), className, "ClassNotFound", "0"});
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void handleMethodNotFound(File jarFile, String className) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(basePath + File.separator + "output.csv", true))) {
            writer.writeNext(new String[]{jarFile.getName(), className, "MethodNotFound", "0"});
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void handleIncompatibleClassChange(File jarFile, String className) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(basePath + File.separator + "output.csv", true))) {
            writer.writeNext(new String[]{jarFile.getName(), className, "IncompatibleClassChange", "0"});
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
