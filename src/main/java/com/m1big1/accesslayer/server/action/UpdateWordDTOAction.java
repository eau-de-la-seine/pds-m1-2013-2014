package com.m1big1.accesslayer.server.action;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.m1big1.accesslayer.server.dao.DAOFactory;
import com.m1big1.accesslayer.server.dto.UpdateWordDTO;
import com.m1big1.accesslayer.server.model.Node;

public class UpdateWordDTOAction extends Action<UpdateWordDTO> {
    private static final Logger logger = Logger.getLogger(UpdateWordDTOAction.class);
    
    public UpdateWordDTOAction(UpdateWordDTO dtoInput) {
        this.dtoInput = dtoInput;
    }
    /**
     * <p>The method renames a Node in the database and return a DTO result.</p>
     * @param createNodeDTO                     <p>The DTO input.</p>
     * @return CreateNodeDTO                    <p>The DTO output.</p>
     * @throws IOException                      <p>The properties file can not be read.</p>
     * @throws SQLException                     <p>The Connection vers MySQL failed</p>
     */
    public UpdateWordDTO execute() throws SQLException {
        UpdateWordDTO updateWordDTO = dtoInput;
        logger.info("The user " + updateWordDTO.getUserDatabaseId()  + 
                " updates the content of the Word file with ID : " + updateWordDTO.getNodeId() +" .");
        int userID = updateWordDTO.getUserDatabaseId();
        int nodeID = updateWordDTO.getNodeId();
        String newContent = updateWordDTO.getContent();
        
        Node node = new Node(userID, nodeID, "", newContent);
        
        switch(updateWordDTO.getStep()) {
            case 1:
                updateWordDTO.setSuccess(false);
                if(updateWordDTO.getUserDatabaseId() == node.getNodeUserID()) {
                    if(node.getNodeTypeID() == 2) {
                        updateWordDTO.setSuccess(DAOFactory.getNodeDAO().checkNode(node));
                    }
                }
                break;
            case 2:
                updateWordDTO.setSuccess(DAOFactory.getNodeDAO().update(node));
                break;
            default:
                break;
        }
        return updateWordDTO;
    }
}
