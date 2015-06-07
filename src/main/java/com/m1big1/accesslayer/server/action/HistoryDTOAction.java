package com.m1big1.accesslayer.server.action;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.m1big1.accesslayer.server.dao.DAOFactory;
import com.m1big1.accesslayer.server.dto.HistoryDTO;
import com.m1big1.accesslayer.server.model.HistoryEvent;

public class HistoryDTOAction extends Action<HistoryDTO> {
    private static final Logger logger = Logger.getLogger(HistoryDTOAction.class);
    
    public HistoryDTOAction(HistoryDTO dtoInput) {
        this.dtoInput = dtoInput;
    }
    
    public HistoryDTO execute() throws SQLException {
        HistoryDTO historyDTO = dtoInput;
        logger.info("The user " + historyDTO.getUserDatabaseId()  + 
                " is looking for its history .");
        historyDTO.setRootHistoryEvent(HistoryEvent.getDTOfromList(DAOFactory.getLogDAO().findLogOfUser(historyDTO.getUserDatabaseId())));
        return historyDTO;
    }
}
