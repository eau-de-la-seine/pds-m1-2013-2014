package com.m1big1.accesslayer.server;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import com.m1big1.accesslayer.server.rmi.RMIServer;

/**
 * <p>The ServerLauncher is the main class. It launchs the <b>RMIServer</b> to start the service.</p>
 * @Author Huu Khuong NGUYEN
 * @category Access Core
 */
public class ServerLauncher {
    private static final Logger logger = Logger.getLogger(ServerLauncher.class);
    
    public static void main(String[] args) {
        try {
            logger.info("============================================================================================================================================================");
            logger.info("RMI Server is starting...");
            RMIServer server = new RMIServer();
            server.go();
            logger.info("RMI Server is ready.");
        } catch (RemoteException e) {
            logger.fatal("RMIException. RMI Server can not be started.", e);
        }
    }
}
