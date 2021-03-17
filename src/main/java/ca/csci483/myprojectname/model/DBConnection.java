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
import com.mongodb.*;
import io.github.cdimascio.dotenv.Dotenv;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class DBConnection {
    private String dbURI;
    private String dbName;
    private MongoClient client;
    private DB database;
    private DBCollection userCollection;
    private DBCollection meetingCollection;
    
    public DBConnection(){
        Dotenv dotenv = Dotenv.configure()
        .directory("../utils")
        .ignoreIfMalformed()
        .ignoreIfMissing()
        .load();
       
        this.dbURI = dotenv.get("DB_URI");
        this.dbName = dotenv.get("DB_NAME");
        try {
            MongoClientURI uri = new MongoClientURI(this.dbURI);
            MongoClient mongoClient = new MongoClient(uri);
            this.database = mongoClient.getDB(this.dbName);
            this.userCollection = database.getCollection("user");
            this.meetingCollection = database.getCollection("meeting");
            System.out.println("Mongo successfully connected!");
        } catch (UnknownHostException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    };
    
}