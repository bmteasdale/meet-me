/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.csci483.myprojectname.model;

/**
 *
 * @author bmteasdale
 */
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class DBConnection {
    private final String HOST;
    private final String PORT;
    private final String DB_NAME;
    private final String USER_NAME;
    private final String PASSWORD;
    
    
    public DBConnection(){
        this.HOST = "localhost";
        this.PORT = "3306";
        this.DB_NAME = "meetme";
        this.USER_NAME = "csci483";
        this.PASSWORD = "csci483";
    };
    
}