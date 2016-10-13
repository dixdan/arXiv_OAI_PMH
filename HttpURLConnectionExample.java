package arXiv_OAI_PMH;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class HttpURLConnectionExample {

  private final String USER_AGENT = "Mozilla/5.0";
  //static String date;
  
  public static void main(String[] args) throws Exception {
	  
	//String date = args[0]; 
	
    HttpURLConnectionExample http = new HttpURLConnectionExample();

    System.out.println("Testing 1 - Send Http GET request");
    http.sendGet();

    //System.out.println("\nTesting 2 - Send Http POST request");
    //http.sendPost();
    
    String xmlString = readXmlFromFile("F:/newfile1.xml");
    System.out.println("original xml:\n" + xmlString + "\n");
    System.out.println("divided xml:\n");
    List<String> dividedXmls = divideXmlByTag(xmlString, "record");
    int i=1;
    FileOutputStream fop = null;
	File file;
    for (String xmlPart : dividedXmls) {
    	
    	file = new File("G:/arXiv/" + i + ".xml");
    	//file = new File(args[1] + i + ".xml");
		fop = new FileOutputStream(file);
    	
		byte[] contentInBytes = xmlPart.getBytes();
		
		fop.write(contentInBytes);
		fop.flush();
		fop.close();
    	
    	i++;
    }
  }

  // HTTP GET request
  private void sendGet() throws Exception {

    //String url = "http://export.arxiv.org/oai2?verb=ListRecords&from="+date+"&&metadataPrefix=oai_dc";
    String url = "http://export.arxiv.org/oai2?verb=ListRecords&from=2016-01-05&&metadataPrefix=oai_dc";

    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    // optional default is GET
    con.setRequestMethod("GET");

    //add request header
    con.setRequestProperty("User-Agent", USER_AGENT);

    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'GET' request to URL : " + url);
    System.out.println("Response Code : " + responseCode);

    BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    //print result
    //System.out.println(response.toString());
    
  //writing to file
    FileOutputStream fop = null;
    File file;
    try {

          file = new File("F:/newfile1.xml");
          fop = new FileOutputStream(file);

          // if file doesn't exists, then create it
          if (!file.exists()) {
              file.createNewFile();
          }

          // get the content in bytes
          String xmlString = response.toString();
          System.out.println(xmlString);
          byte[] contentInBytes = xmlString.getBytes();

          fop.write(contentInBytes);
          fop.flush();
          fop.close();

          System.out.println("Done");

      } catch (IOException e) {
          e.printStackTrace();
      } finally {
          try {
              if (fop != null) {
                  fop.close();
              }
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
  }
    
    public static String readXmlFromFile(String fileName) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String lineSeparator = System.getProperty("line.separator");

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(lineSeparator);
        }

        return stringBuilder.toString();
    }
    
    public static List<String> divideXmlByTag(String xml, String tag) throws Exception {
        List<String> list = new ArrayList<String>();
        Document document = loadXmlDocument(xml);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        NodeList rowList = document.getElementsByTagName(tag);
        for(int i=0; i<rowList.getLength(); i++) {
            Node rowNode = rowList.item(i);
            if (rowNode.getNodeType() == Node.ELEMENT_NODE) {
                DOMSource source = new DOMSource(rowNode);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                StreamResult streamResult = new StreamResult(baos);
                transformer.transform(source, streamResult);
                list.add(baos.toString());
            }
        }
        return list;
    }
    
    private static Document loadXmlDocument(String xml) throws SAXException, IOException, ParserConfigurationException {
        return loadXmlDocument(new ByteArrayInputStream(xml.getBytes()));
    }

    private static Document loadXmlDocument(InputStream inputStream) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = null;
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputStream);
        inputStream.close();
        return document;
    }
    

  
}
