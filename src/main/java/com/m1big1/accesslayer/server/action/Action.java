package com.m1big1.accesslayer.server.action;

import java.sql.SQLException;

import com.m1big1.accesslayer.server.dto.*;

/**
 * <p>The class Action treats a DTO given by the server and return a DTO result.
 * All the methods in the class have the same name "execute".
 * The method execute an action by the type of the DTO.<br>
 * Supported DTO is :<br>
 * - <b>FindDatabaseIdByLoginPasswordDTO</b>    : DTO allows to find a database user ID by the login and the password.<br>
 * - <b>FindUserNodeListByDatabaseIdDTO</b>     : DTO allows to find all the node list with the database user ID.<br>
 * - <b>CreateNodeDTO</b>                       : DTO allows to create a node with the information of the node.<br>
 * - <b>DeleteNodeDTO</b>                       : DTO allows to delete a node with the information of the node.<br>
 * - <b>RenameNodeDTO</b>                       : DTO allows to rename a node with the information of the node.<br>
 * - <b>SearchWordDTO</b>                       : DTO allows .<br>
 * - <b>HistoryDTO</b>                          : DTO allows .<br>
 * @author Huu Khuong NGUYEN
 * @category Access Core
 */
public abstract class Action<T extends DTO> {
    protected T dtoInput;
    
    /**
     * <p>The method is abstract and must be redefinied in each child class.<br>
     * The method will treat the DTO entered and return the DTO return.</p>
     * @return method returns a dto corresponds to the message output. 
     * @throws SQLException if the program can not connect to the SQL Server
     */
    public abstract T execute() throws SQLException ;
}
