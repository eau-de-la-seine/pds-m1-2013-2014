package com.m1big1.presentationlayer.client.connection;

import org.apache.log4j.Logger;

import com.m1big1.presentationlayer.client.dto.ConnectionDTO;
import com.m1big1.presentationlayer.client.swing.LoginFrame;
import com.m1big1.presentationlayer.client.swing.WordManagerFrame;
/**
 * <p>Class which use when ConnectionDTO is in JMS reception </p>
 * @author Samama Oriel
 *
 */
public class ConnectionDTOAction implements Action{
	
	private ConnectionDTO    dto;
	private LoginFrame       loginframe;
	private WordManagerFrame wordManagerFrame;
	private static Logger    logger = Logger.getLogger(ConnectionDTOAction.class);
	/**
	 * Constructor of ConnectionDTOAction
	 * @param dto
	 * @param loginFrame
	 * @param wordManagerFrame
	 * @param lang
	 */
	public ConnectionDTOAction(ConnectionDTO dto , LoginFrame loginFrame , WordManagerFrame wordManagerFrame , String lang){
		this.dto              = dto; 
		this.loginframe       = loginFrame;
		this.wordManagerFrame = wordManagerFrame;

	}
	/**
	 * <p>Action when the ConnectionDTOAction is recept by the listener</p>
	 */
	@Override
	public void execute() {
		// Close loginFrame window
				loginframe.dispose();
				try {
					// Initialise the Word Manager Frame
					wordManagerFrame = new WordManagerFrame(
							loginframe.getTransmitter() ,
							loginframe.getImplementationMessageListener() ,
							dto.getSessionId() , dto.getRootNode() , dto.getLanguage());
					// Show windows
					wordManagerFrame.showWindows();
				} catch (Exception e) {
					logger.fatal("Problem with creation of WordManagerFrame" , e);
				}
		
	}

}
