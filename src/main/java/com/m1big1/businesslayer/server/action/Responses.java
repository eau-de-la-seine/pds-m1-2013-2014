package com.m1big1.businesslayer.server.action;

import java.io.File;
import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.dto.access.FindDatabaseIdByLoginPasswordDTO;
import com.m1big1.businesslayer.server.dto.access.FindUserNodeListByDatabaseIdDTO;
import com.m1big1.businesslayer.server.internationalisation.ErrorCode;
import com.m1big1.businesslayer.server.internationalisation.ErrorFactory;
import com.m1big1.businesslayer.server.word.WordManager;
import com.m1big1.common.Transmitter;

/**
 *<p>A Strategy class.</p>
 *<p>This class receives the responses from the Access Layer. 
 * Please read the documentation for more information.</p>
 * @author Gokan EKINCI
 */
@Deprecated
public class Responses {
    private static Logger logger = Logger.getLogger(Responses.class);
    private static String wordLocation;
    private static ErrorFactory errorFactory = new ErrorFactory();
    
    // LOADING PROPERTIES FILE
    static {
        try {
            Context context = new InitialContext();
            wordLocation = (String) context.lookup("java:comp/env/word_file_location");
        } catch (NamingException e) {
            logger.fatal("Problem with loading word_file_location", e);
        }
    }
    
