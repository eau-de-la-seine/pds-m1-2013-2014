package com.m1big1.businesslayer.server.action;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.server.internationalisation.ErrorCode;
import com.m1big1.businesslayer.server.internationalisation.ErrorFactory;
import com.m1big1.common.Transmitter;

/**
 * <p>Parent of all Action implementation classes</p>
 *
 * @author Gokan EKINCI
 */
public abstract class Action {
    private static Logger logger = Logger.getLogger(Action.class);
    private static ErrorFactory errorFactory = new ErrorFactory();
    protected static String wordLocation;

    
    // LOADING PROPERTIES FILE
    static {
        try {
            Context context = new InitialContext();
            wordLocation = (String) context.lookup("java:comp/env/word_file_location");
        } catch (NamingException e) {
            logger.fatal("Problem with loading word_file_location", e);
        }
    }
    
    /**
     * <p>Excute implemented Action for the DTO</p>
     */
    public abstract void execute();
     
    /**
     * <p>Sending the message to the Access Layer</p>
     * @param transmitterToAccess
     * @param receivedDto
     */
    protected <T> void sendToAccessLayer(Transmitter transmitterToAccess, T receivedDto){
        try {
             transmitterToAccess.send(Transmitter.objectToXml(receivedDto));
         } catch (JMSException e) {
             logger.error("Cannot send message to Access Layer in Responses class", e);
         } catch (IOException e) {
             logger.error("Problem with objecToXml", e);
         }
    }
     
     
    /**
      * <p>Send error message to client</p>
      * @param title
      * @param message
      */
    protected void sendErrorMessageToClient(Client client, Session session, ErrorCode errorCode){
         
        // Sending the message to the client
        try {
            Transmitter transmitterToClient = new Transmitter(session, client.getDestination());
            transmitterToClient.send(Transmitter.objectToXml(errorFactory.getErrorDTO(client.getLanguage(), errorCode)));
        } catch (JMSException e) {
            logger.error("Cannot create Queue in Responses class", e);
        } catch (IOException e) {
            logger.error("Problem with objecToXml", e);
        }
    }
}
