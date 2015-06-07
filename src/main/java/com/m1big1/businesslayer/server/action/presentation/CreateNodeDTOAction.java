package com.m1big1.businesslayer.server.action.presentation;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.action.Action;
import com.m1big1.businesslayer.server.dto.presentation.CreateNodeDTO;
import com.m1big1.common.Transmitter;

/**
 * <p>If user can create node</p>
 * 
 * @category Visitor Pattern
 * @author Gokan EKINCI
 */
public class CreateNodeDTOAction extends Action {
    private static Logger logger = Logger.getLogger(CreateNodeDTOAction.class);
    private ClientFactory clientFactory;
    private Transmitter transmitterToAccess;
    private CreateNodeDTO receivedDto; 

    
    /**
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param receivedDto            The received DTO from the Presentation Layer
     */
    public CreateNodeDTOAction(
        final ClientFactory clientFactory,
        final Transmitter transmitterToAccess, 
        final com.m1big1.businesslayer.server.dto.presentation.CreateNodeDTO receivedDto
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

}
