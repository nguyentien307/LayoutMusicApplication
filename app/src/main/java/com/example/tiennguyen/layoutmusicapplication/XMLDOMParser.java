package com.example.tiennguyen.layoutmusicapplication;

/**
 * Created by TIENNGUYEN on 5/10/2017.
 */

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLDOMParser {
    public Document getDocument(String xml)
    {
        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder db = factory.newDocumentBuilder();
            //InputSource is = new InputSource();
            //is.setCharacterStream(new StringReader(xml));
            //is.setEncoding("UTF-8");
            byte[] bytes = Charset.forName("UTF-8").encode(xml).array();
            InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            document = db.parse(is);

        }catch(ParserConfigurationException e)
        {
            Log.e("Error: ", e.getMessage(), e);
            return null;
        }
        catch (SAXException e) {
            Log.e("Error: ", e.getMessage(), e);
            return null;
        }
        catch(IOException e){
            Log.e("Error: ", e.getMessage(), e);
            return null;
        }
        return document;
    }
    public String getValue(Element item, String name)
    {
        NodeList nodes = item.getElementsByTagName(name);
        return this.getTextNodeValue(nodes.item(0));
    }

    public String getValue(org.w3c.dom.Document doc, String nameNode){
        NodeList nodeList = doc.getElementsByTagName(nameNode);
        String name = nodeList.item(0).getTextContent();
        return  name;
    }

    public String getValueData(Element item, String nameNode){
        NodeList nodeList = item.getElementsByTagName(nameNode);
        String name = nodeList.item(0).getTextContent();
        return  name;
    }

    private final String getTextNodeValue(Node elem) {
        Node child;
        if( elem != null){
            if (elem.hasChildNodes()){
                for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                    if( child.getNodeType() == Node.TEXT_NODE  ){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    public static String LoadDataFromURL(String theUrl) {
        StringBuilder content = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(theUrl);

            Log.d("URL", String.valueOf(url));
            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}