package com.m1big1.accesslayer.server.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.m1big1.accesslayer.server.model.Node;

/**
 * <p>NodeDAO (Node Data Access Object) is an object of Data Access Layer that allows user to access database automatically.</p>
 * <p>The objects to be accessed have the type Node.</p>
 * @author      NGUYEN Huu Khuong
 * @category    DAO Pattern
 */
public class NodeDAO extends DAO<Node> {
    private static final Logger logger = Logger.getLogger(NodeDAO.class);
    
    private static final String SQL_PROPERTIES_FILE = "configuration/sqlNodeDAO.properties";
    
    private static final int CREATE = 1;
    private static final int DELETE = 2;
    private static final int UPDATE = 3;
    private static final int RENAME = 4;
    
    private static String SELECT_NODES_getNodeFullPath;
    private static String SELECT_NODES_getNodeByUserID;
    private static String SELECT_NODES_SearchWord;
    private static String SELECT_NODES_find_byID;
    
    private static String UPDATE_NODES_rename;

    private static String DELETE_NODES_delete;
    private static String UPDATE_NODES_left_index;
    private static String UPDATE_NODES_right_index;

    private static String SELECT_checkNodeRename;
    private static String SELECT_checkNode;
    private static String SELECT_NODES_leftindex_create;
    private static String UPDATE_NODES_leftIndex_createNode;
    private static String SELECT_NODES_checkParent_createNode;
    private static String UPDATE_NODES_rightIndex_createNode;
    private static String INSERT_NODES_createNode;
    private static String SELECT_NODES_createNode;
    private static String SELECT_NODES_duplicatedName_createNode;
    private static String UPDATE_NODES_changeContent;
    
    
    static {
        try {
            InputStream propertiesFile      = Thread.currentThread().getContextClassLoader().getResourceAsStream(SQL_PROPERTIES_FILE);
            Properties  properties          = new Properties();
            properties.load(propertiesFile);
            propertiesFile.close();
            
            
            SELECT_NODES_getNodeFullPath            = properties.getProperty("SELECT_NODES_getNodeFullPath");
            SELECT_NODES_getNodeByUserID            = properties.getProperty("SELECT_NODES_getNodeByUserID");
            SELECT_NODES_SearchWord                 = properties.getProperty("SELECT_NODES_SearchWord");
            SELECT_NODES_find_byID                  = properties.getProperty("SELECT_NODES_find_byID");
            
            UPDATE_NODES_rename                     = properties.getProperty("UPDATE_NODES_rename");
            DELETE_NODES_delete                     = properties.getProperty("DELETE_NODES_delete");
            
            UPDATE_NODES_left_index                 = properties.getProperty("UPDATE_NODES_left_index");
            UPDATE_NODES_right_index                = properties.getProperty("UPDATE_NODES_right_index");
            SELECT_checkNodeRename                  = properties.getProperty("SELECT_checkNodeRename");
            SELECT_checkNode                        = properties.getProperty("SELECT_checkNode");
            
            SELECT_NODES_leftindex_create           = properties.getProperty("SELECT_NODES_leftindex_create");
            UPDATE_NODES_leftIndex_createNode       = properties.getProperty("UPDATE_NODES_leftIndex_createNode");
            UPDATE_NODES_rightIndex_createNode      =  properties.getProperty("UPDATE_NODES_rightIndex_createNode");
            SELECT_NODES_checkParent_createNode     =  properties.getProperty("SELECT_NODES_checkParent_createNode");
            INSERT_NODES_createNode                 = properties.getProperty("INSERT_NODES_createNode");
            SELECT_NODES_createNode                 = properties.getProperty("SELECT_NODES_createNode");
            SELECT_NODES_duplicatedName_createNode  = properties.getProperty("SELECT_NODES_duplicatedName_createNode");
            
            UPDATE_NODES_changeContent              = properties.getProperty("UPDATE_NODES_changeContent");
            
        } catch(IOException e) {
            logger.error("The Configuration file can not be loaded. The DAO can not find its SQLQuery.", e);
        }
    }
    

