package com.m1big1.businesslayer.server.action.access;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.action.Action;
import com.m1big1.businesslayer.server.dto.access.HistoryDTO;
import com.m1big1.common.Transmitter;

/**
 * <p>This method treats RenameNodeDTO response from the Access Layer</p>
 * <ul>This class has only 1 step :
 *   <ol>Get all word documents which correspond to the searched document</ol>
 * </ul>
 * 
 * @category Visitor Pattern
 * @author Gokan EKINCI
 */
public class HistoryDTOAction extends Action {
    private static Logger logger = Logger.getLogger(HistoryDTOAction.class);
    private ClientFactory clientFactory;
    // private Transmitter transmitterToAccess;
    private Session session;
    private HistoryDTO receivedDto; 
    
    
    /**
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param session                A JMS session
     * @param receivedDto            The received DTO from the Access Layer
     */
    public HistoryDTOAction(
        final ClientFactory clientFactory, 
        final Transmitter transmitterToAccess, 
        final Session session, 
        final HistoryDTO receivedDto
    ){
        this.clientFactory = clientFactory;
        // this.transmitterToAccess = transmitterToAccess;
        this.session = session;
        this.receivedDto = receivedDto;
    }
    
    
    @Override
    public void execute() {
        final Client client = clientFactory.getClient(receivedDto.getSessionId());
        if(client != null) {
            client.addToA2BPool(new Runnable(){
                @Override
                public void run(){
                    try {
                        
                        // Initializing the Client's HistoryDTO
                        com.m1big1.businesslayer.server.dto.presentation.HistoryDTO conDto = 
                                new com.m1big1.businesslayer.server.dto.presentation.HistoryDTO();
                        conDto.setSessionId(client.getSessionId());
                        
                        
                        // Initializing the Client's HistoryEventDTO
                        com.m1big1.businesslayer.server.dto.presentation.HistoryEventDTO root = null;
                        com.m1big1.businesslayer.server.dto.presentation.HistoryEventDTO presentationNode = null;

                        for(com.m1big1.businesslayer.server.dto.access.HistoryEventDTO accessNode = receivedDto.getRootHistoryEvent();
                            accessNode != null;
                            accessNode = accessNode.getNextHistoryEvent()
                        ){
                            com.m1big1.businesslayer.server.dto.presentation.HistoryEventDTO nextPresentationHistoryEvent =
                                new com.m1big1.businesslayer.server.dto.presentation.HistoryEventDTO();
                            
                            nextPresentationHistoryEvent.setCompletePath(accessNode.getCompletePath());
                            nextPresentationHistoryEvent.setEventDate(accessNode.getEventDate());
                            nextPresentationHistoryEvent.setEventKind(accessNode.getEventKind());
                            
                            if(root == null){
                                root = nextPresentationHistoryEvent;
                                presentationNode = nextPresentationHistoryEvent;
                            } else {
                                presentationNode.setNextHistoryEvent(nextPresentationHistoryEvent);
                                presentationNode = nextPresentationHistoryEvent;
                            }
                        }
                        
                        conDto.setRootHistoryEvent(root);
                        
                        // Sending the message to the client
                        Transmitter transmitterToClient = new Transmitter(session, client.getDestination());
                        String xml = Transmitter.objectToXml(conDto);
                        transmitterToClient.send(xml);
                        logger.info("SENT : " + xml);

                    } catch (JMSException e) {
                        logger.error("Cannot send message with transmitterToClient", e);
                    } catch (IOException e) {
                        logger.error("Problem with objecToXml", e);
                    }
                } // End of Runnable.run()
            }); // End of anonymous Runnable
        } // End of if(client != null)          
    }

}
