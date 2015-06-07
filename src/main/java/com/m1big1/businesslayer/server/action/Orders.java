package com.m1big1.businesslayer.server.action;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.dto.access.FindDatabaseIdByLoginPasswordDTO;
import com.m1big1.businesslayer.server.internationalisation.Language;
import com.m1big1.common.Transmitter;


/**
 *<p>A Strategy class</p>
 *<p>This class sends the first step of each orders to the Access Layer</p>
 * @author Gokan EKINCI
 */
@Deprecated
public class Orders { 
    private static Logger logger = Logger.getLogger(Orders.class);
    
    /**
     * <p>If user can connect</p>
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param receivedDto            The received DTO from the Presentation Layer
     */
    public void execute (
        final ClientFactory clientFactory,
        final Transmitter transmitterToAccess, 
        final com.m1big1.businesslayer.server.dto.presentation.ConnectionDTO receivedDto
    ){
        final Client client = clientFactory.newClient();
        client.addToP2BPool(new Runnable(){
            @Override
            public void run(){
                client.setLogin(receivedDto.getLogin());
                client.setLanguage(Language.valueOf(receivedDto.getLanguage()));
                FindDatabaseIdByLoginPasswordDTO dtoMessageToSend = new FindDatabaseIdByLoginPasswordDTO();
                dtoMessageToSend.setSessionId(client.getSessionId());
                dtoMessageToSend.setLogin(receivedDto.getLogin());
                dtoMessageToSend.setPassword(receivedDto.getPassword());  
                
                try {
                    transmitterToAccess.send(Transmitter.objectToXml(dtoMessageToSend));
                } catch (Exception e) {
                    logger.error("Exception when transmitting a ConnectionDTO to AccessLayer", e);
                }
            }
        });
    }
    
    
    /**
     * <p>If user can create node</p>
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param receivedDto            The received DTO from the Presentation Layer
     */
    public void execute (
        final ClientFactory clientFactory,
        final Transmitter transmitterToAccess, 
        final com.m1big1.businesslayer.server.dto.presentation.CreateNodeDTO receivedDto
    ){
        final Client client = clientFactory.getClient(receivedDto.getSessionId());
        if(client != null) {
            client.addToP2BPool(new Runnable(){
                @Override
                public void run(){
                    com.m1big1.businesslayer.server.dto.access.CreateNodeDTO dtoMessageToSend 
                        = new com.m1big1.businesslayer.server.dto.access.CreateNodeDTO();
                    
                    dtoMessageToSend.setStep(1);
                    dtoMessageToSend.setSessionId(client.getSessionId());
                    dtoMessageToSend.setUserDatabaseId(client.getDbId());
                    dtoMessageToSend.setNodeName(receivedDto.getNodeName());
                    dtoMessageToSend.setNodeType(receivedDto.getNodeType());                     
                    dtoMessageToSend.setParentFolderId(receivedDto.getParentFolderId());  
                                                       
                    try {
                        transmitterToAccess.send(Transmitter.objectToXml(dtoMessageToSend));
                    } catch (Exception e) {
                        logger.error("Exception when transmitting a CreateNodeDTO to AccessLayer", e);
                    }
                }
            });
        }
    }
    
    
    
    /**
     * <p>If user can delete node</p>
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param receivedDto            The received DTO from the Presentation Layer
     */
    public void execute (
        final ClientFactory clientFactory,
        final Transmitter transmitterToAccess, 
        final com.m1big1.businesslayer.server.dto.presentation.DeleteNodeDTO receivedDto
    ){
        final Client client = clientFactory.getClient(receivedDto.getSessionId());
        if(client != null) {
            client.addToP2BPool(new Runnable(){
                @Override
                public void run(){
                    com.m1big1.businesslayer.server.dto.access.DeleteNodeDTO dtoMessageToSend 
                        = new com.m1big1.businesslayer.server.dto.access.DeleteNodeDTO();              
                    dtoMessageToSend.setSessionId(client.getSessionId());
                    dtoMessageToSend.setStep(1);
                    dtoMessageToSend.setNodeId(receivedDto.getNodeId());
                    dtoMessageToSend.setUserDatabaseId(client.getDbId()); 
                                   
                    try {
                        transmitterToAccess.send(Transmitter.objectToXml(dtoMessageToSend));
                    } catch (Exception e) {
                        logger.error("Exception when transmitting a DeleteNodeDTO to AccessLayer", e);
                    }
                }
            });
        }
    }
    
    