    public NodeDAO(DataSource dataSource) {
        super(dataSource);
    }
    
    //Create Node
        /**
         * <p>The method check if the Node parent is valid (parent node type is a folder and it belongs to the user who wants to create the new node).</p>
         * @param node          <p>The node to be created in the database.</p>
         * @return boolean      <p>The result (boolean) means if the program can create a node.</p>
         * @throws SQLException <p>The SQLException means there is a problem with SQL Connection.</p>
         */
        public boolean checkParent(Node node) throws SQLException {
            boolean valid           = false;
            int     fileParentID    = node.getNodeParentID();
            int     userID          = node.getNodeUserID();

            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_NODES_checkParent_createNode);
            ps.setInt(1, userID);
            ps.setInt(2, fileParentID);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                valid = rs.getBoolean(1);
            }
            rs.close();
            ps.close();
            conn.close();
            return valid;
        }
        
        /**
         * <p>The method check if the Node parent is valid (there is no file with the same type and the same name with the new node).</p>
         * @param node          <p>The node to be created in the database.</p>
         * @return boolean      <p>The result (boolean) means if the program can create a node.</p>
         * @throws SQLException <p>The SQLException means there is a problem with SQL Connection.</p>
         */
        public boolean checkDupplicatedName(Node node) throws SQLException {
            boolean valid           = false;
            int     fileParentID    = node.getNodeParentID();
            int     userID          = node.getNodeUserID();
            int     fileType        = node.getNodeTypeID();
            String  fileName        = node.getNodeName();

            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(SELECT_NODES_duplicatedName_createNode);
            ps.setInt(1, userID);
            ps.setInt(2, fileParentID);
            ps.setInt(3, fileType);
            ps.setString(4, fileName);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                valid = rs.getBoolean(1);
            }
            rs.close();
            ps.close();
            conn.close();
            return valid;
        }
        
        /**
         * <p>The method create the node in the database.</p>
         * @param node          <p>The node to be created in the database.</p>
         * @return boolean      <p>The result (boolean) means if the program can create a node.</p>
         * @throws SQLException <p>The SQLException means there is a problem with SQL Connection.</p>
         */
        @Override
        public int create(Node node) throws SQLException {
            int     parentLeft      = 0;
            int     fileParentID    = node.getNodeParentID();
            int     fileType        = node.getNodeTypeID();
            int     userID          = node.getNodeUserID();
            String  fileName        = node.getNodeName();
            
            Connection conn = dataSource.getConnection();
            PreparedStatement ps;
            ResultSet rs;
            
            ps = conn.prepareStatement(SELECT_NODES_leftindex_create);
            ps.setInt(1, fileParentID);
            rs = ps.executeQuery();
            if (rs.next()) {
                parentLeft = rs.getInt("left_index");
            }

            ps = conn.prepareStatement(UPDATE_NODES_leftIndex_createNode);
            ps.setInt(1, parentLeft);
            ps.setInt(2, userID);
            ps.executeUpdate();
            
            ps = conn.prepareStatement(UPDATE_NODES_rightIndex_createNode);
            ps.setInt(1, parentLeft);
            ps.setInt(2, userID);
            ps.executeUpdate();
            
            ps = conn.prepareStatement(INSERT_NODES_createNode);
            ps.setInt(1, fileParentID);
            ps.setString(2, fileName);
            ps.setInt(3, userID);
            ps.setInt(4, fileType);
            ps.setInt(5, parentLeft+1);
            ps.setInt(6, parentLeft+2);
            ps.executeUpdate();
            
            ps = conn.prepareStatement(SELECT_NODES_createNode);
            rs = ps.executeQuery();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            
            Node newNode = find(id);
            
            DAOFactory.getLogDAO().insertLog(userID, CREATE, getFullPath(newNode.getNodeID()));

            rs.close();
            ps.close();
            conn.close();
            return id;
        }
    //End of create Node.
        
    /**
     * <p>The method check if the node belongs to the user.</p>
     * @param node          <p>The node to be checked.</p>
     * @return              <p>The result (boolean) means that the node belons to the user.</p>
     * @throws SQLException <p>The SQLException means there is a problem with SQL Connection.</p>
     */
    public boolean checkNode(Node node) throws SQLException {
        boolean valid   = false;
        int     fileID  = node.getNodeID();
        int     userID  = node.getNodeUserID();

        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(SELECT_checkNode);
        ps.setInt(1, userID);
        ps.setInt(2, fileID);
        
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            valid = rs.getBoolean(1);
        }
        rs.close();
        ps.close();
        conn.close();
        return valid;
    }

    /**
     * <p>The method check if the program can rename a node.</p>
     * @param node          <p>The node to be checked.</p>
     * @return              <p>The result (boolean) means that the node can be change.</p>
     * @throws SQLException <p>The SQLException means there is a problem with SQL Connection.</p>
     */
    public boolean checkNodeRename(Node node) throws SQLException {
        boolean valid   = false;
        int fileID      = node.getNodeID();
        int userID      = node.getNodeUserID();
        String fileName = node.getNodeName();
        
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(SELECT_checkNodeRename);
        ps.setInt(1, userID);
        ps.setInt(2, fileID);
        ps.setString(3, fileName);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            valid = rs.getBoolean("value");
        }
        rs.close();
        ps.close();
        conn.close();
        return valid;
    }

    //Delete Node
        /**
         * <p>The method check if the node belongs to the user.</p>
         * @param node          <p>The node to be deleted.</p>
         * @return              <p>The result (boolean) means that the node is deleted.</p>
         * @throws SQLException <p>The SQLException means there is a problem with SQL Connection.</p>
         */
        @Override
        public boolean delete(Node node) throws SQLException {
            int     userID          = node.getNodeUserID();
            int     fileID          = node.getNodeID();
            String  nodeName        = getFullPath(fileID);
            Node    nodeFullDesc    = find(fileID);
            int     leftIndex       = nodeFullDesc.getLeftIndex();
            int     rightIndex      = nodeFullDesc.getRightIndex();
            int     different       = rightIndex - leftIndex + 1;
            
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(DELETE_NODES_delete);
            ps.setInt(1, userID);
            ps.setInt(2, leftIndex);
            ps.setInt(3, rightIndex);
            ps.executeUpdate();
            
            //Left Index
            ps = conn.prepareStatement(UPDATE_NODES_left_index);
            ps.setInt(1, different);
            ps.setInt(2, userID);
            ps.setInt(3, leftIndex);
            ps.executeUpdate();
            
            ps = conn.prepareStatement(UPDATE_NODES_right_index);
            ps.setInt(1, different);
            ps.setInt(2, userID);
            ps.setInt(3, rightIndex);
            ps.executeUpdate();

            DAOFactory.getLogDAO().insertLog(userID, DELETE, nodeName);

            ps.close();
            conn.close();
            return true;
        }

    //End Delete Node
        /**
         * <p>The method renames a node.</p>
         * @param node          <p>The node to be renamed.</p>
         * @return              <p>The result (boolean) means that the node is renamed.</p>
         * @throws SQLException <p>The SQLException means there is a problem with SQL Connection.</p>
         */
    public boolean rename(Node node) throws SQLException {
        int     userID      = node.getNodeUserID();
        int     fileID      = node.getNodeID();
        String  nodeName    = getFullPath(fileID);
        String  nodeNewName = node.getNodeName();

        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(UPDATE_NODES_rename);
        
        ps.setString(1, nodeNewName);
        ps.setInt(2, userID);
        ps.setInt(3, fileID);
        ps.executeUpdate();
        
        DAOFactory.getLogDAO().insertLog(userID, RENAME, nodeName);
        
        ps.close();
        conn.close();
        return true;
    }
    
    @Override
    public boolean update(Node node) throws SQLException {
        int     userID          = node.getNodeUserID();
        int     fileID          = node.getNodeID();
        String  nodeName        = getFullPath(fileID);
        String  nodeNewContent  = node.getNodeContent();

        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(UPDATE_NODES_changeContent);
        ps.setString(1, nodeNewContent);
        ps.setInt(2, userID);
        ps.setInt(3, fileID);
        ps.executeUpdate();
        
        DAOFactory.getLogDAO().insertLog(userID, UPDATE, nodeName);
        ps.close();
        conn.close();
        return true;
    }

    /**
     * <p>The method finds a node with its id.</p>
     * @param id            <p>The id of the node.</p>
     * @return              <p>The result is the Node to be found.</p>
     * @throws SQLException <p>The SQLException means there is a problem with SQL Connection.</p>
     */
    @Override
    public Node find(int id) throws SQLException {
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(SELECT_NODES_find_byID);
        ps.setInt(1, id);
        
        ResultSet rs = ps.executeQuery();
        Node node = null;
        if(rs.next()) {
            node = new Node(rs.getInt("fk_id_user"), rs.getInt("id_file"),
                    rs.getInt("id_file_parent"), rs.getInt("fk_id_type"),
                    rs.getString("file_name"), rs.getString("file_content"),
                    rs.getInt("left_index"), rs.getInt("right_index"));
        }
        rs.close();
        ps.close();
        conn.close();
        return node;
    }

    @Override
    public List<Node> findAll() {
        return null;
    }

    /**
     * <p>This method finds all the node belongs to an user.</p>
     * @param userID        <p>The ID of the user.</p>
     * @return List<Node>   <p>The list of node of the user.</p>
     * @throws SQLException <p>The SQLException means there is a problem with SQL Connection.</p>
     */
    public List<Node> findAllByUserID(int userID) throws SQLException {
        List<Node> list = new ArrayList<Node>();
        Connection conn = dataSource.getConnection();
        PreparedStatement ps  = conn.prepareStatement(SELECT_NODES_getNodeByUserID);
        ps.setInt(1, userID);
        ResultSet rs = ps.executeQuery();
        Node node;
        while (rs.next()) {
            node = new Node(rs.getInt("fk_id_user"), rs.getInt("id_file"),
                    rs.getInt("id_file_parent"), rs.getInt("fk_id_type"),
                    rs.getString("file_name"), rs.getString("file_content"),
                    rs.getInt("left_index"), rs.getInt("right_index"));
            list.add(node);
        }
        rs.close();
        ps.close();
        conn.close();
        return list;
    }
    
    /**
     * <p>Search all the Word belong to a User and have a Keyword in the name.</p>
     * @param userID        The ID of the user.
     * @param keyword       The keyword to be search.
     * @return              A list of Word files.
     * @throws SQLException if there is a problem with the connection to the database MySQL.
     */
    public List<Node> find(int userID, String keyword) throws SQLException {
        List<Node> list = new ArrayList<Node>();
        Connection conn = dataSource.getConnection();
        PreparedStatement ps  = conn.prepareStatement(SELECT_NODES_SearchWord);
        ps.setInt(1, Node.WORD);
        ps.setInt(2, userID);
        ps.setString(3, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();
        Node node;
        while (rs.next()) {
            node = new Node();
            node.setNodeID(rs.getInt(1));
            node.setNodeCompletePath(rs.getString(2));
            list.add(node);
        }
        rs.close();
        ps.close();
        conn.close();
        return list;
    }
    
    public String getFullPath(int nodeID) throws SQLException {
        String fullPath ="";
        
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(SELECT_NODES_getNodeFullPath);
        ps.setInt(1, nodeID);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            fullPath = rs.getString(1);
        }
        
        rs.close();
        ps.close();
        conn.close();
        return fullPath;
        
        //SELECT_NODES_getNodeFullPath
        
        /*
        String fullPath = "";
        Node node = find(nodeID);
        if(node!= null) {
            fullPath = node.getNodeName();
        }
        while(node.getNodeParentID() > 0) {
            node = find(node.getNodeParentID());
            fullPath = node.getNodeName() + "/" + fullPath;
        }*/
    }
}