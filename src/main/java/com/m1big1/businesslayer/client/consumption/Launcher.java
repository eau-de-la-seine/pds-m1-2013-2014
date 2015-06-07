package com.m1big1.businesslayer.client.consumption;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;


/* Test
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ConnectionDTO login="huu" password="toto" language="French" />

OK ********** DTO pour Lecture d'un noeud de type WORD lors d'un double clic
OK ********** Faire en sorte que ErrorDTO hérite de DTO, sinon Oriel aura une Exception
OK ********** Voir pour l'API Apache Word
NO ********** Ajouter le cas du nom vide du CreateNodeDTO ou RenameNodeDTO au niveau Metier
*/



/**
 *<p>This class launches the Business Layer client</p>
 * @author Gokan EKINCI
 */
public class Launcher {
    private static Logger logger = Logger.getLogger(Launcher.class);
    
    /**
     * <p>The main method which initialize parameters</p>
     */
    public static void main(String[] args) throws IOException{
        try {
            
            // Getting informations from the properties file for the HTTP and other parameters :
            Properties properties = new Properties();
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream propertiesFile = classLoader.getResourceAsStream("configurations.properties");
            properties.load(propertiesFile);
            propertiesFile.close();    
            
            // Getting informations from the JNDI InitialContext for the JMS queue :
            Context context = new InitialContext();       
            ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("connectionFactory");        
            
            // Initialize P2B location : Destination of the p2b queue, and HTTP service (URL + parameter) : 
            Destination p2bJMSQueue = (Destination) context.lookup("p2b_jms_queue");
            String p2bUrlHttpService = properties.getProperty("p2b_http_url");
            String p2bUrlParameter = properties.getProperty("p2b_url_param");
            
            // Initialize A2B location : Destination of the a2b queue, and HTTP service (URL + parameter) :  
            Destination a2bJMSQueue = (Destination) context.lookup("a2b_jms_queue");
            String a2bUrlHttpService = properties.getProperty("a2b_http_url");
            String a2bUrlParameter = properties.getProperty("a2b_url_param");

            // Launching P2B and A2B consumers :
            new Consumer(connectionFactory, p2bJMSQueue, p2bUrlHttpService, p2bUrlParameter);
            new Consumer(connectionFactory, a2bJMSQueue, a2bUrlHttpService, a2bUrlParameter);
            logger.info("Business Client is launched.");
        } catch (JMSException e) {
            logger.fatal("Because of JMS Queue connection / initialisation !", e);
        } catch (NamingException e) {
            logger.fatal("Because of the Initial context / lookup methods !", e);
        }
    }
}
