package com.m1big1.presentationlayer.client.nodes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import com.m1big1.common.Transmitter;
import com.m1big1.presentationlayer.client.dto.CreateNodeDTO;
import com.m1big1.presentationlayer.client.dto.DeleteNodeDTO;
import com.m1big1.presentationlayer.client.dto.RenameNodeDTO;
/**
 * <p>Abstract Class which define nodes which was use by application</p>
 * @author Oriel
 *
 */
public abstract class AbstractNode extends DefaultMutableTreeNode {
    private static   String      sessionID;
    protected static String      lang;
    protected static ImageIcon   folderIcon , userIcon , wordIcon;
    public static    Transmitter transmitter;
    protected        JPopupMenu  menuOption;
    private static   Logger      logger = Logger.getLogger(AbstractNode.class);

    
	private          int         id;
	private          int         parentID;
    private          String      name;
    private          String      content;
    private          boolean     typeNode;
    protected 		 ImageIcon   icon;
	private   static String      confirmSuppr;
    
    /**
     * <p>Constructor of AbstractNode</p>
     * @param id
     * @param name
     * @param content
     * @param typeNode
     * @param icon
     */
    public AbstractNode(int myID , int myparentID , String myName , String myContent , boolean mytypeNode , ImageIcon myIcon , String mySessionID , String myLang)
    {
        id        = myID; 
        parentID  = myparentID;
        name      = myName; 
        content   = myContent;
        typeNode  = mytypeNode;
        icon      = myIcon ;
        sessionID = mySessionID;
        lang      =  myLang ;
        initLanguages();
    }
    
    
    
    /**
     * <p>Method which init images with imageLocation.properties</p>
     */
    public static void initImages(){
        try {
            ClassLoader classLoader         = Thread.currentThread().getContextClassLoader();
            
            Properties propertiesImage      = new Properties();
            InputStream propertiesImageFile = classLoader.getResourceAsStream("images/imageLocation.properties");
            propertiesImage.load(propertiesImageFile);
            

            String wordIconLocation         = propertiesImage.getProperty("WORD_ICON_LOCATION");
            wordIcon                        = new ImageIcon(NodeWord.class.getResource(wordIconLocation));
            
            String folderIconLocation       = propertiesImage.getProperty("FOLDER_ICON_LOCATION");
            folderIcon                      = new ImageIcon(NodeFolder.class.getResource(folderIconLocation));

            String userIconLocation         = propertiesImage.getProperty("USER_ICON_LOCATION");
            userIcon                        = new ImageIcon(NodeUser.class.getResource(userIconLocation));
            
            
            propertiesImageFile.close();
        } catch (IOException e) {
        	logger.fatal("Problem to load languages" , e);
        }
    }
    
    public void initLanguages(){
        try {
            ClassLoader classLoader    = Thread.currentThread().getContextClassLoader();
            
            Properties properties      = new Properties();
            
            InputStream propertiesFile = classLoader.getResourceAsStream("languages/language_"+ lang +".properties");
            properties.load(propertiesFile);
	     // options for WordNode
	        confirmSuppr                = properties.getProperty("confirmSuppr");
	        System.out.println(confirmSuppr);
	        propertiesFile.close();
	        
        } catch (IOException e) {
        	logger.fatal("Problem to load languages" , e);
        }
    }
    
    
    
