package com.m1big1.businesslayer.server.action.access;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.action.Action;
import com.m1big1.businesslayer.server.dto.access.FindDatabaseIdByLoginPasswordDTO;
import com.m1big1.businesslayer.server.dto.access.FindUserNodeListByDatabaseIdDTO;
import com.m1big1.businesslayer.server.internationalisation.ErrorCode;
import com.m1big1.common.Transmitter;


/**
 * <p>This constructor treats FindDatabaseIdByLoginPasswordDTO response from the Access Layer</p>
 * <p>This DTO permits to know if the user is recognized by the Access Layer.
 * If the Access Layer sends 0, Login/Password is wrong, otherwise it's ok.</p>
 * 
 * @category Visitor Pattern
 * @author Gokan EKINCI
 */
public class FindDatabaseIdByLoginPasswordDTOAction extends Action {
    private static Logger logger = Logger.getLogger(FindDatabaseIdByLoginPasswordDTOAction.class);
    private ClientFactory clientFactory;
    private Transmitter transmitterToAccess;
    private Session session;
    private FindDatabaseIdByLoginPasswordDTO receivedDto; 
    
    /**
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param session                A JMS session
     * @param receivedDto            The received DTO from the Access Layer
     */
    public FindDatabaseIdByLoginPasswordDTOAction(
        final ClientFactory clientFactory, 
        final Transmitter transmitterToAccess, 
        final Session session, 
        final FindDatabaseIdByLoginPasswordDTO receivedDto
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

}
