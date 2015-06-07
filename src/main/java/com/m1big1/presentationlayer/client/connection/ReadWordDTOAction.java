package com.m1big1.presentationlayer.client.connection;


import com.m1big1.presentationlayer.client.dto.ReadWordDTO;
import com.m1big1.presentationlayer.client.swing.LoginFrame;
import com.m1big1.presentationlayer.client.swing.WordManagerFrame;
/**
 * <p>Class which use when ReadWordDTOAction is in JMS reception </p>
 * @author Samama Oriel
 *
 */
public class ReadWordDTOAction implements Action{
	
	private ReadWordDTO      dto;
	private WordManagerFrame wordManagerFrame;

	/**
	 * <p>Constructor of ReadWordDTOAction</p>
	 * @param dto
	 * @param loginFrame
	 * @param wordManagerFrame
	 * @param lang
	 */
	public ReadWordDTOAction(ReadWordDTO dto , LoginFrame loginFrame , WordManagerFrame wordManagerFrame , String lang){
		this.dto              = dto;
		this.wordManagerFrame = wordManagerFrame;
	}
	
	
	
	/**
	 * <p>Action when the ReadWordDTOAction is recept by the listener</p>
	 */

	@Override
	public void execute() {
		wordManagerFrame.setNodeWord(dto.getNodeId() , dto.getContent());
		
	}

}
