package com.m1big1.businesslayer.server.action.presentation;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.action.Action;
import com.m1big1.businesslayer.server.dto.presentation.DeleteNodeDTO;
import com.m1big1.common.Transmitter;

/**
 * <p>If user can delete node</p>
 * 
 * @category Visitor Pattern     
 * @author Gokan EKINCI
 */
public class DeleteNodeDTOAction extends Action {
    private static Logger logger = Logger.getLogger(DeleteNodeDTOAction.class);
    private ClientFactory clientFactory;
    private Transmitter transmitterToAccess;
    private DeleteNodeDTO receivedDto; 
    
    
    /**
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param receivedDto            The received DTO from the Presentation Layer
     */
    public DeleteNodeDTOAction(
        final ClientFactory clientFactory,
        final Transmitter transmitterToAccess, 
        final com.m1big1.businesslayer.server.dto.presentation.DeleteNodeDTO receivedDto
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

}
