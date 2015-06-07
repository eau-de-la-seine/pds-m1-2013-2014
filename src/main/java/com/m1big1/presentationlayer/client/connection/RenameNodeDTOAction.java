package com.m1big1.presentationlayer.client.connection;

import com.m1big1.presentationlayer.client.dto.RenameNodeDTO;
import com.m1big1.presentationlayer.client.swing.LoginFrame;
import com.m1big1.presentationlayer.client.swing.WordManagerFrame;
/**
 * <p>Class which use when RenameNodeDTOAction is in JMS reception </p>
 * @author Samama Oriel
 *
 */
public class RenameNodeDTOAction implements Action{
	
	private RenameNodeDTO    dto;
	private WordManagerFrame wordManagerFrame;

	/**
	 * <p>Constructor of RenameNodeDTOAction</p>
	 * @param dto
	 * @param loginFrame
	 * @param wordManagerFrame
	 * @param lang
	 */
	public RenameNodeDTOAction(RenameNodeDTO dto , LoginFrame loginFrame , WordManagerFrame wordManagerFrame , String lang){
		this.dto              = dto;
		this.wordManagerFrame = wordManagerFrame;
	}
	
	
	/**
	 * <p>Action when the RenameNodeDTOAction is recept by the listener</p>
	 */
	@Override
	public void execute() {
		// Rename node in JTree
				wordManagerFrame.getMyJTree().renameNode(dto.getNodeId() , dto.getName());
	}

}
