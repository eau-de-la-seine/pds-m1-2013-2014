package com.m1big1.accesslayer.server.model;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import com.m1big1.accesslayer.server.dto.NodeStructureDTO;

/**
 * <p>Node class is the model of a Word file or a Folder.</p>
 * @author      Huu Khuong NGUYEN
 * @category    Model
 */
public class Node extends DefaultMutableTreeNode {
    private                 int     nodeUserID;
    private                 int     nodeID;
    private                 int     nodeParentID;
    private                 int     nodeTypeID;
    private                 String  nodeName;
    private                 String  nodeCompletePath;
    private                 String  nodeContent;
    private                 int     leftIndex;
    private                 int     rightIndex;
    public  static final    int     FOLDER = 1;
    public  static final    int     WORD = 2;
    
    /**
     * <p>The constructor of a Node. The constructor is used for a case general.<br>
     * - <b>nodeUserID</b>  : The ID of the User of the Node.<br>
     * - <b>nodeID</b>      : The ID of the Node.<br>
     * - <b>nodeParentID</b>: The ID of the Node Parent that contains the Node.<br>
     * - <b>nodeTypeID</b>  : The type of the Node. (by the business convention, 1 means a <i>Folder</i> and 2 means a <i>Word file</i> <br>
     * - <b>nodeName</b>    : The name of the new Node<br>
     * - <b>nodeContent</b> : The content of the new Node<br>
     * - <b>leftIndex</b>   : The left index of the new Node<br>
     * - <b>rightIndex</b>  : The right index  of the new Node<br>
     * So the constructor is only used to <b>create a new node</b>.</p>
     */
    public Node(int nodeUserID, int nodeID, int nodeParentID, int nodeTypeID, String nodeName, String nodeContent, int leftIndex, int rightIndex) {
        this.nodeUserID = nodeUserID;
        this.nodeID = nodeID;
        this.nodeParentID = nodeParentID;
        this.nodeTypeID = nodeTypeID;
        this.nodeName = nodeName;
        this.nodeContent = nodeContent;
        this.leftIndex = leftIndex;
        this.rightIndex = rightIndex;
    }
    
    /**
     * <p>Create a Node with : <br>
     * - <b>nodeUserID</b>  : The ID of the User of the Node.<br>
     * - <b>nodeID</b>      : The ID of the Node.<br>
     * - <b>nodeName</b>    : The name of the new Node<br>
     * - <b>nodeContent</b> : The content of the new Node<br>
     * So the constructor is only used to <b>update the content of a node</b>.</p>
     */
    public Node(int nodeUserID, int nodeID, String nodeName, String nodeContent) {
        this.nodeUserID = nodeUserID;
        this.nodeID = nodeID;
        this.nodeParentID = 0;
        this.nodeTypeID = 2;
        this.nodeContent = nodeContent;
    }
    
    /**
     * <p>Creates a Node with :<br>
     * - <b>nodeUserID</b>     : The ID of the User of the Node.<br>
     * - <b>nodeParentID</b>   : The ID of the Node Parent that contains the Node to be created.<br>
     * - <b>nodeTypeID</b>     : The type of the Node. (by the business convention, 1 means a <i>Folder</i> and 2 means a <i>Word file</i> <br>
     * - <b>nodeName</b>       : The name of the new Node<br>
     * So the constructor is only used to <b>create a new node</b>.</p>
     * @param nodeUserID    The ID of the User.
     * @param nodeParentID  The ID of the Node Parent.
     * @param nodeName      The name of the Node.
     * @param nodeTypeID    The type of the Node (<i>Folder</i> or <i>Word file</i>).
     */
    public Node(int nodeUserID, int nodeParentID, int nodeTypeID, String nodeName) {
        this.nodeUserID = nodeUserID;
        this.nodeParentID = nodeParentID;
        this.nodeTypeID = nodeTypeID;
        this.nodeName = nodeName;
    }
    
    /**
     * <p>Creates a Node with the its ID, Name and user ID.
     * So the constructor is only used to <b>rename</b> a node.</p>
     * @param nodeUserID    The ID of the User.
     * @param nodeID        The ID of the Node.
     * @param nodeName      The name of the Node.
     */
    public Node(int nodeUserID, int nodeID, String nodeName) {
        this.nodeUserID = nodeUserID;
        this.nodeID = nodeID;
        this.nodeName = nodeName;
    }
    
    /**
     * <p>Creates a Node with the its ID and its user ID.
     * So the constructor is only used to <b>delete</b> a node or <b>read the content</b> of a node.</p>
     * @param nodeUserID    The ID of the User.
     * @param nodeID        The ID of the Node.
     */
    public Node(int nodeUserID, int nodeID) {
        this.nodeUserID = nodeUserID;
        this.nodeID = nodeID;
    }
    
    public Node() { }

    public static NodeStructureDTO getDtoFromNode(Node node) {
        NodeStructureDTO nodeDTO = new NodeStructureDTO();
        nodeDTO.setId(node.getNodeID());
        nodeDTO.setParentId(node.getNodeParentID());
        nodeDTO.setType(node.getNodeTypeID());
        nodeDTO.setName(node.getNodeName());
        nodeDTO.setCompletePath(node.getNodeCompletePath());
        nodeDTO.setContent(node.getNodeContent());
        return nodeDTO;
    }
    
    public static NodeStructureDTO getDTOfromList(List<Node> list) {
        NodeStructureDTO rootNode = null;
        NodeStructureDTO currentNode;
        if(list.size()>0) {
             rootNode = getDtoFromNode(list.get(0));
             currentNode = rootNode;
             
             for(int i = 1, n=list.size(); i<n; i++) {
                 NodeStructureDTO newNodeDTO = getDtoFromNode(list.get(i));
                 currentNode.setNextNode(newNodeDTO);
                 currentNode = newNodeDTO;
            }
        }
        return rootNode;
    }

    public int getNodeUserID() {
        return nodeUserID;
    }

    public int getNodeID() {
        return nodeID;
    }

    public int getNodeParentID() {
        return nodeParentID;
    }

    public int getNodeTypeID() {
        return nodeTypeID;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getNodeCompletePath() {
        return nodeCompletePath;
    }

    public String getNodeContent() {
        return nodeContent;
    }

    public int getLeftIndex() {
        return leftIndex;
    }

    public int getRightIndex() {
        return rightIndex;
    }

    public void setNodeUserID(int nodeUserID) {
        this.nodeUserID = nodeUserID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    public void setNodeParentID(int nodeParentID) {
        this.nodeParentID = nodeParentID;
    }

    public void setNodeTypeID(int nodeTypeID) {
        this.nodeTypeID = nodeTypeID;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setNodeCompletePath(String nodeCompletePath) {
        this.nodeCompletePath = nodeCompletePath;
    }

    public void setNodeContent(String nodeContent) {
        this.nodeContent = nodeContent;
    }

    public void setLeftIndex(int leftIndex) {
        this.leftIndex = leftIndex;
    }

    public void setRightIndex(int rightIndex) {
        this.rightIndex = rightIndex;
    }
}
