package com.m1big1.common;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.MessageListener;
import org.apache.log4j.Logger;


/**
 *<p>Implement this class when you need to receive asynchronous messages from a JMS Queue</p>
 * @author Gokan EKINCI
 */
public abstract class AbstractMessageListener implements MessageListener {

    private static Logger logger = Logger.getLogger(AbstractMessageListener.class);
    
    /**
     * <p>Handler of the MessageListener</p>
     * @param message    The received message
     */
    @Override
    public void onMessage(Message message){
        if (message instanceof TextMessage) {
            try {
            TextMessage textMessage = (TextMessage) message;
                layerImplementation(textMessage.getText());
            } catch (JMSException e) {
                logger.warn("Due to TextMessage.getText() in AbstractmessageListener", e);
            }
        }
    }  
    
    
    /**
     * <p>This contains the action to execute when you receive a message</p>
     * @param receivedMessage    The received message, use it on your layer implementation
     */
    public abstract void layerImplementation(String receivedMessage);
}

