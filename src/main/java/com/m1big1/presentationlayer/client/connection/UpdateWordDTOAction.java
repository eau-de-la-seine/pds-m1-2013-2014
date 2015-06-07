package com.m1big1.presentationlayer.client.connection;

import com.m1big1.presentationlayer.client.dto.UpdateWordDTO;
import com.m1big1.presentationlayer.client.swing.LoginFrame;
import com.m1big1.presentationlayer.client.swing.WordManagerFrame;
/**
 * <p>Class which use when UpdateWordDTOAction is in JMS reception </p>
 * @author Samama Oriel
 *
 */
public class UpdateWordDTOAction implements Action{

	private UpdateWordDTO    dto;
	private WordManagerFrame wordManagerFrame;
	
	/**
	 * <p>Constructor of UpdateWordDTOAction</p>
	 * @param dto
	 * @param loginFrame
	 * @param wordManagerFrame
	 * @param lang
	 */
	public UpdateWordDTOAction(UpdateWordDTO dto , LoginFrame loginFrame , WordManagerFrame wordManagerFrame , String lang){
		this.dto              = dto;
		this.wordManagerFrame = wordManagerFrame;
	}
	/**
	 * <p>Action when the UpdateWordDTOAction is recept by the listener</p>
	 */
	@Override
	public void execute() {
		// Update node DTO
				wordManagerFrame.getMyJTree().updateNode(dto.getNodeId() , dto.getContent());
				
	}

}
