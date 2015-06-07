package com.m1big1.presentationlayer.client.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.m1big1.presentationlayer.client.dto.HistoryEventDTO;
/**
 * <p>Class which create a frame which wee can see history of actions</p>
 * @author Samama Oriel
 *
 */
public class HistoryFrame extends JFrame {
	private        String            lang;
	private        String            dateHistory, objectHistory, actionHistory;
	private        DefaultTableModel tableur;
	private        JTable            tableHistory;
	private        HistoryEventDTO   myHistory;
	private static Logger            logger = Logger.getLogger(HistoryFrame.class);

	/**
	 * <p>Constructor of History Frame</p>
	 * 
	 * @param title
	 * @param history
	 * @param lang
	 */
	public HistoryFrame(String title, HistoryEventDTO history, String lang) {
		super(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.lang      = lang;
		this.myHistory = history;
		initLanguages();
		initComponent();
	}

	/**
	 * <p>
	 * Init languages
	 * </p>
	 */
	private void initLanguages() {
		try {
			Properties properties      = new Properties();
			ClassLoader classLoader    = Thread.currentThread().getContextClassLoader();
			InputStream propertiesFile = classLoader.getResourceAsStream("languages/language_" + lang+ ".properties");
			properties.load(propertiesFile);
			propertiesFile.close();
			// First Label of right panel
			dateHistory   			   = properties.getProperty("dateHistory");
			actionHistory              = properties.getProperty("actionHistory");
			objectHistory              = properties.getProperty("objectHistory");
		} catch (IOException e) {
			logger.fatal("Problem to load languages", e);
		}
	}
	/**
	 * <p>Void which construct the table which we have history</p>
	 */
	private void initComponent() {
		// Create Table Model (rows, columns...)
		tableur = new DefaultTableModel();
		// Add title column
		tableur.addColumn(dateHistory);
		tableur.addColumn(objectHistory);
		tableur.addColumn(actionHistory);
		// Add line of Action History with HistoryEventDTO
		while (myHistory != null) {
			tableur.addRow(new Object[] { myHistory.getEventDate() , myHistory.getCompletePath() , myHistory.getEventKind() });
			myHistory = myHistory.getNextHistoryEvent();
		}
		//Create JTable and make edition of cell at "false"
		tableHistory = new JTable(tableur) {
			@Override
			public boolean isCellEditable(int row , int col) {
				return false;
			}
		};
		//Add JTable to Frame
		tableHistory.setPreferredScrollableViewportSize(new Dimension(600 , 400));
		add(new JScrollPane(tableHistory) , BorderLayout.CENTER);
	}
	/**
	 * <p>Void which make that we see window</p>
	 */
	public void showWindows() {
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

	}
}
