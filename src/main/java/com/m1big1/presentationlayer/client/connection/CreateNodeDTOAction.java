package com.m1big1.presentationlayer.client.connection;

import com.m1big1.presentationlayer.client.dto.CreateNodeDTO;
import com.m1big1.presentationlayer.client.nodes.AbstractNode;
import com.m1big1.presentationlayer.client.nodes.NodeFolder;
import com.m1big1.presentationlayer.client.nodes.NodeWord;
import com.m1big1.presentationlayer.client.swing.LoginFrame;
import com.m1big1.presentationlayer.client.swing.WordManagerFrame;
/**
 * <p>Class which use when CreateNodeDTOAction is in JMS reception </p>
 * @author SAMAMAO
 *
 */
public class CreateNodeDTOAction implements Action {
	
	private CreateNodeDTO    dto;
	private WordManagerFrame wordManagerFrame;
	private String           lang;
	/**
	 * <p>Constructor of CreateNodeDTOAction</p>
	 * @param dto
	 * @param loginFrame
	 * @param wordManagerFrame
	 * @param lang
	 */
	public CreateNodeDTOAction(CreateNodeDTO dto , LoginFrame loginFrame , WordManagerFrame wordManagerFrame , String lang){
		this.dto              = dto;
		this.wordManagerFrame = wordManagerFrame;
		this.lang             = lang;
	}
	/**
	 * <p>Action when the CreateNodeDTOAction is recept by the listener</p>
	 */
	@Override
	public void execute() {
		// Create the node which will add to jtree
				AbstractNode node = (dto.getNodeType() == 1) ? new NodeFolder(
						dto.getNodeId() , dto.getParentFolderId() , dto.getNodeName(),
						dto.getSessionId() , lang) : new NodeWord(dto.getNodeId() ,
						dto.getParentFolderId() , dto.getNodeName() , null ,
						dto.getSessionId() , lang);
				// Add node in JTree
				wordManagerFrame.getMyJTree().addNodeTo(node , dto.getParentFolderId());
		
	}

}
