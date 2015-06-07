package com.m1big1.accesslayer.server.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import com.jolbox.bonecp.BoneCPDataSource;


public class PoolConnection {
    private static final    Logger      logger              = Logger.getLogger(PoolConnection.class);
    private static final    String      SQL_PROPERTIES_FILE = "configuration/sql.properties";
    
    private static          String      mySqlDriverName;
    private static          String      connectionString;
    private static          String      user;
    private static          String      password;
    private static          DataSource  dataSource;
    
    static {
        try {
            InputStream propertiesFile      = Thread.currentThread().getContextClassLoader().getResourceAsStream(SQL_PROPERTIES_FILE);
            Properties  properties          = new Properties();
            properties.load(propertiesFile);
            propertiesFile.close();

            mySqlDriverName     = properties.getProperty("mySqlDriverName");
            connectionString    = properties.getProperty("connectionString");
            user                = properties.getProperty("user");
            password            = properties.getProperty("password");
            
            
        } catch(IOException e) {
            logger.error("The properties file can not be loaded.", e);
        }
    }
    
    static {
        try {
            Class.forName(mySqlDriverName);
            BoneCPDataSource bds = new BoneCPDataSource();
            bds.setJdbcUrl(connectionString);
            bds.setUser(user);
            bds.setPassword(password);
            
            bds.setMinConnectionsPerPartition(5);
            bds.setMaxConnectionsPerPartition(10);
            bds.setPartitionCount(2);
            
            dataSource = bds;
        } catch (ClassNotFoundException e) {
            logger.fatal("Connection Pool can not load mySQL driver.", e);
        }
    }
    
    public static DataSource getInstance() throws SQLException {
        if(dataSource != null) {
            return dataSource;
        } else {
            throw new SQLException("DataSource does not exist");
        }
    }
    

}
