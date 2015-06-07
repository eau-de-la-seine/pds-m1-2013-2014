package com.m1big1.accesslayer.server.action;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.m1big1.accesslayer.server.dao.DAOFactory;
import com.m1big1.accesslayer.server.dto.ReadWordDTO;
import com.m1big1.accesslayer.server.model.Node;

public class ReadWordDTOAction extends Action<ReadWordDTO> {
    private static final Logger logger = Logger.getLogger(ReadWordDTOAction.class);
    
    public ReadWordDTOAction(ReadWordDTO dtoInput) {
        this.dtoInput = dtoInput;
    }
    
    /**
     * <p>This method find a user in the database with its login and password and return its user ID.</p>
     * <p>If the user exists, the user ID will be superior than 0.</p>
     * <p>If the user doesn't exists, the user ID will be 0.</p>
     * @param findUserDTO                       <p>The DTO input with the login and the password.</p>
     * @return FindDatabaseIdByLoginPasswordDTO <p>The DTO output with the user ID.</p>
     * @throws IOException                      <p>The properties file can not be read.</p>
     * @throws SQLException                     <p>The Connection vers MySQL failed</p>
     */
    public ReadWordDTO execute() throws SQLException {
        ReadWordDTO readWordDTO = dtoInput;
        logger.info("The user " + readWordDTO.getUserDatabaseId()  + 
                " finds the content of the Word file with ID : " + readWordDTO.getNodeId() + " .");
        Node node = DAOFactory.getNodeDAO().find(readWordDTO.getNodeId());
        switch(readWordDTO.getStep()) {
            case 1:
                readWordDTO.setSuccess(false);
                if(readWordDTO.getUserDatabaseId() == node.getNodeUserID()) {
                    if(node.getNodeTypeID() == 2) {
                        readWordDTO.setSuccess(DAOFactory.getNodeDAO().checkParent(node));
                    }
                }
                break;
            case 2:
                readWordDTO.setContent(node.getNodeContent());
                readWordDTO.setSuccess(true);
                break;
            default: break;
        }
        return readWordDTO;
    }
}
