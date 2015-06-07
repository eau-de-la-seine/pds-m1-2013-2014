package com.m1big1.businesslayer.client.consumption;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.NamingException;
import org.apache.log4j.Logger;
import com.m1big1.common.AbstractMessageListener;
import com.m1big1.common.Receiver;

/**
 *<p>The consumer receives messages from the Queue and send them to the Business Layer server
 * which contains a HTTP service.</p>
 * @author Gokan EKINCI
 */
public class Consumer {
    private static Logger logger = Logger.getLogger(Consumer.class);
    private Connection connection;
    private String urlHttpService;
    private String urlParameter;
    private Executor executor;
    
    
    /**
     * <p>Consumer's constructor</p>
     * @param clientFactory     JMS Connection Factory
     * @param JMSQueue          The JMS Queue to listen
     * @param urlHttpService    The HTTP service's URL which receive the message
     * @param urlParameter      An URL parameter which contains the DTO's XML format
     */
    public Consumer(ConnectionFactory connectionFactory, Destination JMSQueue, String urlHttpService, String urlParameter) throws JMSException, NamingException, IOException{        
        this.urlHttpService = urlHttpService;
        this.urlParameter = urlParameter;
        executor = Executors.newFixedThreadPool(10);
        connection = connectionFactory.createConnection();
        connection.start();        
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        new Receiver(session, JMSQueue, queueListener());
    }
    
    /**
     * <p>Business Layer's Message Listener implementation</p>
     * <p>This implementation sends HTTP requests to the Business Layer's HTTP service</p>
     * <p>For each consumer (P2B and A2B), the thread pool can treat until 10 tasks at the same time</p>
     */
    public AbstractMessageListener queueListener(){
        return new AbstractMessageListener(){
            @Override
            public void layerImplementation(final String message) {
                executor.execute(new Runnable(){
                    @Override
                    public void run() {
                        logger.info(message);
                        sendHttp(message);
                    } 
                });
            }          
        };
    }
    
    
    /**
     * <p>HTTP request sending's implementation</p>
     * @param message   The message to send with a HTTP request 
     */
    private void sendHttp(String message){
        DataOutputStream out = null;               
        
        try {
            String messageToSend = urlParameter + "=" + URLEncoder.encode(message, "UTF-8");
            System.out.println("Crotte : " + messageToSend);
            URL url = new URL(urlHttpService);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection(); 
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setInstanceFollowRedirects(false); 
            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
            httpConnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpConnection.setRequestProperty("charset", "utf-8");
            httpConnection.setUseCaches(false);
            httpConnection.setRequestProperty("Content-Length", "" + Integer.toString(messageToSend.getBytes().length));
            out = new DataOutputStream(httpConnection.getOutputStream());
            out.writeBytes(messageToSend);
            out.flush();  
            httpConnection.getResponseCode();
            httpConnection.disconnect();  
        } catch (MalformedURLException e) {
            logger.error("Problem has happened with the URL object's constructor", e);
        } catch (IOException e){
            logger.error("Problem has happened with the HTTP connect/socket or writeBytes", e);
        } finally {
            try {
                if(out != null) out.close(); // Use it after httpConnection.disconnect();
            } catch (IOException e) {
                logger.error("Closure of the \"DataOutputStream\" object has failed !", e);
            }
        }
    }
    
    
    /**
     * <p>Closing the JMS Connection when closing the application</p>
     */
    @Override
    public void finalize(){
        try {
            if(connection != null) connection.close();
        } catch (JMSException e) {
            logger.error("Closure of the \"connection\" object has failed !", e);
        }
    }
      
}
