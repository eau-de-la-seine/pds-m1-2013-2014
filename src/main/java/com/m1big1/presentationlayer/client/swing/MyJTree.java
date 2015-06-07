package com.m1big1.presentationlayer.client.swing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import com.m1big1.common.Transmitter;
import com.m1big1.presentationlayer.client.dto.ReadWordDTO;
import com.m1big1.presentationlayer.client.nodes.AbstractNode;
import com.m1big1.presentationlayer.client.nodes.NodeWord;
/**
 * <p> Class which is created JTree, it set options for nodes and listener</p>  
 * @author Oriel SAMAMA
 *
 */
 public class MyJTree extends JTree {
    private        WordManagerFrame           wordFrame;
    private        Map<Integer, AbstractNode> nodeMap;
	private 	   String 					  saveModificationMessage;
    private static String                     sessionID;
    private static Logger                     logger = Logger.getLogger(LoginFrame.class);
    
    
    /**
     * <p>Constructor of JTree</p>
     * @param noeuds
     * @param wordFrame
     * @param map
     * @param SessionId
     * @param language
     * @param myTransmitter
     * @param myImplementationMessageListener
     * @throws IOException
     */
    public MyJTree(final AbstractNode noeuds, WordManagerFrame myWordFrame, Map<Integer, AbstractNode> map, String mySessionID) throws IOException {
        super(noeuds);
        wordFrame = myWordFrame;
        nodeMap   = map;
        sessionID = mySessionID;
        // Mouse listeners for node
        ListenerMouse();
        initLanguages();
    }
    /**
     * <p>When action was make, we refresh the JTree with modifications</p>
     * @param node
     * @param path
     */
	public void refreshMyJTree(AbstractNode node, TreePath path ) {
    	( (DefaultTreeModel) getModel()).nodeChanged(node);
    	( (DefaultTreeModel) getModel()).reload(node);
    	makeVisible(path);
    	expandPath(getSelectionPath());
    }
    /**
     * <p>Make node visibles</p>
     */
    public void makeNodeVisible(){
        makeVisible(getSelectionPath());
        expandPath(getSelectionPath());
    }
    /**
     * <p>Add Node in JTree</p>
     * @param node
     * @param parentID
     */
    public void addNodeTo(AbstractNode node , int parentID) {
        nodeMap.put(node.getId() , node);
        AbstractNode parentNode = nodeMap.get(parentID);
        ((DefaultTreeModel)getModel()).insertNodeInto(node , parentNode , parentNode.getChildCount());
        refreshMyJTree(parentNode , this.getSelectionPath());
    }
    /**
     * <p>Remove node in JTree</p>
     * @param nodeID
     */
    public void removeNode(int nodeID) {
        AbstractNode node = (AbstractNode) nodeMap.get(nodeID);
        node.removeFromParent(); // a voir
        nodeMap.remove(nodeID);
        ((DefaultTreeModel)this.getModel()).nodeStructureChanged((AbstractNode)node);
        refreshMyJTree(node , this.getSelectionPath());
        wordFrame.makeWelcomePanel();
    }
    /**
     * <p>Remove node in JTree</p>
     * @param nodeID
     * @param nodeName
     */
    public void renameNode(int nodeID , String nodeName){
        AbstractNode node = nodeMap.get(nodeID);
        node.setName(nodeName);
        ((DefaultTreeModel)this.getModel()).nodeChanged(node);
        refreshMyJTree(node , this.getSelectionPath());
        
    }
    /**
     * <p>Update node in JTree</p>
     * @param nodeID
     * @param myContent
     */
    public void updateNode(int nodeID , String myContent){
        AbstractNode node = nodeMap.get(nodeID);
        //node.setContent(myContent);
        ((DefaultTreeModel)this.getModel()).nodeStructureChanged((AbstractNode)node);
        refreshMyJTree(node , this.getSelectionPath());
        JOptionPane.showMessageDialog(wordFrame, saveModificationMessage);
        wordFrame.setNodeWord(nodeID, myContent);
    }
    /**
     * <p>return To String</p>
     */
    public String toString() {
        return getModel().getRoot().toString();
    }
   
    /**
     * <p>Method contains interaction with nodes</p>
     */
    private void ListenerMouse() {
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                int row = getRowForLocation(e.getX() , e.getY());
                if (row == -1) return;
                setSelectionRow(row);
                AbstractNode node = (AbstractNode) getLastSelectedPathComponent();
                if (e.getClickCount() == 2) {
                    if (node instanceof NodeWord) {
                    	//wordFrame.setNodeWord(node.getId());
                       ReadWordDTO readwordDTO = new ReadWordDTO();
                       readwordDTO.setSessionId(sessionID);
                       readwordDTO.setNodeId(node.getId());
                        try {
                            String xml = Transmitter.objectToXml(readwordDTO);
                            logger.info("Message OUT \n" + xml);
                            wordFrame.getTransmitter().send(xml);
                            } catch (Exception e1) {
                            	logger.fatal("Problem to send ReadWordDTO" , e1);
                            } 
                       
                    }
                }
                if (e.isPopupTrigger()) {
                    node.getNodeOptions().show((JComponent) e.getSource() , 
                            e.getX() , e.getY());
                }
            }
        });
        
    }
	private void initLanguages() {
		try {
            Properties properties      = new Properties();
            ClassLoader classLoader    = Thread.currentThread().getContextClassLoader();
            InputStream propertiesFile = classLoader.getResourceAsStream("languages/language_"+ wordFrame.getLang() +".properties");
            properties.load(propertiesFile);
            propertiesFile.close();
            // First Label of right panel
            saveModificationMessage                 = properties.getProperty("savemodificationmessage");
        } catch (IOException e) {
        	logger.fatal("Problem to load languages" , e);
        }
		
	}
}