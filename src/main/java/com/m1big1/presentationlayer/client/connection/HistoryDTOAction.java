package com.m1big1.presentationlayer.client.connection;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.m1big1.presentationlayer.client.dto.HistoryDTO;
import com.m1big1.presentationlayer.client.swing.HistoryFrame;
import com.m1big1.presentationlayer.client.swing.LoginFrame;
import com.m1big1.presentationlayer.client.swing.WordManagerFrame;
/**
 * <p>Class which use when HistoryDTOAction is in JMS reception </p>
 * @author SAMAMAO
 *
 */
public class HistoryDTOAction implements Action {
	
	private        HistoryDTO    dto;
	private        String        lang;
	private static Logger        logger = Logger.getLogger(HistoryDTOAction.class);
	/**
	 * <p>Constructor of HistoryDTOAction</p>
	 * @param dto
	 * @param loginFrame
	 * @param wordManagerFrame
	 * @param lang
	 */
	public HistoryDTOAction(HistoryDTO dto , LoginFrame loginFrame , WordManagerFrame wordManagerFrame , String lang){
		this.dto  = dto;
		this.lang = lang;
	}
	/**
	 * <p>Action when the HistoryDTOAction is recept by the listener</p>
	 */
	@Override
	public void execute() {
		try {
			Properties properties      = new Properties();
	        ClassLoader classLoader    = Thread.currentThread().getContextClassLoader();
	        InputStream propertiesFile = classLoader.getResourceAsStream("languages/language_"+ lang +".properties");
	        properties.load(propertiesFile);
			HistoryFrame hist 		   = new HistoryFrame(properties.getProperty("view_history") , dto.getRootHistoryEvent() , lang);
			hist.showWindows();
			properties.load(propertiesFile);
			propertiesFile.close();
		} catch (Exception e) {
			logger.fatal("Problem to load languages" , e);
		}
		
	}

}
