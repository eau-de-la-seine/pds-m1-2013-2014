package com.m1big1.businesslayer.server.servlet;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.common.Transmitter;


/**
 *<p>This class is useful if you want to factorize for P2B and A2B services </p>
 * @see P2BServlet
 * @see A2BServlet
 * @author Gokan EKINCI
 */
public abstract class AbstractServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(AbstractServlet.class);
    
    // Attributes which are specific to each servlet
    protected String dtoPackageName;
    protected String actionPackageName;
    
    // Attributes which are the same for each servlet
    protected final static ClientFactory CLIENT_FACTORY = new ClientFactory();
    protected static Connection connection;
    protected static Session session;
    protected static Transmitter transmitterToAccess;
    protected static Context envContext;
    
    
    /**
     * <p>Initialize AbstractServlet's static attributes</p>
     */
    static {
        try {
            Context context = new InitialContext();            
            envContext = (Context) context.lookup("java:comp/env");
            Context jmsContext = (Context) context.lookup("java:comp/env/jms");
            ConnectionFactory connectionFactory = (ConnectionFactory) jmsContext.lookup("connectionFactory");
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            transmitterToAccess = new Transmitter(session, (Destination) jmsContext.lookup("b2a_jms_queue"));
        } catch (NamingException e) {
            logger.fatal("An error happened when using lookup in AbstractServlet", e);
        } catch (JMSException e) {
            logger.fatal("An error happened when initializing the ConnectionFactory in AbstractServlet", e);
        } 
    }

}
