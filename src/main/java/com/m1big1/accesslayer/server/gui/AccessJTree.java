package com.m1big1.accesslayer.server.gui;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.m1big1.accesslayer.server.dao.NodeDAO;
import com.m1big1.accesslayer.server.model.Node;

public class AccessJTree extends JTree implements ActionListener {
    private JPopupMenu fileOptions      = new JPopupMenu();
    
    private JMenuItem optionNewFolder   = new JMenuItem();
    private JMenuItem optionNewWord     = new JMenuItem();
    private JMenuItem optionReadNode    = new JMenuItem();
    private JMenuItem optionDeleteNode  = new JMenuItem();
    private JMenuItem optionUpdateNode  = new JMenuItem();
    private JMenuItem optionRenameNode  = new JMenuItem();
    private JMenuItem optionRefresh     = new JMenuItem();

    private Map<Integer, Node> nodeMap;
    private NodeDAO nodeDAO;
    private DatabaseInterface gui;

    public AccessJTree(DatabaseInterface gui, NodeDAO nodeDAO, Map<Integer, Node> nodeMap, final DefaultMutableTreeNode node) {
        super(node);
        this.gui = gui;
        this.nodeMap = nodeMap;
        this.nodeDAO = nodeDAO;
        initJPopupMenu();
        initJTreeMouseListener();
        setCellRenderer(new NodeRenderer());
    }

    public void refreshAccessTree(DefaultMutableTreeNode myNode, TreePath myPath) {
        ((DefaultTreeModel) getModel()).nodeChanged((TreeNode) myNode);
        ((DefaultTreeModel) getModel()).reload((TreeNode) myNode);
        makeVisible(myPath);
        expandPath(getSelectionPath());
    }

    public AccessJTree getAccessTree() {
        return this;
    }
    
    public void initJPopupMenu() {
        //Options for folder
            optionNewFolder.setText("New Folder");
            optionNewFolder.setActionCommand("newFolder");
            optionNewFolder.addActionListener(this);
            fileOptions.add(optionNewFolder);

            optionNewWord.setText("New Word");
            optionNewWord.setActionCommand("newWord");
            optionNewWord.addActionListener(this);
            fileOptions.add(optionNewWord);
            
            optionReadNode.setText("Read");
            optionReadNode.setActionCommand("readNode");
            optionReadNode.addActionListener(this);
            fileOptions.add(optionReadNode);
            
            optionDeleteNode.setText("Delete");
            optionDeleteNode.setActionCommand("deleteNode");
            optionDeleteNode.addActionListener(this);
            fileOptions.add(optionDeleteNode);

            optionRenameNode.setText("Rename");
            optionRenameNode.setActionCommand("renameNode");
            optionRenameNode.addActionListener(this);
            fileOptions.add(optionRenameNode);
            
            optionUpdateNode.setText("Update");
            optionUpdateNode.setActionCommand("updateNode");
            optionUpdateNode.addActionListener(this);
            fileOptions.add(optionUpdateNode);

            optionRefresh.setText("Refresh");
            optionRefresh.setActionCommand("refresh");
            optionRefresh.addActionListener(this);
            fileOptions.add(optionRefresh);

        fileOptions.setOpaque(true);
        fileOptions.setLightWeightPopupEnabled(true);
    }

    public void deleteNode(Node node) {
        for(int i = 0; i<node.getChildCount(); i++) {
            deleteNode((Node) node.getChildAt(i));
        }
        nodeMap.remove(node.getNodeID());
        node.removeAllChildren();
        node.removeFromParent();
    }
    
