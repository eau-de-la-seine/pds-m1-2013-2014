package com.m1big1.accesslayer.server.action;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.m1big1.accesslayer.server.dao.DAOFactory;
import com.m1big1.accesslayer.server.dto.SearchWordDTO;
import com.m1big1.accesslayer.server.model.Node;

public class SearchWordDTOAction extends Action<SearchWordDTO> {
    private static final Logger logger = Logger.getLogger(SearchWordDTOAction.class);
    
    public SearchWordDTOAction(SearchWordDTO dtoInput) {
        this.dtoInput = dtoInput;
    }
    /**
     * <p>The method renames a Node in the database and return a DTO result.</p>
     * @param createNodeDTO                     The DTO input.
     * @return CreateNodeDTO                    The DTO output.
     * @throws IOException                      The properties file can not be read.
     * @throws SQLException                     The Connection vers MySQL failed
     */
    public SearchWordDTO execute() throws SQLException {
        SearchWordDTO searchWordDTO = dtoInput;
        logger.info("The user " + searchWordDTO.getUserDatabaseId()  + 
                " searchs a Word file with name : " + searchWordDTO.getSearchName() +" .");
        searchWordDTO.setRootNode(Node.getDTOfromList(DAOFactory.getNodeDAO().find(searchWordDTO.getUserDatabaseId(), searchWordDTO.getSearchName())));
        return searchWordDTO;
    }
}
