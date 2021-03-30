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
//import io.github.cdimascio.dotenv.Dotenv;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import javax.faces.bean.ManagedBean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;


@ManagedBean(name = "dbConnection")
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
}