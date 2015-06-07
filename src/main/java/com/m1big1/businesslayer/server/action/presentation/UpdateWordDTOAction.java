package com.m1big1.businesslayer.server.action.presentation;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.action.Action;
import com.m1big1.businesslayer.server.dto.presentation.UpdateWordDTO;
import com.m1big1.common.Transmitter;

/** 
 * <p>if user can update a word</p>
 * 
 * @category Visitor Pattern
 * @author Gokan
 */
public class UpdateWordDTOAction extends Action {
    private static Logger logger = Logger.getLogger(UpdateWordDTOAction.class);
    private ClientFactory clientFactory;
    private Transmitter transmitterToAccess;
    private UpdateWordDTO receivedDto; 
    
    
    /**
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param receivedDto            The received DTO from the Presentation Layer
     */
    public UpdateWordDTOAction(
        final ClientFactory clientFactory,
        final Transmitter transmitterToAccess, 
        final com.m1big1.businesslayer.server.dto.presentation.UpdateWordDTO receivedDto
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

}
