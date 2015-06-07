package com.m1big1.accesslayer.server.model;

/**
 * User is a model replace a user with id, login, password, name, lastname.
 * @author      NGUYEN Huu Khuong
 * @category    Model
 */
public class User {
    private final   int     userID;
    private         String  userLogin;
    private         String  userPassword;
    private         String  userFirstname;
    private         String  userLastname;
    
    /**
     * <p>Creates an object User with all of its attributs possible:<br>
     * - ID.<br>
     * - Login.<br>
     * - Password.<br>
     * - Firstname.<br>
     * - Lastname.<p>
     * @param userID        The ID of the user. The ID can not be modified.
     * @param userLogin     The login of the user.
     * @param userPassword  The password of the user.
     * @param userFirstname The firstname of the user.
     * @param userLastname  The lastname of the user.
     */
    public User(int userID, String userLogin, String userPassword, String userFirstname, String userLastname) {
        this.userID = userID;
        this.userLogin = userLogin;
        this.userPassword = userPassword;
        this.userFirstname = userFirstname;
        this.userLastname = userLastname;
    }

    /// GETTERS
    public int getUserID() {
        return userID;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserFirstname() {
        return userFirstname;
    }

    public String getUserLastname() {
        return userLastname;
    }

    /// SETTERS
    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserFirstname(String userFirstname) {
        this.userFirstname = userFirstname;
    }

    public void setUserLastname(String userLastname) {
        this.userLastname = userLastname;
    }
}
