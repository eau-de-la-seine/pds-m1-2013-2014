package com.m1big1.businesslayer.server.action.access;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.action.Action;
import com.m1big1.businesslayer.server.dto.access.DeleteNodeDTO;
import com.m1big1.businesslayer.server.internationalisation.ErrorCode;
import com.m1big1.common.Transmitter;


/**
 * <p>This method treats DeleteNodeDTO response from the Access Layer</p>
 * <ul>This class has 2 steps :
 *   <ol>Verify if node to delete belongs to user</ol>
 *   <ol>Suppress the node (and its children if it's a folder)</ol>
 * </ul>
 * 
 * @category Visitor Pattern
 * @author Gokan EKINCI
 */
public class DeleteNodeDTOAction extends Action {
    private static Logger logger = Logger.getLogger(DeleteNodeDTOAction.class);
    private ClientFactory clientFactory;
    private Transmitter transmitterToAccess;
    private Session session;
    private DeleteNodeDTO receivedDto; 
    
    
    /**
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param session                A JMS session
     * @param receivedDto            The received DTO from the Access Layer
     */
    public DeleteNodeDTOAction(
        final ClientFactory clientFactory, 
        final Transmitter transmitterToAccess, 
        final Session session, 
        final DeleteNodeDTO receivedDto
    ){
        this.clientFactory = clientFactory;
        this.transmitterToAccess = transmitterToAccess;
        this.session = session;
        this.receivedDto = receivedDto;
    }
    
    
    @Override
    public void execute() {
        final Client client = clientFactory.getClient(receivedDto.getSessionId());
        if(client != null){
        
        client.addToA2BPool(new Runnable(){
            @Override
            public void run() {
                switch(receivedDto.getStep()){
                    case 1 :
                        if(receivedDto.isSuccess()){
                            receivedDto.setStep(2);
                            sendToAccessLayer(transmitterToAccess, receivedDto);
                        } else {
                            sendErrorMessageToClient(client, session, ErrorCode.DELETE_NODE);
                        }
                        
                        break;
                        
                    case 2 :
                        // Sending the message to the client
                        com.m1big1.businesslayer.server.dto.presentation.DeleteNodeDTO deleteNodeDTO = 
                            new com.m1big1.businesslayer.server.dto.presentation.DeleteNodeDTO();
                        
                        deleteNodeDTO.setSessionId(client.getSessionId());
                        deleteNodeDTO.setNodeId(receivedDto.getNodeId());
                        
                        try {
                            Transmitter transmitterToClient = new Transmitter(session, client.getDestination());
                            transmitterToClient.send(Transmitter.objectToXml(deleteNodeDTO));
                        } catch (JMSException e) {
                            logger.error("Cannot send to client in Responses class", e);
                        } catch (IOException e) {
                            logger.error("Problem with objecToXml", e);
                        }
                        
                        break;
                    } // End of switch
                } // End of Runnable.run()                
            }); // End of anonymous Runnable
        } // if(client != null)
    }

}
