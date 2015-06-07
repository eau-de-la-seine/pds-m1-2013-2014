package com.m1big1.presentationlayer.client.swing;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Session;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.m1big1.common.Transmitter;
import com.m1big1.presentationlayer.client.connection.ImplementationMessageListener;
/**
 * <p>Class whith start application , it initialise connection to activeMQ and open the first frame</p>
 * @author Oriel SAMAMA
 *
 */
public class Launcher {
	private static Logger logger = Logger.getLogger(Launcher.class);
    /**
     * <p>Main method of launcher</p>
     * @param args
     */
    public static void main(String[] args) {
        // Initialisation connection and queue
        Connection                    connection      = null;
        Transmitter                   transmitter     = null;
        ImplementationMessageListener messageListener = null;
        InitialContext                cntx            = null;

        try {
            cntx = new InitialContext();
            // Creation of factory with JNDI informations
            ConnectionFactory factory = (ConnectionFactory) cntx.lookup("connectionFactory");
            //Create connection
            connection                = factory.createConnection();
            //Start connection
            connection.start();
            logger.info("Connection started");
            // Creation of session
            Session session           = connection.createSession(false , Session.AUTO_ACKNOWLEDGE);
            logger.info("Session started");
            // Selection of queue for send
            Destination sendDest      = (Destination) cntx.lookup("send");
            // Create the object for sending XMLs
            transmitter               = new Transmitter(session , sendDest);
            messageListener           = new ImplementationMessageListener();
            logger.info("Object for reception started");
            //Initialise the LoginFrame
            LoginFrame loginFrame     = new LoginFrame(transmitter , messageListener , session);
            //Show window login frame
            loginFrame.showWindows();
            //Make loginframe in message listener class
            messageListener.setLoginFrame(loginFrame);
        } catch (Exception e) {
        	logger.warn("Problem with connection to ActiveMQ" , e);
        }
        
    }

}
