package com.m1big1.businesslayer.server.action.presentation;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.action.Action;
import com.m1big1.businesslayer.server.dto.presentation.SearchWordDTO;
import com.m1big1.common.Transmitter;

/**
 * <p>Ask for a list of words which correspond to a searched document</p>
 * 
 * @category Visitor Pattern
 * @author Gokan EKINCI
 */
public class SearchWordDTOAction extends Action {
    private static Logger logger = Logger.getLogger(SearchWordDTOAction.class);
    private ClientFactory clientFactory;
    private Transmitter transmitterToAccess;
    private SearchWordDTO receivedDto; 
    
    /**
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param receivedDto            The received DTO from the Presentation Layer
     */
    public SearchWordDTOAction(
        final ClientFactory clientFactory,
        final Transmitter transmitterToAccess, 
        final com.m1big1.businesslayer.server.dto.presentation.SearchWordDTO receivedDto
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
                    com.m1big1.businesslayer.server.dto.access.SearchWordDTO dtoMessageToSend 
                        = new com.m1big1.businesslayer.server.dto.access.SearchWordDTO();              
                    dtoMessageToSend.setSessionId(client.getSessionId());
                    dtoMessageToSend.setUserDatabaseId(client.getDbId()); 
                    dtoMessageToSend.setSearchName(receivedDto.getSearchName());
                                   
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
