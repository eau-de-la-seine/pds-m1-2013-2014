package com.m1big1.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.xml.bind.JAXB;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


/**
 *<p>Use this class when you need to receive a message</p>
 * @author Gokan EKINCI
 */
public final class Receiver { 
    private MessageConsumer messageConsumer;
    
    
    /**
     * <p>Receiver's constructor</p>
     * @param session        The session obtained from the JMS connection
     * @param destination    The destination from where you want to receive your messages
     * @throws               Can launch an exception when you want it creates a message consumer or setting the message listener
     */
    public Receiver(Session session, Destination destination, MessageListener messageListener) throws JMSException{
        if(session == null) throw new IllegalArgumentException("Session object cannot be null !");
        if(destination == null) throw new IllegalArgumentException("Destination object cannot be null !");
        if(messageListener == null) throw new IllegalArgumentException("MessageListener object cannot be null !");
        
        messageConsumer = session.createConsumer(destination);
        messageConsumer.setMessageListener(messageListener); // A thread is launched
    }
    
    
    /**
     * <p>Obtain the XML's root name</p>
     * @param xml         The xml to analyse
     * @throws            Multiple exceptions
     */
    private static String getXmlRootName(String xml) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        Element root = doc.getDocumentElement();
        return root.getTagName();
    }
    
    /**
     * <p>Transforms an XML to object</p>
     * <ul>Exemple of use :
     *     <li>Receiver.xmlToObject("com.blabla.dto", myXMLMessage);</li>
     * </ul>
     * @param packageName    Package name of the DTO object
     * @throws               Multiple exceptions
     */
    public final static <T> T xmlToObject(String packageName, String xmlMessage) 
            throws ClassNotFoundException, UnsupportedEncodingException, ParserConfigurationException, SAXException, IOException {
        StringReader reader = new StringReader(xmlMessage);
        T dtoObject = (T) JAXB.unmarshal(reader, Class.forName(packageName + "." + getXmlRootName(xmlMessage)));
        reader.close();
        return dtoObject;
    }
}

