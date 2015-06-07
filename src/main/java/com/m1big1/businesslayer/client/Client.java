package com.m1big1.businesslayer.client;

import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.jms.Destination;

import com.m1big1.businesslayer.server.internationalisation.Language;


/**
 *<p>This class represents each users from the Presentation Layer</p>
 *<p>If you want to initiate this class, @see ClientFactory.ClientImplementation </p>
 * @author Gokan EKINCI
 */
public abstract class Client {
    private final String sessionId;
    private int dbId;
    private boolean accepted;
    private Executor poolP2B, poolA2B;
    private String login;
    private Language language;
    private Destination destination;
    
    /**
     * <p>Client's constructor</p>
     */
    public Client(){
        super();
        sessionId = UUID.randomUUID().toString();   
        dbId = 0;
        accepted = false;
        poolP2B = Executors.newFixedThreadPool(1);
        poolA2B = Executors.newFixedThreadPool(1);
    }
    
    /**
     * <p>Add a runnable to P2B thread pool</p>
     * <p>Those runnables represent orders sent by the Presentation Layer</p>
     * @param r     A P2B runnable
     */
    public void addToP2BPool(Runnable r){
        poolP2B.execute(r);
    }
    
    /**
     * <p>Add a runnable to A2B thread pool</p>
     * <p>Those runnables represent responses sent by the Access Layer</p>
     * @param r     A A2B runnable
     */
    public void addToA2BPool(Runnable r){
        poolA2B.execute(r);
    }
    
    /**
     * <p>Get client's session id (it's the correlation id)</p>
     */
    public String getSessionId(){
        return sessionId;
    }
    
    
    /**
     * <p>If client is accepted by Access Layer</p>
     */
    public boolean isAccepted(){
        return accepted;
    }
    
        
    /**
     * <p>Set client's login (it's also his JMS Queue Name)</p>
     * @param login    Client's login
     */
    public void setLogin(String login){
        this.login = login;
    }
    
    
    /**
     * <p>Get client's login</p>
     * @return Client's login
     */
    public String getLogin(){
        return login;
    }
    

    /**
     * <p>Set client's destination</p>
     * @param destination    Client's JMS queue destination
     */
    public void setDestination(Destination destination){
        this.destination = destination;
    }
    
    
    /**
     * <p>Get client's destination</p>
     * @return Client's JMS queue destination
     */
    public Destination getDestination(){
        return destination;
    }
    
    
    /**
     * <p>Set client's database user id</p>
     * @param dbId    User's database id
     */
    public void setDbId(int dbId){
        this.dbId = dbId;
    }
    
    
    /**
     * <p>Get client's database user id</p>
     * @return Client's database user id
     */
    public int getDbId(){
        return dbId;
    }

    
    /**
     * <p>Get client's language</p>
     * @return Client's language
     */
    public Language getLanguage() {
        return language;
    }

    
    /**
     * <p>Set client's language</p>
     * @param language Client's language
     */
    public void setLanguage(Language language) {
        this.language = language;
    }      
}
