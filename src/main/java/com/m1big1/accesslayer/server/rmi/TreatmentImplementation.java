package com.m1big1.accesslayer.server.rmi;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.m1big1.accesslayer.server.action.Action;
import com.m1big1.accesslayer.server.dto.DTO;
import com.m1big1.common.Receiver;
import com.m1big1.common.Transmitter;

/**
 *  <p>The class is an implementation class of the interface <b>ITreatment</b>. The class aims to treat the <i>message input</i> received from the <b>RMI Client</b>
 *  and then return a <i>message output (results)</i> to the <b>RMI Client</b> to be sended to the <b>Business Layer</b>.</p>
 * @author NGUYEN Huu Khuong
 * @category RMI
 */
public class TreatmentImplementation extends UnicastRemoteObject implements ITreatment {
    private static final    Logger  logger          = Logger.getLogger(TreatmentImplementation.class);
    private static final    String  propertiesFiles = "configuration/accesslayer.properties";

    private static          String  dtoPackageName;
    private static          String  actionPackageName;
    
    static {
        try {
            InputStream propertiesFile      = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFiles);
            Properties  properties          = new Properties();
            properties.load(propertiesFile);
            propertiesFile.close();

            dtoPackageName      = properties.getProperty("dto_package_name");
            actionPackageName   = properties.getProperty("action_package_name");
        } catch(IOException e) {
            logger.error("The properties file can not be loaded.", e);
        }
    }
    
    /**
     * <p>Creates an TreatmentImplementation with an attribut action that will treats the message input.</p>
     * @throws RemoteException
     */
    public TreatmentImplementation() throws RemoteException {
        logger.info("The RMI Service is created.");
    }
    
    @Override
    public String treatXml(String messageInput) throws RemoteException {
        String messageOutput = "";
        try {
            DTO dtoInput                = Receiver.xmlToObject(dtoPackageName, messageInput);
            
            Class<?>        actionClass = Class.forName(actionPackageName + "." + dtoInput.getClass().getSimpleName() + "Action");
            Constructor<?>  construct   = actionClass.getConstructor(dtoInput.getClass());
            Action<?>       action      = (Action<?>) construct.newInstance(dtoInput);
            DTO             dtoOutput   = action.execute();
            messageOutput               = Transmitter.objectToXml(dtoOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageOutput;
    }
}
