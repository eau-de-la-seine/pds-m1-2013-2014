package com.m1big1.accesslayer.server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * <p>The remote interface that allows RMI service to work. The interface have only one method : <i>treatXml</i><br>
 * This method treatXml treat the message input (in the parameter) and then return a message output.</p>
 * @author      NGUYEN Huu Khuong
 * @category    RMI
 */
public interface ITreatment extends Remote {
    /**
     * <p>Method <b>treatXml</b> have one parameter : the message input. After the treatment in the class implementation,
     * the method returns a message output to be sended to the <b>Business layer</b>.</p>
     * @param messageInput : The message input to be treated.
     * @return <b>messageOutput</b> : The message output
     * @throws RemoteException if the method can not be executed normally because of an error in RMI.
     */
    public String treatXml(String messageInput) throws RemoteException;
}
