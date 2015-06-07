package com.m1big1.businesslayer.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>Factory Pattern</p>
 * <p>This class is a container which represents all the users from the PresentationLayer</p>
 * @category Factory Pattern
 * @author Gokan EKINCI
 */
public class ClientFactory {
    private Map<String, Client> clients = new HashMap<String, Client>();
    
    
    /**
     * <p>Generate a new client in the Factory</p>
     * @return      returns the new generated client
     */
    public Client newClient(){
        Client client = new ClientImplementation();
        clients.put(client.getSessionId(), client);
        return client;
    }
    
    
    /**
     * <p>Get the client by his sessionId</p>
     * @param sessionId     client's id to retrieve
     * @return              returns the client, if the client does not exist, returns null
     */
    public Client getClient(String sessionId){
        return clients.get(sessionId);
    }
    
    /**
     * <p>When the user wants to disconnect</p>
     * @param sessionId    client's id to delete
     */
    public void removeClient(String sessionId){
        for(Iterator<Map.Entry<String, Client>> it = clients.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Client> entry = it.next();
            if(entry.getKey().equals(sessionId)) {
                it.remove();
                break;
            }
        }
    }
    
    
    /**
     * <p>Factory Pattern's internal element</p>
     * <p>This class is useful if you want to create a real Factory Pattern</p>
     * @see FactoryClient.newClient
     * @author Gokan EKINCI
     */
    private class ClientImplementation extends Client {}
}
