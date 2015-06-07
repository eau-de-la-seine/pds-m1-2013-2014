package com.m1big1.accesslayer.server.action;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.m1big1.accesslayer.server.dao.DAOFactory;
import com.m1big1.accesslayer.server.dao.NodeDAO;
import com.m1big1.accesslayer.server.dto.CreateNodeDTO;
import com.m1big1.accesslayer.server.model.Node;

public class CreateNodeDTOAction extends Action<CreateNodeDTO> {
    private static final Logger logger = Logger.getLogger(CreateNodeDTOAction.class);
    
    public CreateNodeDTOAction(CreateNodeDTO dtoInput) {
        this.dtoInput = dtoInput;
    }
    @Override
    /**
     * <p>The method creates a Node in the database and return a DTO result.</p>
     * @param createNodeDTO                     <p>The DTO input with the user ID, the name and the type of the new node.</p>
     * @return CreateNodeDTO                    <p>The DTO output. If the attribute "success" is false, the new node can not be created.</p>
     * @throws IOException                      <p>The properties file can not be read.</p>
     * @throws SQLException                     <p>The Connection vers MySQL failed</p>
     */
    public CreateNodeDTO execute() throws SQLException {
        CreateNodeDTO createNodeDTO = dtoInput;
        logger.info("The user " + createNodeDTO.getUserDatabaseId()  + 
                " creates a node with name : " + createNodeDTO.getNodeName() +" .");
        NodeDAO nodeDAO = DAOFactory.getNodeDAO();
        Node node = new Node(createNodeDTO.getUserDatabaseId(), createNodeDTO.getParentFolderId(), createNodeDTO.getNodeType(), createNodeDTO.getNodeName());
        switch(createNodeDTO.getStep()) {
            case 1: createNodeDTO.setSuccess(nodeDAO.checkParent(node)); break;
            case 2: createNodeDTO.setSuccess(nodeDAO.checkDupplicatedName(node)); break;
            case 3: int id = nodeDAO.create(node);
                    if(id != 0) {
                        createNodeDTO.setNodeId(id);
                        createNodeDTO.setSuccess(true);
                    }
                    break;
            default: break;
        }
        return createNodeDTO;
    }
}
