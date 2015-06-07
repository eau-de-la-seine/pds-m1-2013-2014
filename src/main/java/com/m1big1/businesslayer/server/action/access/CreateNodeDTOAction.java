package com.m1big1.businesslayer.server.action.access;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.action.Action;
import com.m1big1.businesslayer.server.dto.access.CreateNodeDTO;
import com.m1big1.businesslayer.server.internationalisation.ErrorCode;
import com.m1big1.businesslayer.server.word.WordManager;
import com.m1big1.common.Transmitter;


/**
 * <p>This method treats CreateNodeDTO response from the Access Layer</p>
 * <ul>This class has 3 steps :
 * <ol>Verify if Presentation Layer does not send a wrong parent id</ol>
 * <ol>Verify if a node with the same name and the same type is not already present in the folder</ol>
 * <ol>Create node in the folder</ol>
 * </ul>
 * 
 * @category Visitor Pattern
 * @author Gokan
 */
public class CreateNodeDTOAction extends Action {
    private static Logger logger = Logger.getLogger(CreateNodeDTOAction.class);
    private ClientFactory clientFactory;
    private Transmitter transmitterToAccess;
    private Session session;
    private CreateNodeDTO receivedDto; 
    
    
    /**
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param session                A JMS session
     * @param receivedDto            The received DTO from the Access Layer
     */
    public CreateNodeDTOAction(
        final ClientFactory clientFactory, 
        final Transmitter transmitterToAccess, 
        final Session session, 
        final CreateNodeDTO receivedDto
    ){
        this.clientFactory = clientFactory;
        this.transmitterToAccess = transmitterToAccess;
        this.session = session;
        this.receivedDto = receivedDto;
    }
    
    
    @Override
    public void execute() {
        final Client client = clientFactory.getClient(receivedDto.getSessionId());
        if(client != null) {
            client.addToA2BPool(new Runnable(){
                @Override
                public void run() {
                                        
                    switch(receivedDto.getStep()){
                        case 1:
                            if(receivedDto.isSuccess()){
                                receivedDto.setStep(2);                           
                                sendToAccessLayer(transmitterToAccess, receivedDto);
                            } else {
                                sendErrorMessageToClient(client, session, ErrorCode.CREATE_NODE_STEP1);
                            }
                        
                            break;
                    
                        case 2:
                            if(receivedDto.isSuccess()){
                                receivedDto.setStep(3);
                                sendToAccessLayer(transmitterToAccess, receivedDto);
                            } else {
                                sendErrorMessageToClient(client, session, ErrorCode.CREATE_NODE_STEP2);
                            }
                            
                            break;
                            
                        case 3:
                            try {
                            /* Create file for Word */
                            if(receivedDto.getNodeType() == 2) {
                                WordManager wm = new WordManager(wordLocation + receivedDto.getNodeId());
                                wm.createWordDocument();
                                /* File file = new File(wordLocation + receivedDto.getNodeId());
                                if(!file.createNewFile()){
                                    logger.info("CANNOT CREATE FILE !");
                                } */
                            }
                            
                            // Sending the message to the client
                            com.m1big1.businesslayer.server.dto.presentation.CreateNodeDTO createNodeDto = 
                                new com.m1big1.businesslayer.server.dto.presentation.CreateNodeDTO();
                            
                            createNodeDto.setSessionId(client.getSessionId());
                            createNodeDto.setParentFolderId(receivedDto.getParentFolderId());
                            createNodeDto.setNodeId(receivedDto.getNodeId());
                            createNodeDto.setNodeName(receivedDto.getNodeName());
                            createNodeDto.setNodeType(receivedDto.getNodeType());
                            
                            
                            Transmitter transmitterToClient = new Transmitter(session, client.getDestination());
                            transmitterToClient.send(Transmitter.objectToXml(createNodeDto));
                        } catch (JMSException e) {
                            logger.error("Cannot send to client in Responses class", e);
                        } catch (IOException e) {
                            logger.error("Problem with objecToXml", e);
                        }
                        
                        break;
                        
                    } // End of switch
                } // End of Runnable.run()                
            }); // End of anonymous Runnable
        } // End of if(client != null)        
    }

}
