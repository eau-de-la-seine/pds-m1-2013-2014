package com.m1big1.presentationlayer.client.connection;


import com.m1big1.presentationlayer.client.dto.DeleteNodeDTO;
import com.m1big1.presentationlayer.client.swing.LoginFrame;
import com.m1big1.presentationlayer.client.swing.WordManagerFrame;
/**
 * <p>Class which use when DeleteNodeDTOAction is in JMS reception </p>
 * @author Samama Oriel
 *
 */
public class DeleteNodeDTOAction implements Action{

	private DeleteNodeDTO    dto;
	private WordManagerFrame wordManagerFrame;
	/**
	 * <p>Constructor of DeleteNodeDTOAction</p>
	 * @param dto
	 * @param loginFrame
	 * @param wordManagerFrame
	 * @param lang
	 */
	public DeleteNodeDTOAction(DeleteNodeDTO dto , LoginFrame loginFrame, WordManagerFrame wordManagerFrame, String lang){
		this.dto              = dto;
		this.wordManagerFrame = wordManagerFrame;
	}
	/**
	 * <p>Action when the DeleteNodeDTOAction is recept by the listener</p>
	 */
	@Override
	public void execute() {
		wordManagerFrame.getMyJTree().removeNode(dto.getNodeId());
	}
}
