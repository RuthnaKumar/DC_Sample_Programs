package com.zoho.detective.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XML_to_JSON_method_inclusion {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String path= "E:\\Detective_Tool\\Coverage\\WEB-INF\\";//args[0];
        ArrayList<String> files=get_xml_files(path+"web.xml");
        files.stream().forEach(System.out::println);
        assert files != null;
        ArrayList<Map<String,Map<String,HashSet<String>>>> mapArrayList=new ArrayList<>();
        /*for(String file:files){
            System.out.println(file.trim());
        }*/
        for(String file:files){
            //System.out.println(file);
            mapArrayList.add(xml_to_json(path+file.trim()));
        }
        String output="E:\\Detective_Tool\\Coverage\\output.json";
        Map<String,Map<String,HashSet<String>>> finalMap=merge_maps(mapArrayList);
        Map<String, Map<String, HashSet<String>>> finalMap2 = new HashMap<>();
        finalMap.forEach((method, entityMap ) -> {
            entityMap.forEach((entity, urlList) -> {
                Map methodVsURLs = finalMap2.getOrDefault(entity, new HashMap<>());
                List urls = (List) methodVsURLs.getOrDefault(method, new ArrayList<>());
                urls.addAll(urlList);
                methodVsURLs.put(method, urls);
                finalMap2.put(entity, methodVsURLs);
            });
        } );
//		Map finalMap3 = new HashMap();
//		finalMap3.put("skipped_urls", new ArrayList<>());
//		finalMap3.put("entity_groupings", finalMap2);
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            // Write the Map as JSON to the file
            objectMapper.writeValue(new File(output), finalMap2);
            //			objectMapper.writeValue(new File("output2.json"), finalMap2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Map<String, HashSet<String>>> xml_to_json(String filepath) throws IOException, SAXException, ParserConfigurationException {
        File xmlFile = new File(filepath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(xmlFile);
        document.getDocumentElement().normalize();
        Element rootElement = document.getDocumentElement();
        Map<String, Map<String, HashSet<String>>> methodVsEntityVsPaths = new HashMap<>();
        NodeList parentList = rootElement.getElementsByTagName("urls");
        for (int i = 0; i < parentList.getLength(); i++) {
            Element urlElement = (Element)parentList.item(i);
            String methodAttribute = urlElement.getAttribute("method");
            String entityAttribute = urlElement.getAttribute("entitytype");
            NodeList childList = rootElement.getElementsByTagName("url");
            for (int j = 0; j < childList.getLength(); j++) {
                urlElement = (Element)childList.item(j);
                methodAttribute = (!urlElement.getAttribute("method").isEmpty()) ? urlElement.getAttribute("method") : methodAttribute;
                entityAttribute = (!urlElement.getAttribute("entitytype").isEmpty()) ? urlElement.getAttribute("entitytype") : entityAttribute;
                String path = urlElement.getAttribute("path");
                if (entityAttribute == null || entityAttribute.isEmpty())
                    entityAttribute = "undefined";
                if (methodAttribute == null || methodAttribute.isEmpty())
                    methodAttribute = "get";
                JSONArray expandedUrls = expandUrls(path);
                for (String method : methodAttribute.toLowerCase().split(",")) {
                    Map<String, HashSet<String>> entityVsPaths = methodVsEntityVsPaths.getOrDefault(method, new HashMap<>());
                    HashSet<String> paths = entityVsPaths.getOrDefault(entityAttribute, new HashSet<>());
                    expandedUrls.forEach(x -> paths.add(x.toString()));
                    entityVsPaths.put(entityAttribute, paths);
                    methodVsEntityVsPaths.put(method, entityVsPaths);
                }
            }
        }
        return methodVsEntityVsPaths;
    }

    public static JSONArray expandUrls(String path) {
        JSONArray expandedUrls = new JSONArray();
        Pattern pattern1=Pattern.compile("\\((.+?(\\|.+?)*)\\)");
        Matcher matcher1 = pattern1.matcher(path);
        if (matcher1.find()) {
            String group = matcher1.group(1);
            //System.out.println(group);
            String[] parts = group.split("\\|");

            if(parts.length>1)
            {
                for(String part : parts)
                {
                    String url = path.replace("(" + group + ")", part);
                    JSONArray expUrl = expandUrls(url);
                    expUrl.forEach(x -> expandedUrls.put(x));
                    //				expandedUrls.putAll(expUrl);
                }
            }
            else
            {
                expandedUrls.put(strip_api(path));
            }
        }
        else {
            expandedUrls.put(strip_api(path));
        }

        return expandedUrls;
    }

    public static String strip_api(String path){
        Pattern pattern=Pattern.compile("/api/v[0-9]+/");
        Matcher matcher=pattern.matcher(path);
        return matcher.replaceAll("/");
    }

    public static ArrayList<String> get_xml_files(String filepath) throws ParserConfigurationException, IOException, SAXException {
        File xmlFile = new File(filepath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(xmlFile);

        document.getDocumentElement().normalize();

        Element rootElement = document.getDocumentElement();
        NodeList filterList=rootElement.getElementsByTagName("filter");
        for(int i=0;i<filterList.getLength();i++){
            Element urlElement = (Element) filterList.item(i);
            Element filter=(Element) urlElement.getElementsByTagName("filter-name").item(0);
            String filterName=filter.getTextContent();
            if(filterName.trim().equals("Security Filter")){
                Element initParam=(Element) urlElement.getElementsByTagName("init-param").item(0);
                Element paramNameElement=(Element) initParam.getElementsByTagName("param-name").item(0);
                String paramName=paramNameElement.getTextContent();
                if(paramName.trim().equals("config-file")) {
                    Element paramValue = (Element) initParam.getElementsByTagName("param-value").item(0);
                    String textContent = paramValue.getTextContent();

                    String[] files = textContent.split(",");
                    ArrayList<String> fileArray=new ArrayList<>();
                    for(String file:files){
                        if(file.endsWith(".xml")) {
                            fileArray.add(file);
                        }
                        else {
                            File file1 = new File(filepath.substring(0, filepath.lastIndexOf("/")+1) + file);
                            if(file1.isDirectory())
                            {
                                Arrays.stream(file1.list()).forEach(x -> {
                                    if(x.endsWith(".xml"))
                                        fileArray.add(file + "/" + x);
                                });
                            }
                        }
                    }
                    return fileArray;
                }
            }
        }

        return null;
    }

    public static Map<String,Map<String,HashSet<String>>> merge_maps(ArrayList<Map<String,Map<String,HashSet<String>>>> mapArrayList){
        Map<String,Map<String,HashSet<String>>> finalMap=new HashMap<>();
        for(Map<String,Map<String,HashSet<String>>> map:mapArrayList){
            for(String key:map.keySet()){
                if(finalMap.containsKey(key)){
                    Map<String,HashSet<String>> subMap=map.get(key);
                    for(String key2:subMap.keySet()) {
                        if (finalMap.get(key).containsKey(key2)){
                            finalMap.get(key).get(key2).addAll(subMap.get(key2));
                        }
                        else{
                            finalMap.get(key).put(key2,subMap.get(key2));
                        }
                    }
                }
                else {
                    finalMap.put(key,map.get(key));
                }
            }
        }
        return finalMap;
    }

}
