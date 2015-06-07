package com.m1big1.accesslayer.server.action;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.m1big1.accesslayer.server.dao.DAOFactory;
import com.m1big1.accesslayer.server.dto.RenameNodeDTO;
import com.m1big1.accesslayer.server.model.Node;

public class RenameNodeDTOAction extends Action<RenameNodeDTO> {
    private static final Logger logger = Logger.getLogger(RenameNodeDTOAction.class);
    
    public RenameNodeDTOAction(RenameNodeDTO dtoInput) {
        this.dtoInput = dtoInput;
    }
    
    /**
     * <p>The method renames a Node in the database and return a DTO result.</p>
     * @param createNodeDTO                     <p>The DTO input.</p>
     * @return CreateNodeDTO                    <p>The DTO output.</p>
     * @throws IOException                      <p>The properties file can not be read.</p>
     * @throws SQLException                     <p>The Connection vers MySQL failed</p>
     */
    public RenameNodeDTO execute() throws SQLException {
        RenameNodeDTO renameNodeDTO = dtoInput;
        logger.info("The user " + renameNodeDTO.getUserDatabaseId()  + 
                " deletes the node with ID " + renameNodeDTO.getNodeId() +" .");
        int userID = renameNodeDTO.getUserDatabaseId();
        int nodeID = renameNodeDTO.getNodeId();
        String nodeNewName = renameNodeDTO.getName();
        
        Node node = new Node(userID, nodeID, nodeNewName);
        
        switch(renameNodeDTO.getStep()) {
            case 1: renameNodeDTO.setSuccess(DAOFactory.getNodeDAO().checkNodeRename(node)); break;
            case 2: renameNodeDTO.setSuccess(DAOFactory.getNodeDAO().rename(node)); break;
            default: break;
        }
        return renameNodeDTO;
    }

}
