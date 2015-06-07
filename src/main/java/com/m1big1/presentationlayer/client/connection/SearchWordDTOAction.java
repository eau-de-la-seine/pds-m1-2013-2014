package com.m1big1.presentationlayer.client.connection;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.m1big1.presentationlayer.client.dto.SearchWordDTO;
import com.m1big1.presentationlayer.client.swing.LoginFrame;
import com.m1big1.presentationlayer.client.swing.ResultSearchFrame;
import com.m1big1.presentationlayer.client.swing.WordManagerFrame;
/**
 * <p>Class which use when SearchWordDTOAction is in JMS reception </p>
 * @author Samama Oriel
 *
 */
public class SearchWordDTOAction implements Action{
	
	private SearchWordDTO    dto;
	private String           lang;
	private WordManagerFrame wordManagerFrame;
	private static Logger    logger = Logger.getLogger(SearchWordDTOAction.class);

	/**
	 * <p>Constructor of SearchWordDTOAction</p>
	 * @param dto
	 * @param loginFrame
	 * @param wordManagerFrame
	 * @param lang
	 */
	public SearchWordDTOAction(SearchWordDTO dto , LoginFrame loginFrame , WordManagerFrame wordManagerFrame , String lang){
		this.dto              = dto;
		this.lang             = lang;
		this.wordManagerFrame = wordManagerFrame;
	}

	/**
	 * <p><p>Action when the SearchWordDTOAction is recept by the listener</p>
	 */
	@Override
	public void execute() {
		try {
			Properties properties	   = new Properties();
			ClassLoader classLoader    = Thread.currentThread().getContextClassLoader();
			InputStream propertiesFile = classLoader.getResourceAsStream("languages/language_" + lang + ".properties");
			properties.load(propertiesFile);
			ResultSearchFrame rsf      = new ResultSearchFrame(properties.getProperty("view_search_result") , dto.getRootNode() , dto.getSessionId() , dto.getSearchName() , wordManagerFrame.getTransmitter() , lang);
			rsf.showWindows();
			propertiesFile.close();
		} catch (Exception e) {
			logger.fatal("Problem to load languages" , e);
		}
		
	}

}