    /**
     * <p>This method treats FindDatabaseIdByLoginPasswordDTO response from the Access Layer</p>
     * <p>This DTO permits to know if the user is recognized by the Access Layer.
     * If the Access Layer sends 0, Login/Password is wrong, otherwise it's ok.</p>
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param session                A JMS session
     * @param receivedDto            The received DTO from the Access Layer
     */
    public void execute(
        final ClientFactory clientFactory, 
        final Transmitter transmitterToAccess, 
        final Session session, 
        final FindDatabaseIdByLoginPasswordDTO receivedDto
    ){
        final Client client = clientFactory.getClient(receivedDto.getSessionId());
        if(client != null) {
            client.addToA2BPool(new Runnable(){
                @Override
                public void run(){
                    try {
                        
                        client.setDestination(session.createQueue(client.getLogin()));
                        
                        // Step 1 is a success, now send step n°2 (FindUserNodeListByDatabaseIdDTO)
                        if(receivedDto.getUserDatabaseId() != 0){
                            client.setDbId(receivedDto.getUserDatabaseId());
                            FindUserNodeListByDatabaseIdDTO fdto = new FindUserNodeListByDatabaseIdDTO();
                            fdto.setSessionId(client.getSessionId());
                            fdto.setUserDatabaseId(client.getDbId());
                            
                            String xml = Transmitter.objectToXml(fdto);
                            transmitterToAccess.send(xml);
                            logger.info("SENT : " + xml);
                        } 
                        
                        // else step 1 has failed
                        else { 
                            sendErrorMessageToClient(client, session, ErrorCode.CONNECTION);
                        }
                    } catch (JMSException e) {
                        logger.error("Cannot \"createQueue()\" for user in Responses class", e);
                    } catch (IOException e) {
                        logger.error("Problem with objecToXml", e);
                    }
                    
                } // End of Runnable.run()
            }); // End of anonymous Runnable
        } // End of if(client != null) 
    }
    
    
    /**
     * <p>This method treats FindUserNodeListByDatabaseIdDTO response from the Access Layer</p>
     * <p>This DTO reconstructs user's node list.</p>
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param session                A JMS session
     * @param receivedDto            The received DTO from the Access Layer
     */
    public void execute(
        final ClientFactory clientFactory, 
        final Transmitter transmitterToAccess, 
        final Session session, 
        final FindUserNodeListByDatabaseIdDTO receivedDto
    ){
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
    
    
    /**
     * <p>This method treats CreateNodeDTO response from the Access Layer</p>
     * <ul>This class has 3 steps :
     * <ol>Verify if Presentation Layer does not send a wrong parent id</ol>
     * <ol>Verify if a node with the same name and the same type is not already present in the folder</ol>
     * <ol>Create node in the folder</ol>
     * </ul>
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param session                A JMS session
     * @param receivedDto            The received DTO from the Access Layer
     */
    public void execute(
        final ClientFactory clientFactory, 
        final Transmitter transmitterToAccess, 
        final Session session, 
        final com.m1big1.businesslayer.server.dto.access.CreateNodeDTO receivedDto
    ){
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
                                File file = new File(wordLocation + receivedDto.getNodeId());
                                if(!file.createNewFile()){
                                    logger.info("CANNOT CREATE FILE !");
                                }
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
    
    
    
    /**
     * <p>This method treats DeleteNodeDTO response from the Access Layer</p>
     * <ul>This class has 2 steps :
     * <ol>Verify if node to delete belongs to user</ol>
     * <ol>Suppress the node (and its children if it's a folder)</ol>
     * </ul>
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param session                A JMS session
     * @param receivedDto            The received DTO from the Access Layer
     */
    public void execute(
        final ClientFactory clientFactory, 
        final Transmitter transmitterToAccess, 
        final Session session, 
        final com.m1big1.businesslayer.server.dto.access.DeleteNodeDTO receivedDto
    ){
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
    
    
    
    
    /**
     * <p>This method treats RenameNodeDTO response from the Access Layer</p>
     * <ul>This class has 2 steps :
     * <ol>Verify if node to rename belongs to user</ol>
     * <ol>Renaming of the node</ol>
     * </ul>
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param session                A JMS session
     * @param receivedDto            The received DTO from the Access Layer
     */
    public void execute(
        final ClientFactory clientFactory, 
        final Transmitter transmitterToAccess, 
        final Session session, 
        final com.m1big1.businesslayer.server.dto.access.RenameNodeDTO receivedDto
    ){
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
                                sendErrorMessageToClient(client, session, ErrorCode.RENAME_NODE);
                            }
                            
                            break;
                        
                        case 2 :
                            // Sending the message to the client
                            com.m1big1.businesslayer.server.dto.presentation.RenameNodeDTO renameNodeDTO = 
                                new com.m1big1.businesslayer.server.dto.presentation.RenameNodeDTO();
                            
                            renameNodeDTO.setSessionId(client.getSessionId());
                            renameNodeDTO.setNodeId(receivedDto.getNodeId());
                            renameNodeDTO.setName(receivedDto.getName());
                            
                            try {
                                Transmitter transmitterToClient = new Transmitter(session, client.getDestination());
                                transmitterToClient.send(Transmitter.objectToXml(renameNodeDTO));
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
    
    
        
    /**
     * <p>This method treats UpdateWordDTO response from the Access Layer</p>
     * <ul>This class has 2 steps :
     * <ol>Verify if word node to update belongs to user</ol>
     * <ol>Updating of the node</ol>
     * </ul>
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param session                A JMS session
     * @param receivedDto            The received DTO from the Access Layer
     */
    public void execute(
        final ClientFactory clientFactory, 
        final Transmitter transmitterToAccess, 
        final Session session, 
        final com.m1big1.businesslayer.server.dto.access.UpdateWordDTO receivedDto
    ){
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
                            sendErrorMessageToClient(client, session, ErrorCode.UPDATE_WORD);
                        }
                        
                        break;
                    case 2:
                        // Sending the message to the client
                        com.m1big1.businesslayer.server.dto.presentation.UpdateWordDTO updateWordDTO = 
                            new com.m1big1.businesslayer.server.dto.presentation.UpdateWordDTO();
                        
                        updateWordDTO.setSessionId(client.getSessionId());
                        updateWordDTO.setNodeId(receivedDto.getNodeId());
                        updateWordDTO.setContent(receivedDto.getContent());            
                        
                        try {        
                            WordManager wordManager = new WordManager(wordLocation + receivedDto.getNodeId());
                            wordManager.write(receivedDto.getContent());
                            Transmitter transmitterToClient = new Transmitter(session, client.getDestination());
                            transmitterToClient.send(Transmitter.objectToXml(updateWordDTO));
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
    
    
   
    /**
     * <p>This method treats ReadWordDTO response from the Access Layer</p>
     * <ul>This class has 2 steps :
     * <ol>Verify if word node to read belongs to user</ol>
     * <ol>Read of the node</ol>
     * </ul>
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param session                A JMS session
     * @param receivedDto            The received DTO from the Access Layer
     */
    public void execute(
        final ClientFactory clientFactory, 
        final Transmitter transmitterToAccess, 
        final Session session, 
        final com.m1big1.businesslayer.server.dto.access.ReadWordDTO receivedDto
    ){
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
                                sendErrorMessageToClient(client, session, ErrorCode.READ_WORD);
                            }
                            
                            break;
                        case 2:
                            // Sending the message to the client
                            com.m1big1.businesslayer.server.dto.presentation.ReadWordDTO readWordDTO = 
                                new com.m1big1.businesslayer.server.dto.presentation.ReadWordDTO();
                            
                            readWordDTO.setSessionId(client.getSessionId());
                            readWordDTO.setNodeId(receivedDto.getNodeId());
                            
                            try {                         
                                WordManager wordManager = new WordManager(wordLocation + receivedDto.getNodeId());
                                readWordDTO.setContent(wordManager.read()); 
                                Transmitter transmitterToClient = new Transmitter(session, client.getDestination());
                                transmitterToClient.send(Transmitter.objectToXml(readWordDTO));
                            } catch (JMSException e) {
                                logger.error("Cannot create Queue in Responses class", e);
                            } catch (IOException e) {
                                logger.error("Problem with objecToXml OR when reading from WordManager", e);
                            }    
                            
                            break;
                    } // End of switch
                } // End of Runnable.run()
            }); // End of Runnable.run()
        } // if(client != null)
    }
  
        
    /**
     * <p>Sending the message to the Access Layer</p>
     * @param transmitterToAccess
     * @param receivedDto
     */
    private <T> void sendToAccessLayer(Transmitter transmitterToAccess, T receivedDto){
        try {
            transmitterToAccess.send(Transmitter.objectToXml(receivedDto));
        } catch (JMSException e) {
            logger.error("Cannot send message to Access Layer in Responses class", e);
        } catch (IOException e) {
            logger.error("Problem with objecToXml", e);
        }
    }
    
    
    /**
     * <p>Send error message to client</p>
     * @param title
     * @param message
     */
    private void sendErrorMessageToClient(Client client, Session session, ErrorCode errorCode){
        
        // Sending the message to the client
        try {
            Transmitter transmitterToClient = new Transmitter(session, client.getDestination());
            transmitterToClient.send(Transmitter.objectToXml(errorFactory.getErrorDTO(client.getLanguage(), errorCode)));
        } catch (JMSException e) {
            logger.error("Cannot create Queue in Responses class", e);
        } catch (IOException e) {
            logger.error("Problem with objecToXml", e);
        }
    }       
}

