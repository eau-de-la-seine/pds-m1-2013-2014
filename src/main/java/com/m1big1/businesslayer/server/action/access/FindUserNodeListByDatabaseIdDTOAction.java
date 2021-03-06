package com.m1big1.businesslayer.server.action.access;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.action.Action;
import com.m1big1.businesslayer.server.dto.access.FindUserNodeListByDatabaseIdDTO;
import com.m1big1.common.Transmitter;


/**
 * <p>This method treats FindUserNodeListByDatabaseIdDTO response from the Access Layer</p>
 * <p>This DTO reconstructs user's node list.</p>
 * 
 * @category Visitor Pattern
 * @author Gokan
 */
public class FindUserNodeListByDatabaseIdDTOAction extends Action {
    private static Logger logger = Logger.getLogger(FindUserNodeListByDatabaseIdDTOAction.class);
    private ClientFactory clientFactory;
    // private Transmitter transmitterToAccess;
    private Session session;
    private FindUserNodeListByDatabaseIdDTO receivedDto; 
    
    /**
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param session                A JMS session
     * @param receivedDto            The received DTO from the Access Layer
     */
    public FindUserNodeListByDatabaseIdDTOAction(
        final ClientFactory clientFactory, 
        final Transmitter transmitterToAccess, 
        final Session session, 
        final FindUserNodeListByDatabaseIdDTO receivedDto
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
                        
                        // Initializing the Client's ConnectionDTO
                        com.m1big1.businesslayer.server.dto.presentation.ConnectionDTO conDto = 
                                new com.m1big1.businesslayer.server.dto.presentation.ConnectionDTO();
                        conDto.setSessionId(client.getSessionId());
                        conDto.setLanguage(client.getLanguage().toString());
                        
                        
                        // Initializing the Client's NodeStructureDTO
                        com.m1big1.businesslayer.server.dto.presentation.NodeStructureDTO root = null;
                        com.m1big1.businesslayer.server.dto.presentation.NodeStructureDTO presentationNode = null;

                        for(com.m1big1.businesslayer.server.dto.access.NodeStructureDTO accessNode = receivedDto.getRootNode();
                            accessNode != null;
                            accessNode = accessNode.getNextNode()
                        ){
                            com.m1big1.businesslayer.server.dto.presentation.NodeStructureDTO nextPresentationNode =
                                new com.m1big1.businesslayer.server.dto.presentation.NodeStructureDTO();
                            nextPresentationNode.setParentId(accessNode.getParentId());
                            nextPresentationNode.setId(accessNode.getId());
                            nextPresentationNode.setName(accessNode.getName());
                            nextPresentationNode.setContent(accessNode.getContent());
                            nextPresentationNode.setType(accessNode.getType());
                            
                            if(root == null){
                                root = nextPresentationNode;
                                presentationNode = nextPresentationNode;
                            } else {
                                presentationNode.setNextNode(nextPresentationNode);
                                presentationNode = nextPresentationNode;
                            }
                        }
                        
                        conDto.setRootNode(root);
                        
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
