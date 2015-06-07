package com.m1big1.presentationlayer.client.connection;

import javax.swing.JOptionPane;

import com.m1big1.presentationlayer.client.dto.ErrorDTO;
import com.m1big1.presentationlayer.client.swing.LoginFrame;
import com.m1big1.presentationlayer.client.swing.WordManagerFrame;
/**
 * <p>Class which use when ErrorDTOAction is in JMS reception </p>
 * @author Samama Oriel
 *
 */
public class ErrorDTOAction implements Action{
	
	private ErrorDTO dto;

	/**
	 * <p>Constructor of ErrorDTOAction</p>
	 * @param dto
	 * @param loginFrame
	 * @param wordManagerFrame
	 * @param lang
	 */
	public ErrorDTOAction(ErrorDTO dto , LoginFrame loginFrame , WordManagerFrame wordManagerFrame , String lang){
		this.dto = dto;
	}
	/**
	 * <p>Action when the ErrorDTOAction is recept by the listener</p>
	 */
	@Override
	public void execute() {
		// Open a pane with Error Message
		JOptionPane.showMessageDialog(null , dto.getErrorMessage() ,
				dto.getErrorTitle() , JOptionPane.ERROR_MESSAGE);
	}

}
