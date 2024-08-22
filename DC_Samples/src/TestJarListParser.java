import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TestJarListParser {

    public static void main(String[] args) {
        try {
            // Parse the XML file
            File inputFile = new File("C:\\Jenkins_slave\\workspace\\test-JOB\\TestJarList.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // Extract class dependencies
            HashMap<Integer, String> classMap = new HashMap<>();
            List<Dependency> dependencies = new ArrayList<>();

            NodeList nList = doc.getElementsByTagName("class");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String className = eElement.getAttribute("className");
                    String packageName = ((Element) eElement.getParentNode()).getAttribute("package");
                    int uniqueId = Integer.parseInt(eElement.getAttribute("uniqueId"));
                    String fullClassName = packageName + "." + className;
                    classMap.put(uniqueId, fullClassName);

                    String dependenciesStr = eElement.getAttribute("dependencies").replaceAll("[\\[\\]]", "");
                    if (!dependenciesStr.isEmpty()) {
                        String[] dependencyIds = dependenciesStr.split(",");
                        for (String dependencyId : dependencyIds) {
                            dependencies.add(new Dependency(fullClassName, Integer.parseInt(dependencyId.trim())));
                        }
                    }
                }
            }

            // Write the dependencies to a CSV file
            FileWriter csvWriter = new FileWriter("C:\\Jenkins_slave\\workspace\\test-JOB\\dependencies.csv");
            csvWriter.append("Test Class,Dependency Class,Dependency Count\n");

            for (Dependency dependency : dependencies) {
                String dependentClass = dependency.dependentClass;
                String dependencyClass = classMap.get(dependency.dependencyId);
                long dependencyCount = dependencies.stream().filter(d -> d.dependencyId == dependency.dependencyId).count();
                csvWriter.append(dependentClass).append(",")
                        .append(dependencyClass).append(",")
                        .append(String.valueOf(dependencyCount)).append("\n");
            }

            csvWriter.flush();
            csvWriter.close();

            System.out.println("CSV file created successfully.");

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    static class Dependency {
        String dependentClass;
        int dependencyId;

        Dependency(String dependentClass, int dependencyId) {
            this.dependentClass = dependentClass;
            this.dependencyId = dependencyId;
        }
    }
}
