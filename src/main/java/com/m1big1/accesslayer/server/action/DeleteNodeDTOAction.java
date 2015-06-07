package com.m1big1.accesslayer.server.action;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.m1big1.accesslayer.server.dao.DAOFactory;
import com.m1big1.accesslayer.server.dto.DeleteNodeDTO;
import com.m1big1.accesslayer.server.model.Node;

public class DeleteNodeDTOAction extends Action<DeleteNodeDTO> {
    private static final Logger logger = Logger.getLogger(DeleteNodeDTOAction.class);
    
    public DeleteNodeDTOAction(DeleteNodeDTO dtoInput) {
        this.dtoInput = dtoInput;
    }
    /**
     * <p>The method deletes Node in the database and return a DTO result.</p>
     * @param createNodeDTO                     <p>The DTO input.</p>
     * @return CreateNodeDTO                    <p>The DTO output.</p>
     * @throws IOException                      <p>The properties file can not be read.</p>
     * @throws SQLException                     <p>The Connection vers MySQL failed</p>
     */
    public DeleteNodeDTO execute() throws SQLException {
        DeleteNodeDTO deleteNodeDTO = dtoInput;
        logger.info("The user " + deleteNodeDTO.getUserDatabaseId()  + 
                " deletes the node with ID : " + deleteNodeDTO.getNodeId() +" .");
        Node node = new Node(deleteNodeDTO.getUserDatabaseId(), deleteNodeDTO.getNodeId());
        switch(deleteNodeDTO.getStep()) {
            case 1: deleteNodeDTO.setSuccess(DAOFactory.getNodeDAO().checkNode(node)); break;
            case 2: deleteNodeDTO.setSuccess(DAOFactory.getNodeDAO().delete(node)); break;
            default: break;
        }
        return deleteNodeDTO;
    }
}
