package com.m1big1.common;

import java.io.IOException;
import java.io.StringWriter;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;
import javax.xml.bind.JAXB;


/**
 *<p>Use this class when you need to send a message</p>
 * @author Gokan EKINCI
 */
public final class Transmitter {
    private MessageProducer producer;
    private TextMessage textMessage;
    
    /**
     * <p>Transmitter's constructor</p>
     * @param session        The session obtained from the JMS connection
     * @param destination    The destination where you want to send your message
     * @throws               Can launch an exception when you want it creates a text message or a message producer
     */
    public Transmitter(Session session, Destination destination) throws JMSException {
        if(session == null) throw new IllegalArgumentException("Session object cannot be null !");
        if(destination == null) throw new IllegalArgumentException("Destination object cannot be null !");
        
        textMessage = session.createTextMessage();
        producer = session.createProducer(destination);
    } 
    
    
    /**
     * <p>Transforms an object to XML</p>
     * <ul>Exemple of use :
     *     <li>Transmitter.objectToXml(myDTOObject);</li>
     * </ul>
     * @param object         The dto object you want to transform into XML
     * @throws               Can launch an exception when writting in the StringWriter
     */
    public final static <T> String objectToXml(T object) throws IOException {        
        // Send the XML flow into a writer object :
        StringWriter sw = new StringWriter();
        JAXB.marshal(object, sw);
        String xml = sw.toString();
        sw.close();  
        return xml;
    }
    
    /**
     * <p>Send an XML message to queue destination</p>
     * @param xml         The dto object you want to transform into XML
     * @throws            Can launch an JMS exception when setting or sending the TextMessage
     */
    public void send(String xml) throws JMSException {
        textMessage.setText(xml);
        producer.send(textMessage);
    }
}
