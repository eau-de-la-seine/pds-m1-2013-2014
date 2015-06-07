package com.m1big1.accesslayer.client.rmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.Remote;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.log4j.Logger;

import com.m1big1.common.AbstractMessageListener;
import com.m1big1.common.Receiver;
import com.m1big1.common.Transmitter;

/**
 * <p>This class is the Message Consumer that listens to the JMS Message Queue. A message found will be sent to the RMI Server via RMI
 * and another message will be returned to this Message Consumer via RMI. This one message will be sent to the <b>Business Layer</b>.</p>
 * @author NGUYEN Huu Khuong
 * @category RMIClient
 */
public class Consumer { 
    private static final    Logger      logger = Logger.getLogger(Consumer.class);
    private                 Method      rmiServiceMethod;
    private                 Remote      rmiServiceStub;
    private                 Session     jmsSession;
    private                 Transmitter transmitter;
    
    /**
     * <p>Create a <b>Consumer</b> with these parameters :<br>
     * - The JMS Connection.<br>
     * - Two JMS queues (<b>Business2Access</b> and <b>Access2Business</b>).<br>
     * - The RMI Stub<br>
     * - The RMI Method<br>
     * The Consumer will create a JMS Session, a Receiver, a Transmitter.<br>
     * This Consumer will listen to the <b>Business2Access</b> until the receive of a new message, then query the Server to treat the message and
     * sends the message returned to the queue <b>Access2Business</b>.
     * </p>
     * @param jmsConnection     The JMS Connection.
     * @param b2aJmsQueue       The b2a Queue to receive message from Business Layer to Access Layer.
     * @param a2bJmsQueue       The a2b Queue to receive message from Business Layer to Access Layer.
     * @param rmiServiceStub    The RMI Service Object that exists in RMI Server to treat the message.
     * @param rmiServiceMethod  The RMI Service Method that treat message.
     * @throws JMSException     The JMSException is caused by connection JMS.
     */
    public Consumer(
            Connection  jmsConnection,
            Destination b2aJmsQueue,
            Destination a2bJmsQueue,
            Remote      rmiServiceStub,
            Method      rmiServiceMethod
            ) throws JMSException {
        this.rmiServiceStub = rmiServiceStub;
        this.rmiServiceMethod = rmiServiceMethod;
        
        jmsConnection.start();
        jmsSession  = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        transmitter = new Transmitter(jmsSession, a2bJmsQueue);
        
        new Receiver(jmsSession, b2aJmsQueue, accessGetListener());
    }
    
    /**
     * <p>The method that creates and returns a JMS listener.</p>
     * @return AbstractMessageListener  A listener that listens to the queue and call the RMI Service Method.
     */
    public AbstractMessageListener accessGetListener() {
        return new AbstractMessageListener() {
            public void layerImplementation(String message) {
                    try {
                        logger.info("The listener received the message input :\n" + message);
                        String result = rmiServiceMethod.invoke(rmiServiceStub, message).toString();
                        logger.info("The transmitter will send the message output :\n" + result);
                        transmitter.send(result);
                    } catch (IllegalArgumentException e) {
                        logger.fatal("The RMI Service Method can not be done. The argument(s) is inappropriate.", e);
                    } catch (IllegalAccessException e) {
                        logger.fatal("The RMI Service Method can not be done. The access is illegal.", e);
                    } catch (InvocationTargetException e) {
                        logger.fatal("The RMI Service Method can not be done.", e);
                    } catch (JMSException e) {
                        logger.fatal("The transmitter can not send the message.", e);
                    }
            }
        };
    }
}
