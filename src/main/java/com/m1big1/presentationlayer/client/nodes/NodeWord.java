package com.m1big1.presentationlayer.client.nodes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.log4j.Logger;
/**
 * <p> <p>Class which create a node type Word </p>
 * @author Oriel
 *
 */
public class NodeWord extends AbstractNode{
    private JMenuItem optionUpdateWord; 
    private JMenuItem optionDeleteWord;
    
    private static String newNameWord;
    
    private static Logger logger = Logger.getLogger(NodeWord.class);
    
    /**
     * <p>Constructor of word node</p>
     * @param id
     * @param name
     * @param icon
     * @param content
     */
    public NodeWord(int myID , int myparentID , String myName , String myContent , String mySessionID , String myLang){
        super(myID , myparentID , myName ,myContent , true , wordIcon , mySessionID , myLang);
        initLanguages();
        menuOption = new JPopupMenu();
    }
    /**
     * <p>Init languages</p>
     */
    public void initLanguages(){
        try {
            ClassLoader classLoader    = Thread.currentThread().getContextClassLoader();
            
            Properties properties      = new Properties();
            
            InputStream propertiesFile = classLoader.getResourceAsStream("languages/language_"+ lang +".properties");
            properties.load(propertiesFile);
	     // options for WordNode
	        optionUpdateWord 		   = new JMenuItem(properties.getProperty("UpdateWord"));
	        optionDeleteWord           = new JMenuItem(properties.getProperty("DeleteWord"));
	        newNameWord                = properties.getProperty("newNameWord");
	        propertiesFile.close();
	        
        } catch (IOException e) {
        	logger.fatal("Problem to load languages" , e);
        }
    }
    /**
     * <p>Return Actual Node</p>
     * @return
     */
    public NodeWord getMySelf(){
        return this;
    }
    /**
     * <p>Return a the Menu for node type Word</p>
     */
    public JPopupMenu getNodeOptions() {
         if (optionUpdateWord.getActionListeners().length == 0)
        {
            optionUpdateWord.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onClickRenameNode(newNameWord , getMySelf()); 
                }
            });
            menuOption.add(optionUpdateWord);
        }
        
        if (optionDeleteWord.getActionListeners().length == 0)
        {
            optionDeleteWord.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onClickDeleteNode(getMySelf());
                }
    
            });
            menuOption.add(optionDeleteWord);
        }
        menuOption.setOpaque(true);
        menuOption.setLightWeightPopupEnabled(true);
        return menuOption;
    }
}
