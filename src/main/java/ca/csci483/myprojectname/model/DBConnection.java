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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    
    public boolean duplicateUsername(String username){
        Connection dbConnection = null;
        Statement dbStatement = null;
        
        try {
            dbConnection = dataSource.getConnection();
            dbStatement = dbConnection.createStatement();
            
            System.out.println("Connection established and statement issued");
            String query = String.format(
                "SELECT * FROM Users WHERE username = '%s'", username);
            System.out.println("Query used: " + query);
            
            ResultSet result = dbStatement.executeQuery(query);
            if (result.next()) {
                return true;
            }
            
        } catch(SQLException e) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, e);
            this.close(null, dbStatement, dbConnection);
        }
        this.close(null, dbStatement, dbConnection);
        return false;
    }
    
    public User findUser (String username, String password) {
        Connection dbConnection = null;
        Statement dbStatement = null;
        User user = null;
        
        try {
            dbConnection = dataSource.getConnection();
            dbStatement = dbConnection.createStatement();
            String query = String.format("SELECT * FROM Users WHERE username = '%s' and password = '%s'", 
                    username, password);
            System.out.println("Query used: " + query);

            ResultSet result = dbStatement.executeQuery(query);
            if (result.next()) {
                user = new User();
                user.setUsername(username);
                user.setFirstName(result.getString("first_name"));
                user.setLastName(result.getString("last_name"));
                user.setEmail(result.getString("email"));
                user.setBio(result.getString("bio"));
                user.setUserMeetingIds(result.getString("meeting_ids"));
                
            }
            
        } catch(SQLException e) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, e);
            this.close(null, dbStatement, dbConnection);
            return user;
        }
        this.close(null, dbStatement, dbConnection);
        return user;
    }
    
    public boolean editUserInfo(String firstName, String lastName, String username, String email, String street, String city, String state, String zipCode, String phone){
        Connection dbConnection = null;
        Statement dbStatement = null;
        
        try {
            dbConnection = dataSource.getConnection();
            dbStatement = dbConnection.createStatement();
            System.out.println("Connection established and statement issued");
            String query = String.format(
                    "UPDATE Users "
                    + "SET first_name = '%s', last_name = '%s', username = '%s', email = '%s',  street = '%s',  city = '%s',  state = '%s',  zip_code = '%s', phone = '%s'"
                    + "WHERE username = '%s'",
                    firstName, lastName, username, email, street, city, state, zipCode, phone, username);
            System.out.println("Query used: " + query);
            dbStatement.executeUpdate(query);
            
        } catch(SQLException e) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, e);
            this.close(null, dbStatement, dbConnection);
            return false;
        }
        this.close(null, dbStatement, dbConnection);
        return true;
    }
    
    public List<Meeting> findMeetings(List<String> userMeetingIds){
        Connection dbConnection = null;
        Statement dbStatement = null;
        Meeting meeting = null;
        List<Meeting> allMeetings = null;
        
        try {
            dbConnection = dataSource.getConnection();
            dbStatement = dbConnection.createStatement();
            allMeetings = new ArrayList<>();
            
            for (int i = 0; i < userMeetingIds.size(); i++) {
                
                String query = String.format("SELECT * FROM Meetings WHERE meeting_id = '%s'",
                    userMeetingIds.get(i));
                System.out.println("Query used: " + query);
                
                ResultSet result = dbStatement.executeQuery(query);
                if (result.next()) {
                    meeting = new Meeting();
                    meeting.setMeetingId(result.getString("meeting_id"));
                    meeting.setStartDate(LocalDate.parse(result.getString("start_date")));
                    meeting.setEndDate(LocalDate.parse(result.getString("end_date")));
                    meeting.setStartTime(LocalTime.parse(result.getString("start_time")));
                    meeting.setEndTime(LocalTime.parse(result.getString("end_time")));
                    meeting.setTitle(result.getString("title"));
                    meeting.setDescription(result.getString("description"));    
                    meeting.setLocation(result.getString("location"));
                    meeting.setChairPerson(result.getString("chairperson"));
                    meeting.setAttendees(result.getString("attendees"));
                    
                    allMeetings.add(meeting);
                }
                
            }
            
        } catch(SQLException e) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, e);
            this.close(null, dbStatement, dbConnection);
            return allMeetings;
        }
        this.close(null, dbStatement, dbConnection);
        return allMeetings;
        
    }
    
    public List<Meeting> findAvailableTimeblocks(String username){
        
        Connection dbConnection = null;
        Statement dbStatement = null;
        List<Meeting> selectedUserMeetings = null;
        
        try {
            dbConnection = dataSource.getConnection();
            dbStatement = dbConnection.createStatement();
            List<String> selectedUserMeetingsIdList = null;
            selectedUserMeetings = new ArrayList<>();
            String ids = null;
            
            String query = String.format("SELECT * FROM Users WHERE username = '%s'",
                    username);
                System.out.println("Query used: " + query);
            
            
            ResultSet result = dbStatement.executeQuery(query);
            if (result.next()) {
                ids = result.getString("meeting_ids");
                ids = ids.substring(ids.indexOf(('{')) + 1);
                ids = ids.substring(0, ids.indexOf(('}')));
                ids = ids.substring(ids.indexOf((':')) + 1);
                ids = ids.replaceAll("\\s","");
                ids = ids.substring(ids.indexOf(('[')) + 1);
                ids = ids.substring(0, ids.indexOf((']')));
                
                selectedUserMeetingsIdList = Arrays.asList(ids.split(","));
            }
            
            selectedUserMeetings = findMeetings(selectedUserMeetingsIdList);
            
        } catch(SQLException e) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, e);
            this.close(null, dbStatement, dbConnection);
            return selectedUserMeetings;
        }
        this.close(null, dbStatement, dbConnection);
        return selectedUserMeetings;
        
    }
    
}