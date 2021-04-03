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
import com.mysql.cj.jdbc.MysqlDataSource;
import ca.csci483.myprojectname.model.User;
import static com.mysql.cj.MysqlType.JSON;
import javax.faces.bean.ManagedBean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


//@ManagedBean(name = "dbConnection")
public class DBConnection {
    
    private final String host;
    private final int port;
    private final String databaseName;
    private final String username;
    private final String password;
    private MysqlDataSource dataSource;
    
    public DBConnection () {
        this.host = "127.0.0.1";
        this.port = 3306;
        this.databaseName = "meetme";
        this.username = "admin";
        this.password = "admin";
        connectDataSource();
    }
    
    public void connectDataSource(){
        dataSource = new MysqlDataSource();
               
        String url = String.format(
                "jdbc:mysql://%s:%d/%s?allowPublicKeyRetrieval=true&useSSL=false",
                this.host,
                this.port,
                this.databaseName);
        dataSource.setURL(url);

        dataSource.setUser(this.username);
        dataSource.setPassword(this.password);        
    }
    
    private void close(ResultSet rs, Statement st, Connection cn){
        if (rs != null){
            try {
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (st != null){
            try {
                st.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (cn != null){
            try {
                cn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean registerUser(String username, String password, String firstName, String lastName, String email){
        
        Connection dbConnection = null;
        Statement dbStatement = null;
        
        try {
            dbConnection = dataSource.getConnection();
            dbStatement = dbConnection.createStatement();
            System.out.println("Connection established and statement issued");
            String query = String.format(
                    "INSERT INTO Users "
                    + "(username, password, first_name, last_name, email) "
                    + "VALUES ('%s', '%s', '%s', '%s', '%s'); ",
                    username,
                    password,
                    firstName,
                    lastName,
                    email
            );
            
            System.out.println("Query used: " + query);
            dbStatement.executeUpdate(query);
            
        } catch(SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            this.close(null, dbStatement, dbConnection);
            return false;
        }
        this.close(null, dbStatement, dbConnection);
        return true;
    }
    
    public User findUser (String username, String password) {
        Connection dbConnection = null;
        Statement dbStatement = null;
        User user = null;
        
        try {
            dbConnection = dataSource.getConnection();
            dbStatement = dbConnection.createStatement();
            String query = String.format("SELECT * FROM Users WHERE email = %s and password = %s", 
                    username, password);
            System.out.println("Query used: " + query);

            ResultSet result = dbStatement.executeQuery(query);
            if (result.next()) {
                user = new User();
                user.setUsername(username);
                user.setFirstName(result.getString("username"));
                user.setLastName(result.getString("first_name"));
                user.setEmail(result.getString("email"));
            }
            
        } catch(SQLException e) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, e);
            this.close(null, dbStatement, dbConnection);
            return user;
        }
        this.close(null, dbStatement, dbConnection);
        return user;
    }
}