package com.m1big1.businesslayer.server.action.presentation;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.action.Action;
import com.m1big1.businesslayer.server.dto.presentation.ReadWordDTO;
import com.m1big1.common.Transmitter;

/**
 * <p>If user can read a word</p>
 * 
 * @category Visitor Pattern
 * @author Gokan EKINCI
 */
public class ReadWordDTOAction extends Action {
    private static Logger logger = Logger.getLogger(ReadWordDTOAction.class);
    private ClientFactory clientFactory;
    private Transmitter transmitterToAccess;
    private ReadWordDTO receivedDto; 

    
    /**
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param receivedDto            The received DTO from the Presentation Layer
     */
    public ReadWordDTOAction(
        final ClientFactory clientFactory,
        final Transmitter transmitterToAccess, 
        final com.m1big1.businesslayer.server.dto.presentation.ReadWordDTO receivedDto
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
