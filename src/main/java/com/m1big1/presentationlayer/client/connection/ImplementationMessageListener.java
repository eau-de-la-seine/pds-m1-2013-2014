package com.m1big1.presentationlayer.client.connection;

import java.lang.reflect.Constructor;

import org.apache.log4j.Logger;

import com.m1big1.common.AbstractMessageListener;
import com.m1big1.common.Receiver;
import com.m1big1.presentationlayer.client.dto.DTO;
import com.m1big1.presentationlayer.client.swing.LoginFrame;
import com.m1big1.presentationlayer.client.swing.WordManagerFrame;


public class ImplementationMessageListener extends AbstractMessageListener {
    private LoginFrame       loginFrame;
    private String           lang;
    private WordManagerFrame wordManagerFrame;
    private static Logger    logger = Logger.getLogger(ImplementationMessageListener.class);
/**
 <p>This class reception all DTO's about Received queue</p>
 * @author Oriel SAMAMA
 */
    @Override
    public void layerImplementation(String businessXmlResponse) {
            try {
                
            	logger.info("Message IN \n" + businessXmlResponse +"\n");
            	//Create an object which have an abstract object DTO
            	DTO dto 				  = Receiver.xmlToObject("com.m1big1.presentationlayer.dto" , businessXmlResponse);
            	Class <?> actionClass     = Class.forName("com.m1big1.presentationlayer.connection."+dto.getClass().getSimpleName()+"Action");
            	Constructor <?> construct = actionClass.getConstructor(dto.getClass() , LoginFrame.class , WordManagerFrame.class , String.class);
            	Action action             = (Action) construct.newInstance(dto , loginFrame , wordManagerFrame , lang);
            	action.execute();
            } catch (Exception e) {
            	logger.warn("Problem with creation of WordManagerFrame" , e);
            }

    }
    
    
    
    /**
     * <p>Return language</p> 
     * @param lang
     */
    public void setLang(String lang) {
        this.lang = lang;
    }
    
    
    
    /**
     * </p>Define Login Frame</p>
     * @param loginFrame
     */
    public void setLoginFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
    }
    
    
    
    /**
     * </p>Define WordFrameManager</p>
     * @param wordManagerFrame
     */
    public void setWordManagerFrame(WordManagerFrame wordManagerFrame) {
        this.wordManagerFrame = wordManagerFrame;
    }
}
