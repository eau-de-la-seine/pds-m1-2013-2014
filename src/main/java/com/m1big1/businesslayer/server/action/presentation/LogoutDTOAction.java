package com.m1big1.businesslayer.server.action.presentation;

// import org.apache.log4j.Logger;
import com.m1big1.businesslayer.client.Client;
import com.m1big1.businesslayer.client.ClientFactory;
import com.m1big1.businesslayer.server.action.Action;
import com.m1big1.businesslayer.server.dto.presentation.LogoutDTO;
import com.m1big1.common.Transmitter;


/**
 * <p>If user can logout</p>
 * 
 * @category Visitor Pattern
 * @author Gokan EKINCI
 */
public class LogoutDTOAction extends Action {
    // private static Logger logger = Logger.getLogger(LogoutDTOAction.class);
    private ClientFactory clientFactory;
    // private Transmitter transmitterToAccess;
    private LogoutDTO receivedDto; 
    
    
    /**
     * @param clientFactory          Contains clients
     * @param transmitterToAccess    A transmitter object in order to send a Message to the Access Layer
     * @param receivedDto            The received DTO from the Presentation Layer
     */
    public LogoutDTOAction(
        final ClientFactory clientFactory,
        final Transmitter transmitterToAccess, 
        final com.m1big1.businesslayer.server.dto.presentation.LogoutDTO receivedDto
    ){
        this.clientFactory = clientFactory;
        // this.transmitterToAccess = transmitterToAccess;
        this.receivedDto = receivedDto;
    }
    
    
    @Override
    public void execute() {
        final Client client = clientFactory.getClient(receivedDto.getSessionId());
        if(client != null) {
            clientFactory.removeClient(client.getSessionId());
        }        
    }

}
