package com.m1big1.businesslayer.server.action.access;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.action.Action;
import com.m1big1.businesslayer.server.dto.access.ReadWordDTO;
import com.m1big1.businesslayer.server.internationalisation.ErrorCode;
import com.m1big1.businesslayer.server.word.WordManager;
import com.m1big1.common.Transmitter;


/**
 * <p>This method treats ReadWordDTO response from the Access Layer</p>
 * <ul>This class has 2 steps :
 * <ol>Verify if word node to read belongs to user</ol>
 * <ol>Read of the node</ol>
 * </ul>
 * 
 * @category Visitor Pattern
 * @author Gokan EKINCI
 */
public class ReadWordDTOAction extends Action {
    private static Logger logger = Logger.getLogger(ReadWordDTOAction.class);
    private ClientFactory clientFactory;
    private Transmitter transmitterToAccess;
    private Session session;
    private ReadWordDTO receivedDto; 
    
    
    /**
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param session                A JMS session
     * @param receivedDto            The received DTO from the Access Layer
     */
    public ReadWordDTOAction(
        final ClientFactory clientFactory, 
        final Transmitter transmitterToAccess, 
        final Session session, 
        final ReadWordDTO receivedDto
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

}
