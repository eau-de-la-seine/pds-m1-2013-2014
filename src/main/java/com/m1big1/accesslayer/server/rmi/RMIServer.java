package com.m1big1.accesslayer.server.rmi;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * <p>RMIServer Class presents the main class of RMI Service. The class manages the name of the RMI Service, the port to be used.
 * The method go() begins the RMI Service.</p>
 * @author NGUYEN Huu Khuong
 * @category RMI
 */
public class RMIServer {
    private static final    Logger  logger                  = Logger.getLogger(RMIServer.class);
    private static final    String  ACCESS_PROPERTIES_FILE  = "configuration/accesslayer.properties";
    
    private static          String  RMI_Service_Name;
    private static          int     RMI_Service_Port;
    
    static {
        try {
            InputStream propertiesFile      = Thread.currentThread().getContextClassLoader().getResourceAsStream(ACCESS_PROPERTIES_FILE);
            Properties  properties          = new Properties();
            properties.load(propertiesFile);
            propertiesFile.close();
            
            RMI_Service_Name = properties.getProperty("RMI_Service_Name");
            RMI_Service_Port = Integer.parseInt(properties.getProperty("RMI_Service_Port"));
        } catch(IOException e) {
            logger.fatal("The configuration file can not be found", e);
        }
    }
    
    /**
     * <p>The method begin the RMI service. The method creates the class implementation to be used, then create a registry
     * and bind the class service to the URL definied.</p>
     * @throws RemoteException if the method is failed by the RMI Connection.
     */
    public void go() throws RemoteException  {
        TreatmentImplementation service     = new TreatmentImplementation();
        Registry                registry    = LocateRegistry.createRegistry(RMI_Service_Port);
        String                  url         = "rmi://localhost:" + RMI_Service_Port + "/" + RMI_Service_Name;
        registry.rebind(url, service);
        logger.info("Service created on : "+url);
    }
}
