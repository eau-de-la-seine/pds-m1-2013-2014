package com.m1big1.businesslayer.server.action.presentation;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.action.Action;
import com.m1big1.businesslayer.server.dto.presentation.RenameNodeDTO;
import com.m1big1.common.Transmitter;


/**
 * <p>If user can rename node</p>
 * 
 * @category Visitor Pattern
 * @author Gokan EKINCI
 */
public class RenameNodeDTOAction extends Action {
    private static Logger logger = Logger.getLogger(RenameNodeDTOAction.class);
    private ClientFactory clientFactory;
    private Transmitter transmitterToAccess;
    private RenameNodeDTO receivedDto; 
    
    
    /**
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param receivedDto            The received DTO from the Presentation Layer
     */
    public RenameNodeDTOAction(
        final ClientFactory clientFactory,
        final Transmitter transmitterToAccess, 
        final com.m1big1.businesslayer.server.dto.presentation.RenameNodeDTO receivedDto
    ){
        this.clientFactory = clientFactory;
        this.transmitterToAccess = transmitterToAccess;
        this.receivedDto = receivedDto;
    }
    
    
    @Override
    public void execute() {
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

}
