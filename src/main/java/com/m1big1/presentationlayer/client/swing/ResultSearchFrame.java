package com.m1big1.presentationlayer.client.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;

import com.m1big1.common.Transmitter;
import com.m1big1.presentationlayer.client.dto.NodeStructureDTO;
import com.m1big1.presentationlayer.client.dto.ReadWordDTO;
/**
 * <p>Class which create a frame which wee can see result of research</p>
 * @author Samama Oriel
 *
 */
public class ResultSearchFrame extends JFrame{

	private        NodeStructureDTO  rootNode;
	private        String 			 lang , fileNumber , fileName , buttonSeeWord;
	private        int 			     nodeId;
	private        DefaultTableModel tableur;
	private        JTable 		     tableHistory;
				   TableCellRenderer defaultRenderer;
				   JButton 	   		 button = new JButton();
	private 	   String      		 sessionID;
	private        Transmitter 		 transmitter;
	private static Logger      		 logger = Logger.getLogger(ResultSearchFrame.class);
	/**
	 *<p>Constructor of ResultSearchFrame</p> 
	 * @param title
	 * @param rootNode
	 * @param sessionID
	 * @param wordName
	 * @param transmitter
	 * @param lang
	 */
	public ResultSearchFrame(String title , NodeStructureDTO rootNode , String sessionID , String wordName , Transmitter transmitter , String lang) {
		super(title + " " + wordName + ".doc");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.lang 	     = lang;
		this.rootNode    = rootNode;
		this.sessionID   = sessionID;
		this.transmitter = transmitter;
		initLanguages();
		initComponent();
	}


	/**
	 * <p>Void which construct the table which we result of research</p>
	 */
	private void initComponent() {
		// Create Table Model (rows, columns...)
		tableur = new DefaultTableModel();
		// Add title column
		tableur.addColumn(fileNumber);
		tableur.addColumn(fileName);
		tableur.addColumn("");
		// Add line of Action History with NodeStructureDTO
		while(rootNode != null){
			tableur.addRow(new Object[] {rootNode.getId() , rootNode.getCompletePath()});
			rootNode = rootNode.getNextNode();
		}
		//Create JTable 
		tableHistory = new JTable(tableur);
		//Change the renderer of third cells whith grey font 
		tableHistory.getColumn("").setCellRenderer(new ButtonRenderer());
		//Make a CheckBox in this cell
		tableHistory.getColumn("").setCellEditor(new ButtonEditor(new JCheckBox()));
		//Block reordering
		tableHistory.getTableHeader().setReorderingAllowed(false);
		tableHistory.setPreferredScrollableViewportSize(new Dimension(600 , 400));
		add(new JScrollPane(tableHistory) , BorderLayout.CENTER);
	}
	/**
	 * <p>Init languages</p>
	 */
	private void initLanguages() {
		try {
            Properties properties      = new Properties();
            ClassLoader classLoader    = Thread.currentThread().getContextClassLoader();
            InputStream propertiesFile = classLoader.getResourceAsStream("languages/language_"+ lang +".properties");
            properties.load(propertiesFile);
            propertiesFile.close();
            // First Label of right panel
            fileNumber                 = properties.getProperty("fileNumber");
            fileName                   = properties.getProperty("fileName");
            buttonSeeWord              = properties.getProperty("buttonSeeWord");
        } catch (IOException e) {
        	logger.fatal("Problem to load languages" , e);
        }
		
	}
	/**
	 * <p><p>Void which make that we see window</p>
	 */
	public void showWindows() {
		 pack();
	     setResizable(false);
	     setLocationRelativeTo(null);
	     setVisible(true);
		
	}
	/**
	 * <p>Class with wich apply modifications on third cell</p>
	 * @author Samama Oriel
	 *
	 */
	class ButtonRenderer extends JButton implements TableCellRenderer {
		public ButtonRenderer() {
			setOpaque(true);
		}
		/**
		 * <p>fonction which we change the renderer of component</p>
		 */
		public Component getTableCellRendererComponent(JTable table ,
				Object value , boolean isSelected , boolean hasFocus , int row ,
				int column) {
			setText(buttonSeeWord);
			return this;
		}
	}
	/**
	 *  <p>Class with wich change the result when we click on third cell</p>
	 * @author Samama Oriel
	 *
	 */
	class ButtonEditor extends DefaultCellEditor {
		private String label;

		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
			
		}
		/**
		 * <p>Action when user click in third cell</p>
		 */
		public Component getTableCellEditorComponent(JTable table ,
				Object value , boolean isSelected , int row , int column) {
			//Make the same label
			label = (value == null) ? buttonSeeWord : value.toString();
			button.setText(label);
			//we make the node id for SearchWordDTO
			nodeId = (Integer) table.getValueAt(row , 0);
			if (button.getActionListeners().length == 0) {
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						ReadWordDTO readwordDTO = new ReadWordDTO();
	                    readwordDTO.setSessionId(sessionID);
	                    readwordDTO.setNodeId(nodeId);
	                        try {
	                            String xml = Transmitter.objectToXml(readwordDTO);
	                            logger.info("Message OUT \n" + xml);
	                            transmitter.send(xml);
	                            } catch (Exception e1) {
	                            	logger.fatal("Problem to send ReadWordDTO" , e1);
	                            } 
					}
				});
			}
			return button;
		}

		public Object getCellEditorValue() {
			return new String(buttonSeeWord);
		}
	}
}
