package com.m1big1.accesslayer.server.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.m1big1.accesslayer.server.model.User;

/**
 * UserDAO (User Data Access Object) is an object of Data Access Layer that allows user to access database automatically.
 * The objects to be accessed have the type User.
 * @author      NGUYEN Huu Khuong
 * @category    DAO Pattern
 */
public class UserDAO extends DAO<User> {
    private static final Logger logger = Logger.getLogger(UserDAO.class);
    private static final String SQL_PROPERTIES_FILE = "configuration/sqlUserDAO.properties";
    
    private static String INSERT_USERS_createUser;
    private static String DELETE_USERS_deleteUser;
    private static String UPDATE_USERS_updateUser;
    private static String SELECT_USERS_findUserWithLoginAndPassword;
    
    static {
        try {
            InputStream propertiesFile      = Thread.currentThread().getContextClassLoader().getResourceAsStream(SQL_PROPERTIES_FILE);
            Properties  properties          = new Properties();
            properties.load(propertiesFile);
            propertiesFile.close();
            
            INSERT_USERS_createUser                     = properties.getProperty("INSERT_USERS_createUser");
            DELETE_USERS_deleteUser                     = properties.getProperty("DELETE_USERS_deleteUser");
            UPDATE_USERS_updateUser                     = properties.getProperty("UPDATE_USERS_updateUser");
            SELECT_USERS_findUserWithLoginAndPassword   = properties.getProperty("SELECT_USERS_findUserWithLoginAndPassword");
        } catch(IOException e) {
            logger.error("The Configuration file can not be loaded. The DAO can not find its SQLQuery.", e);
        }
    }

    public UserDAO(DataSource dataSource) {
        super(dataSource);
    }
    
    @Override
    public int create(User user) throws SQLException {
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(INSERT_USERS_createUser);
        ps.setString(1, user.getUserLogin());
        ps.setString(2, user.getUserPassword());
        ps.setString(3, user.getUserFirstname());
        ps.setString(4, user.getUserLastname());
        ps.executeUpdate();
        ps.close();
        conn.close();
        return 0;
    }

    @Override
    public boolean delete(User user) throws SQLException {
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(DELETE_USERS_deleteUser);
        ps.setInt(1, user.getUserID());
        ps.executeUpdate();
        ps.close();
        conn.close();
        return true;
    }

    @Override
    public boolean update(User user) throws SQLException {
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(UPDATE_USERS_updateUser);
        ps.setString(1, user.getUserFirstname());
        ps.setString(2, user.getUserLastname());
        ps.setInt(3, user.getUserID());
        ps.executeUpdate();

        ps.close();
        conn.close();
        return true;
    }

    public User find(String login, String password) throws SQLException {
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(SELECT_USERS_findUserWithLoginAndPassword);
        ps.setString(1, login);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        User user = (rs.next()) ?
                new User(rs.getInt("id_user"), rs.getString("login"), rs.getString("password"), rs.getString("firstname"), rs.getString("lastname")) :
                null;

        rs.close();
        ps.close();
        conn.close();
        return user;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User find(int id) {
        return null;
    }
}