package com.m1big1.accesslayer.server.dao;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

/**
 * <p>DAO (Data Access Object) is an interface of Data Access Layer that allows user to access database automatically.</p>
 * @author NGUYEN Huu Khuong
 * @category DAO Pattern
 */
public abstract class DAO<T> {
    protected DataSource dataSource;
    
    public DAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    /**
     * <p>The method to create an object in the database.</p>
     * @param  object       <p>The object to be created in the database.</p> 
     * @return              <p>Return the integer ID of the new object in the database.</p>
     * @throws SQLException <p>SQL Exception comes when the connection to the database can not be etablished.</p>
     */
    public abstract int create(T object) throws SQLException;
    
    /**
     * <p>The method to delete an object in the database.</p>
     */
    public abstract boolean delete(T obj) throws SQLException;
    
    /**
     * <p>The method to update an object in the database.</p>
     */
    public abstract boolean update(T obj) throws SQLException;
    
    /**
     * <p>The method to find an object in the database.</p>
     */
    public abstract T find(int id) throws SQLException;
    
    /**
     * <p>The method to find all the object in the database.</p>
     */
    public abstract List<T> findAll() throws SQLException;
}