    /**
     * <p>When Client want to add a new node, an object (CreateNodeDTO) will send to business layer</p>
     * @param type
     * @param nameNode
     * @param nodeselected
     */
    public void onClickNewNode(int type , String nameNode , AbstractNode nodeselected) {
    	//JOptionPane which request name of node
        String reponse = JOptionPane.showInputDialog(null , nameNode);
        if(reponse != null) {
        	//Create an DTO with informations
            CreateNodeDTO createNode = new CreateNodeDTO();
            createNode.setSessionId(sessionID);
            createNode.setNodeType(type);
            createNode.setParentFolderId(nodeselected.getId());
            createNode.setNodeName(reponse);
            try {
            	//Send XML
                String xml = Transmitter.objectToXml(createNode);
                logger.info("Message OUT \n" + xml);
                transmitter.send(xml);
            } catch (Exception e1) {
            	logger.warn("Problem when we send CreateNodeDTO" , e1);
            }
        }
    }
    
    
    
    
    /**
     * <p>Method when we click in option for node to rename node with sendDTO informations</p>
      * @param nameNode
      * @param nodeSelected
      */
     public void onClickRenameNode(String nameNode , AbstractNode nodeSelected) {
    	//JOptionPane which request name of node
         String reponse = JOptionPane.showInputDialog(null , nameNode);
         if(reponse != null) {
        	//Create an DTO with informations
             RenameNodeDTO renameNode = new RenameNodeDTO();
             renameNode.setNodeId(nodeSelected.getId());
             renameNode.setName(reponse);
             renameNode.setSessionId(sessionID);
             try {
            	//Send XML
                 String xml = com.m1big1.common.Transmitter.objectToXml(renameNode);
                 logger.info("Message OUT \n" + xml);
                 transmitter.send(xml);
             } catch (Exception e1) {
            	 logger.fatal("Problem when we send RenameNodeDTO" , e1);
             }
         }
     }
     
     
     
     /**
      * <p>Method when we click in option for node to delete node with sendDTO informations</p>
      * @param nodeselected
      */
     public void onClickDeleteNode(AbstractNode nodeselected) {

	        System.out.println("toto "+confirmSuppr);
         int option = JOptionPane.showConfirmDialog(null, 
        		 confirmSuppr + " " +nodeselected.getName() + " ?", 
           "", 
           JOptionPane.YES_NO_OPTION, 
           JOptionPane.QUESTION_MESSAGE);
         if(option == JOptionPane.OK_OPTION){
    	//Create an DTO with informations
         DeleteNodeDTO deleteNode = new DeleteNodeDTO();
         deleteNode.setSessionId(sessionID);
         deleteNode.setNodeId(nodeselected.getId());
         try {
        	//Send XML
             String xml = com.m1big1.common.Transmitter.objectToXml(deleteNode);
             logger.info("Message OUT \n" + xml);
             transmitter.send(xml);
         } catch (Exception e1) {
        	 logger.fatal("Problem when we send DeleteNodeDTO" , e1);
         }
        }
     }
     
     
     
     public abstract JPopupMenu getNodeOptions();

    /**
     * <p>ToString</p>
     */
    @Override
    public String toString() {
        return id + " " + name + " " + typeNode + " " + this.getClass();
    }
    
    
    
    /**
     * </p>Return id of node</p>
     * @return 
     */
    public int getId() {
        return id;
    }
    
    
    
    /**
     * <p>Set a number of id</p>
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }
    
    
    
    /**
     * <p>Return name of node</p>
     * @return
     */
    public String getName() {
        return name;
    }
    
    
    
    /**
     * <p>return name of node</p>
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    
    
    /**
     * <p>return content of node</p>
     * @return
     */
    public String getContent() {
        return content;
    }
    
    
    
    /**
     * <p>Set content of node</p>
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    
    
    /**
     * <p>Return type of node</p>
     * @return
     */
    public boolean isTypeNode() {
        return typeNode;
    }
    
    
    
    /**
     * <p>Set type of node</p>
     * @param typeNode
     */
    public void setTypeNode(boolean typeNode) {
        this.typeNode = typeNode;
    }
    
    
    
    /**
     * <p>Return icon of node</p>
     * @return
     */
    public ImageIcon getIcon() {
        return icon;
    }
    
    
    
    /**
     *  <p>Set icon of node</p>
     * @param icon
     */
    
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
    
    
    
    /**
     * <p>Return parentid </p>
     * @return
     */
    public int getParentId() {
        return parentID;
    }
    
    
    
    /**
     * <p>Set parentid for a node</p>
     * @param parentId
     */
    public void setParentId(int parentId) {
        this.parentID = parentId;
    }
    
    
    /**
     * <p>Set Transmitter </p>
     * @param myTransmitter
     */
    public static void setMyTransmitter(Transmitter myTransmitter) {
       transmitter = myTransmitter;
    }
}
    