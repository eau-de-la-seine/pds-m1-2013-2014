package com.m1big1.accesslayer.server.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.m1big1.accesslayer.server.model.HistoryEvent;

/**
 * <p>The class LogDAO (Log Data Access Object) allows user to access database automatically via predefinied method.<br>
 * The Class will access the History to create or read a History.</p>
 * @author      NGUYEN Huu Khuong
 * @category    DAO Pattern
 */
public class HistoryDAO extends DAO<HistoryEvent> {
    private static final    Logger logger = Logger.getLogger(HistoryDAO.class);
    private static final    String SQL_PROPERTIES_FILE = "configuration/sqlHistoryDAO.properties";
    
    private static          String INSERT_LOG_insertLog;
    private static          String SELECT_LOG_findLog;

    static {
        try {
            InputStream propertiesFile      = Thread.currentThread().getContextClassLoader().getResourceAsStream(SQL_PROPERTIES_FILE);
            Properties  properties          = new Properties();
            properties.load(propertiesFile);
            propertiesFile.close();
            
            INSERT_LOG_insertLog    = properties.getProperty("INSERT_LOG_insertLog");
            SELECT_LOG_findLog      = properties.getProperty("SELECT_LOG_findLog");
        } catch(IOException e) {
            logger.error("The configuration file " + SQL_PROPERTIES_FILE + " can not be loaded. The DAO can not find its SQLQuery.", e);
        }
    }
    
    /**
     * <p>Create an object <b>HistoryDAO</b> with the datasource. The datasource comes from a <b>Connection Pool</b></p>
     * @param dataSource The <b>DataSource</b> contains information about the SQL Connection that allows the DAO to connect to the right SQL Server.
     */
    public HistoryDAO(DataSource dataSource) {
        super(dataSource);
    }
    
    public void insertLog(int userID, int actionTypeID, String nodeName) throws SQLException {
        Connection          conn    = dataSource.getConnection();
        PreparedStatement   ps      = conn.prepareStatement(INSERT_LOG_insertLog);
        
        ps.setInt(1, userID);
        ps.setInt(2, actionTypeID);
        ps.setString(3, nodeName);
        ps.executeUpdate();
        
        ps.close();
        conn.close();
    }
    
    public List<HistoryEvent> findLogOfUser(int userID) throws SQLException {
        List<HistoryEvent> list = new ArrayList<HistoryEvent>();
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(SELECT_LOG_findLog);
        ps.setInt(1, userID);
        ResultSet rs = ps.executeQuery();
        HistoryEvent history;
        while (rs.next()) {
            history = new HistoryEvent(rs.getString(1), rs.getString(2), rs.getString(3));
            list.add(history);
        }

        rs.close();
        ps.close();
        conn.close();
        return list;
    }
    
    @Override
    public int create(HistoryEvent obj) throws SQLException {
        return 0;
    }

    @Override
    public boolean delete(HistoryEvent obj) throws SQLException {
        return false;
    }

    @Override
    public boolean update(HistoryEvent obj) throws SQLException {
        return false;
    }

    @Override
    public HistoryEvent find(int id) throws SQLException {
        return null;
    }

    @Override
    public List<HistoryEvent> findAll() throws SQLException {
        return null;
    }
}