    public void initJTreeMouseListener() {
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                int row = getAccessTree().getRowForLocation(e.getX(), e.getY());
                if (row == -1)
                    return;
                getAccessTree().setSelectionRow(row);
                if (e.getClickCount() == 2) {
                    TreePath path = getSelectionPath();
                    Node selectedNode = (Node) path.getLastPathComponent();
                    //System.out.println(selectedNode.getNodeID());
                    if(selectedNode.getNodeTypeID()==Node.WORD)
                        readWordAction(selectedNode);
                }
                if (e.isPopupTrigger()) {
                    fileOptions.show((JComponent) e.getSource(), e.getX(), e.getY());
                }
            }
        });
    }
    
    public void newFolderAction(Node selectedNode) throws HeadlessException, SQLException, IOException {
        String reponse = JOptionPane.showInputDialog(null, "New node name");
        if(reponse == null) {
            return;
        }
        
        Node newNode = new Node(selectedNode.getNodeUserID(), selectedNode.getNodeID(), 1, reponse);
        
        if(nodeDAO.checkParent(newNode)) {
            if(nodeDAO.checkDupplicatedName(newNode)) {
                int id = nodeDAO.create(newNode);
                newNode.setNodeID(id);
                nodeMap.put(newNode.getNodeID(), newNode);
                nodeMap.get(selectedNode.getNodeID()).add(newNode);
                JOptionPane.showMessageDialog(null, "Folder created.");
            } else {
                JOptionPane.showMessageDialog(null, "Duplicated name in the parent node.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "The folder parent doesn't belong to you.");
        }
    }

    public void newWordAction(Node selectedNode) throws HeadlessException, SQLException, IOException{
        String reponse = JOptionPane.showInputDialog(null, "New node name");
        if(reponse == null) {
            return;
        }
        
        Node newNode = new Node(selectedNode.getNodeUserID(), selectedNode.getNodeID(), 2, reponse);
        
        if(nodeDAO.checkParent(newNode)) {
            if(nodeDAO.checkDupplicatedName(newNode)) {
                int id = nodeDAO.create(newNode);
                newNode.setNodeID(id);
                nodeMap.put(newNode.getNodeID(), newNode);
                nodeMap.get(selectedNode.getNodeID()).add(newNode);
                JOptionPane.showMessageDialog(null, "File Word created.");
            } else {
                JOptionPane.showMessageDialog(null, "Duplicated name in the parent node.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "The folder parent doesn't belong to you.");
        }
    }
    
    public void readWordAction(Node selectedNode) {
        gui.setNodeContent(selectedNode.getNodeName(), selectedNode.getNodeContent());
    }
    
    public void deleteNodeAction(Node selectedNode) throws HeadlessException, SQLException, IOException {
        Node node = new Node(selectedNode.getNodeUserID(), selectedNode.getNodeID());
        if(nodeDAO.checkNode(node)) {
            nodeDAO.delete(node);
            deleteNode(nodeMap.get(node.getNodeID()));
            JOptionPane.showMessageDialog(null, "Node deleted.");
        } else {
            JOptionPane.showMessageDialog(null, "The node doesn't belong to you.");
        }
    }
    
    public void renameNodeAction(Node selectedNode) throws HeadlessException, SQLException, IOException {
        String reponse = JOptionPane.showInputDialog(null, "New node name");
        if(reponse == null) {
            return;
        }
        
        Node node = new Node(selectedNode.getNodeUserID(), selectedNode.getNodeID(), reponse);
        //System.out.println(node.getNodeID());
        if(nodeDAO.checkNodeRename(node)) {
            nodeDAO.rename(node);
            nodeMap.get(selectedNode.getNodeID()).setNodeName(reponse);
            JOptionPane.showMessageDialog(null, "Node renamed.");
        } else {
            JOptionPane.showMessageDialog(null, "The node can not be renamed.");
        }
    }
    
    public void updateNodeAction(Node selectedNode) throws HeadlessException, SQLException, IOException {
        Node node = new Node(selectedNode.getNodeUserID(), selectedNode.getNodeID(), 0, 2, "", gui.getWordNewContent(), 0 , 0 );
        
        if(nodeDAO.checkNodeRename(node)) {
            nodeDAO.update(node);
            nodeMap.get(selectedNode.getNodeID()).setNodeContent(node.getNodeContent());;
            JOptionPane.showMessageDialog(null, "Node updated.");
        } else {
            JOptionPane.showMessageDialog(null, "The node can not be updated.");
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e){
        TreePath path = this.getSelectionPath();
        Node selectedNode = (Node) path.getLastPathComponent();
        
        try {
            
            switch(Options.valueOf(e.getActionCommand())) {
                case newFolder:
                    newFolderAction(selectedNode);
                    break;
                case newWord:
                    newWordAction(selectedNode);
                    break;
                case readNode:
                    readWordAction(selectedNode);
                    break;
                case deleteNode:
                    deleteNodeAction(selectedNode);
                    break;
                case renameNode:
                    renameNodeAction(selectedNode);
                    break;
                case updateNode:
                    updateNodeAction(selectedNode);
                    break;
                case refresh:
                    refreshAccessTree(selectedNode, path);
                    break;
                default:
                    break;
            }
            refreshAccessTree(selectedNode, path);
        } catch(Exception e1) {
            e1.printStackTrace();
        }
    }
    
    public enum Options {
        newFolder, newWord, readNode, deleteNode,
        renameNode, updateNode, refresh 
    }
}