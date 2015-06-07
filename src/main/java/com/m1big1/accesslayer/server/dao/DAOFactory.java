package com.m1big1.accesslayer.server.dao;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class DAOFactory {
    private static Logger logger = Logger.getLogger(DAOFactory.class);
    private static DataSource dataSource;
    
    static {
        try {
            dataSource = PoolConnection.getInstance();
        } catch(SQLException e) {
            logger.fatal("Connection Pool not work", e);
        }
    }
    
    private DAOFactory() { }
    
    public static HistoryDAO getLogDAO() {
        return new HistoryDAO(dataSource);
    }
    public static NodeDAO getNodeDAO() {
        return new NodeDAO(dataSource);
    }
    public static UserDAO getUserDAO() {
        return new UserDAO(dataSource);
    }
}
