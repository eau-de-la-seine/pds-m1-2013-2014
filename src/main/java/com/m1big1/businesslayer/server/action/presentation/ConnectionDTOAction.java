package com.m1big1.businesslayer.server.action.presentation;

import org.apache.log4j.Logger;

import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.action.Action;
import com.m1big1.businesslayer.server.dto.access.FindDatabaseIdByLoginPasswordDTO;
import com.m1big1.businesslayer.server.dto.presentation.ConnectionDTO;
import com.m1big1.businesslayer.server.internationalisation.Language;
import com.m1big1.common.Transmitter;

/** 
 * <p>If user can connect</p>
 * 
 * @category Visitor Pattern
 * @author Gokan EKINCI
 */
public class ConnectionDTOAction extends Action {
    private static Logger logger = Logger.getLogger(ConnectionDTOAction.class);
    private ClientFactory clientFactory;
    private Transmitter transmitterToAccess;
    private ConnectionDTO receivedDto; 
    
    /**
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param receivedDto            The received DTO from the Presentation Layer
     */
    public ConnectionDTOAction(
        final ClientFactory clientFactory,
        final Transmitter transmitterToAccess, 
        final com.m1big1.businesslayer.server.dto.presentation.ConnectionDTO receivedDto
    ){
        this.clientFactory = clientFactory;
        this.transmitterToAccess = transmitterToAccess;
        this.receivedDto = receivedDto;
    }

    
    @Override
    public void execute() {
        final Client client = clientFactory.newClient();
        client.addToP2BPool(new Runnable(){
            @Override
            public void run(){
                client.setLogin(receivedDto.getLogin());
                client.setLanguage(Language.valueOf(receivedDto.getLanguage()));
                FindDatabaseIdByLoginPasswordDTO dtoMessageToSend = new FindDatabaseIdByLoginPasswordDTO();
                dtoMessageToSend.setSessionId(client.getSessionId());
                dtoMessageToSend.setLogin(receivedDto.getLogin());
                dtoMessageToSend.setPassword(receivedDto.getPassword());  
                
                try {
                    transmitterToAccess.send(Transmitter.objectToXml(dtoMessageToSend));
                } catch (Exception e) {
                    logger.error("Exception when transmitting a ConnectionDTO to AccessLayer", e);
                }
            }
        });
    }

}
