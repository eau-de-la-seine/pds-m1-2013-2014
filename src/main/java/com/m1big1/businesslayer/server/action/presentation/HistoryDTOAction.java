package com.m1big1.businesslayer.server.action.presentation;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.action.Action;
import com.m1big1.businesslayer.server.dto.presentation.HistoryDTO;
import com.m1big1.common.Transmitter;

/**
 * <p>Ask history</p>
 * 
 * @category Visitor Pattern
 * @author Gokan EKINCI
 */
public class HistoryDTOAction extends Action {
    private static Logger logger = Logger.getLogger(HistoryDTOAction.class);
    private ClientFactory clientFactory;
    private Transmitter transmitterToAccess;
    private HistoryDTO receivedDto; 
    
    /**
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param receivedDto            The received DTO from the Presentation Layer
     */
    public HistoryDTOAction(
        final ClientFactory clientFactory,
        final Transmitter transmitterToAccess, 
        final com.m1big1.businesslayer.server.dto.presentation.HistoryDTO receivedDto
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
                    com.m1big1.businesslayer.server.dto.access.HistoryDTO dtoMessageToSend 
                        = new com.m1big1.businesslayer.server.dto.access.HistoryDTO();              
                    dtoMessageToSend.setSessionId(client.getSessionId());
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