    /**
     * <p>If user can rename node</p>
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param receivedDto            The received DTO from the Presentation Layer
     */
    public void execute (
        final ClientFactory clientFactory,
        final Transmitter transmitterToAccess, 
        final com.m1big1.businesslayer.server.dto.presentation.RenameNodeDTO receivedDto
    ){
        final Client client = clientFactory.getClient(receivedDto.getSessionId());
        if(client != null) {
            client.addToP2BPool(new Runnable(){
                @Override
                public void run(){
                    com.m1big1.businesslayer.server.dto.access.RenameNodeDTO dtoMessageToSend 
                        = new com.m1big1.businesslayer.server.dto.access.RenameNodeDTO();              
                    dtoMessageToSend.setSessionId(client.getSessionId());
                    dtoMessageToSend.setStep(1);
                    dtoMessageToSend.setNodeId(receivedDto.getNodeId());
                    dtoMessageToSend.setName(receivedDto.getName());
                    dtoMessageToSend.setUserDatabaseId(client.getDbId()); 
                                   
                    try {
                        transmitterToAccess.send(Transmitter.objectToXml(dtoMessageToSend));
                    } catch (Exception e) {
                        logger.error("Exception when transmitting a RenameNodeDTO to AccessLayer", e);
                    }
                }
            });
        }
    }
    
    
    /**
     * <p>if user can logout</p>
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param receivedDto            The received DTO from the Presentation Layer
     */
    public void execute (
        final ClientFactory clientFactory,
        final Transmitter transmitterToAccess, 
        final com.m1big1.businesslayer.server.dto.presentation.LogoutDTO receivedDto
    ){
        final Client client = clientFactory.getClient(receivedDto.getSessionId());
        if(client != null) {
            clientFactory.removeClient(client.getSessionId());
        }
    }
    
    
    /**
     * <p>if user can update a word</p>
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param receivedDto            The received DTO from the Presentation Layer
     */
    public void execute (
        final ClientFactory clientFactory,
        final Transmitter transmitterToAccess,
        final com.m1big1.businesslayer.server.dto.presentation.UpdateWordDTO receivedDto
    ){
        final Client client = clientFactory.getClient(receivedDto.getSessionId());
        if(client != null) {
            client.addToP2BPool(new Runnable(){
                @Override
                public void run(){
                    com.m1big1.businesslayer.server.dto.access.UpdateWordDTO dtoMessageToSend 
                    = new com.m1big1.businesslayer.server.dto.access.UpdateWordDTO();              
                    dtoMessageToSend.setSessionId(client.getSessionId());
                    dtoMessageToSend.setStep(1);
                    dtoMessageToSend.setUserDatabaseId(client.getDbId()); 
                    dtoMessageToSend.setNodeId(receivedDto.getNodeId());
                    dtoMessageToSend.setContent(receivedDto.getContent());
                    
                    try {
                        transmitterToAccess.send(Transmitter.objectToXml(dtoMessageToSend));
                    } catch (Exception e) {
                        logger.error("Exception when transmitting a UpdateWordDTO to AccessLayer", e);
                    }
                }
            });
        }
    }
    
    
    
    
    /**
     * <p>if user can read a word</p>
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param receivedDto            The received DTO from the Presentation Layer
     */
    public void execute (
        final ClientFactory clientFactory,
        final Transmitter transmitterToAccess,
        final com.m1big1.businesslayer.server.dto.presentation.ReadWordDTO receivedDto
    ){
        final Client client = clientFactory.getClient(receivedDto.getSessionId());
        if(client != null) {
            client.addToP2BPool(new Runnable(){
                @Override
                public void run(){
                    com.m1big1.businesslayer.server.dto.access.ReadWordDTO dtoMessageToSend 
                    = new com.m1big1.businesslayer.server.dto.access.ReadWordDTO();              
                    dtoMessageToSend.setSessionId(client.getSessionId());
                    dtoMessageToSend.setStep(1);
                    dtoMessageToSend.setUserDatabaseId(client.getDbId()); 
                    dtoMessageToSend.setNodeId(receivedDto.getNodeId());
                    
                    try {
                        transmitterToAccess.send(Transmitter.objectToXml(dtoMessageToSend));
                    } catch (Exception e) {
                        logger.error("Exception when transmitting a ReadWordDTO to AccessLayer", e);
                    }
                }
            });
        }
    }
       
}
