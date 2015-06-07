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
 * <p>Class which create a node type User </p>
 * @author Oriel SAMAMA
 *
 */
public class NodeUser extends AbstractNode{
    private        JMenuItem optionCreateFolder , optionCreateWord; 
    private        String    nameNewWord , newNameFolder;
    private static Logger    logger = Logger.getLogger(NodeUser.class);
    
    /**
     * <p>Constructor for node type User</p>
     * @param myID
     * @param myparentID
     * @param myName
     * @param mySessionID
     * @param myLang
     */
    public NodeUser(int myID , int myparentID , String myName , String mySessionID , String myLang ){
        super(myID, myparentID , myName , null , true , userIcon , mySessionID , myLang);
        initLanguages();
        menuOption = new JPopupMenu();
    }
    
    
    /**
     * <p>Return Actual Node</p>
     * @return
     */
    public NodeUser getMySelf(){
        return this;
    }
    /**
     * <p>Init languages</p>
     */
    public void initLanguages(){
        try {
            ClassLoader classLoader             = Thread.currentThread().getContextClassLoader();
            Properties propertiesLanguage       = new Properties();
            InputStream propertiesLanguageFile  = classLoader.getResourceAsStream("languages/language_"+ lang +".properties");
           
            propertiesLanguage.load(propertiesLanguageFile);
            
            nameNewWord  					    = propertiesLanguage.getProperty("NameNewWord");
            newNameFolder                       = propertiesLanguage.getProperty("newNameFolder");
            
            optionCreateFolder                  = new JMenuItem(propertiesLanguage.getProperty("CreateFolder"));
            optionCreateWord                    = new JMenuItem(propertiesLanguage.getProperty("CreateWord"));

            propertiesLanguageFile.close();
        } catch (IOException e) {
        	logger.warn("Problem to load languages" , e);
        }
    }
    /**
     * <p>Return a the Menu for node type User</p>
     */
    public JPopupMenu getNodeOptions(){
     // option for userNode
    	 if (optionCreateFolder.getActionListeners().length == 0)
         {
        optionCreateFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickNewNode(1 , newNameFolder , getMySelf());
            }
        });
        menuOption.add(optionCreateFolder);
         }
    	 if (optionCreateWord.getActionListeners().length == 0)
         {
        optionCreateWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onClickNewNode(2 , nameNewWord , getMySelf());
            }
        });
        menuOption.add(optionCreateWord);
         }
        menuOption.setOpaque(true);
        menuOption.setLightWeightPopupEnabled(true);
        return menuOption;
      
    }
}