package com.m1big1.accesslayer.server.action;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.m1big1.accesslayer.server.dao.DAOFactory;
import com.m1big1.accesslayer.server.dto.FindDatabaseIdByLoginPasswordDTO;
import com.m1big1.accesslayer.server.model.User;

public class FindDatabaseIdByLoginPasswordDTOAction extends Action<FindDatabaseIdByLoginPasswordDTO>  {
    private static final Logger logger = Logger.getLogger(FindDatabaseIdByLoginPasswordDTOAction.class);
    
    public FindDatabaseIdByLoginPasswordDTOAction(FindDatabaseIdByLoginPasswordDTO dtoInput) {
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
    public FindDatabaseIdByLoginPasswordDTO execute() throws SQLException {
        FindDatabaseIdByLoginPasswordDTO findUserDTO = dtoInput;
        logger.info("Finding database ID with its login " + findUserDTO.getLogin() + " and the password.");
        User user = DAOFactory.getUserDAO().find(findUserDTO.getLogin(), findUserDTO.getPassword());
        if (user != null) {
            findUserDTO.setUserDatabaseId(user.getUserID());
        }
        return findUserDTO;
    }
}
