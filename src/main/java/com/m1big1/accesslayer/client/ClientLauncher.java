package com.m1big1.accesslayer.client;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClassLoader;
import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;
import com.m1big1.accesslayer.client.rmi.Consumer;

/**
 * <p>This class launchs the <b>Client RMI</b> that listens to the message in the <b>JMS Queue</b>. If it finds a message, the message will be sent
 * via the <i>Stub</i> to the <b>RMI Server</b> to be treated. The method RMI returns another message and this one message will be sent to
 * the <b>Business Layer</b>.</p>
 * <p>There is three step in the launch of the client RMI:<br>
 * - Step 1: Look for the JMS Queue<br>
 * - Step 2: Create the <i>RMISecurityManager</i> and then look for the <b>RMI Service</b>.<br>
 * - Step 3: Create the <i>Consumer</i> that listens to the message queue and sends to the <b>RMI Server</b>.<br>
 * </p>
 * @author NGUYEN Huu Khuong
 * @category RMIClient
 */
public class ClientLauncher {
    private static final    Logger  logger = Logger.getLogger(ClientLauncher.class);
    private static final    String  ACCESS_PROPERTIES_FILE = "configuration/accesslayer.properties";
    
    private static          int     rmiServicePort;
    private static          String  rmiServiceMethod;
    private static          String  rmiServiceClass;
    private static          String  rmiServiceCodebase;
    private static          String  rmiServiceUrl;
    
    private static          String  b2aJMSQueueName;
    private static          String  a2bJMSQueueName;
    
    static {
        try {
            InputStream propertiesFile = Thread.currentThread().getContextClassLoader().getResourceAsStream(ACCESS_PROPERTIES_FILE);
            Properties properties = new Properties();
            properties.load(propertiesFile);
            propertiesFile.close();
            
            //RMI variables
                rmiServicePort      = Integer.parseInt(properties.getProperty("RMI_Service_Port"));

                rmiServiceCodebase  = properties.getProperty("RMI_Service_Codebase");
                rmiServiceUrl       = properties.getProperty("RMI_Service_URL");
                
                rmiServiceMethod    = properties.getProperty("RMI_Service_Method");
                rmiServiceClass     = properties.getProperty("RMI_Service_Class");
                
            //JMS variables
                b2aJMSQueueName     = properties.getProperty("b2a_jms_queue");
                a2bJMSQueueName     = properties.getProperty("a2b_jms_queue");
                
        } catch(IOException e) { }
    }
    
    public static void main(String[] args) {
        logger.info("============================================================================================================================================================");
        logger.info("RMIClient is starting...");
        
        try {
            logger.info("JMS : Looking for the JMS Queue : "+b2aJMSQueueName+" and "+a2bJMSQueueName);
                Context context = new InitialContext();
                ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("connectionFactory");
                Connection connection = connectionFactory.createConnection();
                Destination b2aJMSQueue = (Destination) context.lookup(b2aJMSQueueName);
                Destination a2bJMSQueue = (Destination) context.lookup(a2bJMSQueueName);
            logger.info("JMS : DONE. Connection etablished.");

            logger.info("Security Manager : Looking for Security Manager.");
            if(System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager());
                logger.info("Security Manager : new RMISecurityManager is created.");
            }
            logger.info("Security Manager : DONE.");
            
            System.setProperty("java.rmi.server.codebase", rmiServiceCodebase);
            RMIClassLoader.loadClass(rmiServiceCodebase, rmiServiceClass);

            logger.info("RMI : Looking for the RMI Service");
                Registry    registry        = LocateRegistry.getRegistry(rmiServicePort);
                Remote      service         = registry.lookup(rmiServiceUrl);
                Method      serviceMethod   = service.getClass().getDeclaredMethod(rmiServiceMethod, String.class);
            logger.info("RMI : DONE. Connection etablished.");

            logger.info("Creating the message consumer.");
            new Consumer(connection, b2aJMSQueue, a2bJMSQueue, service, serviceMethod);
            logger.info("RMIClient is ready.");
        } catch (MalformedURLException | ClassNotFoundException e) {
            logger.fatal("RMIClassLoader can not load the class in the codebase.", e);
        } catch (NoSuchMethodException e) {
            logger.fatal("The RMI Service Method not found in the class definition.", e);
        } catch (RemoteException | NotBoundException e) {
            logger.fatal("RMIService lookup failed.", e);
        } catch (NamingException e) {
            logger.fatal("JMS Service lookup failed.", e);
        } catch (JMSException e) {
            logger.fatal("Can not etablish JMS Service.", e);
        }
    }
}
