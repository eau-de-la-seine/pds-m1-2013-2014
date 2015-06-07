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
 *<p>Class which create a node type folder </p>
 * @author Oriel SAMAMA
 *
 */
public class NodeFolder extends AbstractNode{
    private 	   JMenuItem optionCreateFolderForFolder , optionCreateWordforFolder , optionRenameFolder , optionDeleteFolder; 
    private        String    nameNewWord , newNameFolder;
    private static Logger    logger = Logger.getLogger(NodeFolder.class);
    
    /**
     * <p>Constructor of node Folder</p>
     * @param id
     * @param name
     * @param icon
     */
    
    public NodeFolder(int myID , int myparentID , String myName , String mySessionID , String myLang){
        super(myID, myparentID , myName , null , true , folderIcon , mySessionID , myLang);
        initLanguage();
        menuOption = new JPopupMenu();
    }
    
    
    
    /**
     * <p>Method which init languages with properties</p>
     */
    public void initLanguage(){
        try {
        	ClassLoader classLoader            = Thread.currentThread().getContextClassLoader();
            
            Properties propertiesLanguage      = new Properties();
            
            InputStream propertiesLanguageFile = classLoader.getResourceAsStream("languages/language_"+ lang +".properties");
           
            propertiesLanguage.load(propertiesLanguageFile);
            
            optionCreateFolderForFolder        = new JMenuItem(propertiesLanguage.getProperty("NewFolderInFolder"));
            optionCreateWordforFolder          = new JMenuItem(propertiesLanguage.getProperty("NewWordInFolder"));
        
            // options for Folder
            optionCreateWordforFolder          = new JMenuItem(propertiesLanguage.getProperty("CreateWord"));
            optionCreateFolderForFolder        = new JMenuItem(propertiesLanguage.getProperty("CreateFolder"));
            optionRenameFolder                 = new JMenuItem(propertiesLanguage.getProperty("RenameFolder"));
            optionDeleteFolder                 = new JMenuItem(propertiesLanguage.getProperty("DeleteFolder"));
            // Names
            nameNewWord                        = propertiesLanguage.getProperty("NameNewWord");
            newNameFolder                      = propertiesLanguage.getProperty("newNameFolder");
            propertiesLanguageFile.close();
        } catch (IOException e) {
        	logger.fatal("Problem to load languages" , e);
        }
    }
    
    
    
    /**
     * <p>Method which return actual object node</p>
     * @return
     */
    public NodeFolder getMyself() {
        return this;
    }
    
    
    
    /**
     * <p>Method which return options for folder nodes
     */
    public JPopupMenu getNodeOptions(){
    	//If there are not yet a listener for this option
        if (optionCreateFolderForFolder.getActionListeners().length == 0)
        {
            optionCreateFolderForFolder.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	//Class which send order to create node
                    onClickNewNode(1 , newNameFolder , getMyself());
                }
            });
            menuOption.add(optionCreateFolderForFolder);
        }
      //If there are not yet a listener for this option
        if (optionCreateWordforFolder.getActionListeners().length == 0)
        {
            optionCreateWordforFolder.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	//Class which send order to create node
                    onClickNewNode(2 , nameNewWord ,getMyself());
                }
            });
            menuOption.add(optionCreateWordforFolder);
        }
      //If there are not yet a listener for this option
        if (optionRenameFolder.getActionListeners().length == 0)
        {
            optionRenameFolder.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	//Class which send order to rename node
                    onClickRenameNode(newNameFolder , getMyself());
                }
            });
            menuOption.add(optionRenameFolder);
        }
      //If there are not yet a listener for this option
        if (optionDeleteFolder.getActionListeners().length == 0)
        {
        	optionDeleteFolder.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onClickDeleteNode(getMyself());
                }
    
            });
            menuOption.add(optionDeleteFolder);
        }
        menuOption.setOpaque(true);
        menuOption.setLightWeightPopupEnabled(true);
        return menuOption;
     }
   
}
