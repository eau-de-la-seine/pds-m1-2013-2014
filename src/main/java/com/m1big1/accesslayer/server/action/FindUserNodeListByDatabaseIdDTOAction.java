package com.m1big1.accesslayer.server.action;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.m1big1.accesslayer.server.dao.DAOFactory;
import com.m1big1.accesslayer.server.dto.FindUserNodeListByDatabaseIdDTO;
import com.m1big1.accesslayer.server.model.Node;

public class FindUserNodeListByDatabaseIdDTOAction extends Action<FindUserNodeListByDatabaseIdDTO> {
    private static final Logger logger = Logger.getLogger(FindUserNodeListByDatabaseIdDTOAction.class);
    
    public FindUserNodeListByDatabaseIdDTOAction(FindUserNodeListByDatabaseIdDTO dtoInput) {
        this.dtoInput = dtoInput;
    }
    /**
     * <p>This method finds all the node of a user in the database with its user ID.</p>
     * @param findNodeListDTO                   <p>The DTO input with the user ID.</p>
     * @return FindUserNodeListByDatabaseIdDTO  <p>The DTO output with all the Node Information.</p>
     * @throws IOException                      <p>The properties file can not be read.</p>
     * @throws SQLException                     <p>The Connection vers MySQL failed</p>
     */
    public FindUserNodeListByDatabaseIdDTO execute() throws SQLException {
        FindUserNodeListByDatabaseIdDTO findNodeListDTO = dtoInput;
        logger.info("The user with ID : " + findNodeListDTO.getUserDatabaseId()  + 
                " finds its nodes.");
        findNodeListDTO.setRootNode(Node.getDTOfromList(DAOFactory.getNodeDAO().findAllByUserID(findNodeListDTO.getUserDatabaseId())));
        return findNodeListDTO;
    }
}
