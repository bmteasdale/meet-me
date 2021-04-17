package ca.csci483.myprojectname.model;

import ca.csci483.myprojectname.controller.UserBean;
import com.mysql.cj.jdbc.MysqlDataSource;
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
import javax.el.ELContext;
import javax.faces.context.FacesContext;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 * @author bmteasdale
 * @author Rachel
 * 
 * Class to establish connection to MySQL server and manage information 
 * retrieval and updates
 */

public class DBConnection {
    
    private final String host;
    private final int port;
    private final String databaseName;
    private final String username;
    private final String password;
    private MysqlDataSource dataSource;
    
    /**
     * Default class constructor to initialize connection parameters based on 
     * docker file provided
     */
    public DBConnection () {
        this.host = "mysqlserver";
        this.port = 3306;
        this.databaseName = "meetme";
        this.username = "admin";
        this.password = "admin";
        connectDataSource();
    }
    
    /**
     * Function to connect data source to the server based on given parameters
     */
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
    
    /**
     * Function to close result set, statement, and connection.
     *
     * @param rs ResultSet that reads the result of a query
     * @param st Statement that executes the query
     * @param cn Connection obtained from data source
     */
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
    
    /**
     * Function to insert new user to the database. This is done when a new 
     * registration is to be processed.
     * 
     * @param username: unique username of user to be inserted
     * @param password: password of user to be inserted
     * @param firstName: first name of user to be inserted
     * @param lastName: last name of user to be inserted
     * @param email: email of user to be inserted
     * 
     * @return boolean value representing if insertion was successful
     */
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
    
    /**
     * Function to retrieve user data from server for given username
     * 
     * @param username: email of user to retrieve
     * 
     * @return boolean value representing if user was found in the database
     */
    public boolean findUser(String username) {
        
        Connection dbConnection = null;
        Statement dbStatement = null;
        
        try {
            dbConnection = dataSource.getConnection();
            dbStatement = dbConnection.createStatement();
            String query = String.format("SELECT * FROM Users WHERE username = '%s'", 
                    username);
            System.out.println("Query used: " + query);

            ResultSet result = dbStatement.executeQuery(query);
            if (result.next()) {
                return true;
            }
            
        } catch(SQLException e) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, e);
            this.close(null, dbStatement, dbConnection);
            return false;
        }
        this.close(null, dbStatement, dbConnection);
        return false;
    }
    
    /**
     * Function to retrieve user data from server for given username and 
     * password combination
     * 
     * @param username: email of user to retrieve
     * @param password: password of user to retrieve
     * 
     * @return User object with all information loaded.
     */
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
    
    /**
     * Function to edit the users information
     * 
     * @param firstName: first name of user to update
     * @param lastName: last name of user to update
     * @param username: username of user to retrieve
     * @param email: email of user to update
     * @param bio: bio of user to update
     * 
     * @return boolean value representing if update was successful 
     */
    public boolean editUserInfo(String firstName, String lastName, String username, String email, String bio){
        Connection dbConnection = null;
        Statement dbStatement = null;
        
        try {
            dbConnection = dataSource.getConnection();
            dbStatement = dbConnection.createStatement();
            System.out.println("Connection established and statement issued");
            String query = String.format(
                    "UPDATE Users "
                    + "SET first_name = '%s', last_name = '%s', username = '%s', email = '%s',  bio = '%s'"
                    + "WHERE username = '%s'",
                    firstName, lastName, username, email, bio, username);
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
    
    /**
     * Function to retrieve a list of meetings from the database
     * 
     * @param userMeetingIds: list of users meeting IDs 
     * 
     * @return list of meetings
     */
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
    
    /**
     * Function to retrieve a list of meetings for a user from the database
     * 
     * @param username: username of user
     * 
     * @return list of meetings
     */
    public List<Meeting> findUserMeetings(String username){
        
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
                
                // convert ids from a String into a list of String variables
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
    
    /**
     * Function to insert a new meeting into the database.
     * 
     * @param startDate: start date of meeting to be inserted
     * @param endDate: end date of meeting to be inserted
     * @param startTime: start time of meeting to be inserted
     * @param endTime: end time of meeting to be inserted
     * @param title: title of meeting to be inserted
     * @param description: description of meeting to be inserted
     * @param location: location of meeting to be inserted
     * @param chairperson: chairperson of meeting to be inserted
     * @param participant: participant of meeting to be inserted
     * @param attendees: attendees of meeting to be inserted
     * 
     * @return boolean value representing if insertion was successful
     */
    public boolean addMeeting(String startDate, String endDate, String startTime, String endTime, String title, String description, String location, String chairperson, String participant, JSONObject attendees){
        
        Connection dbConnection = null;
        Statement dbStatement = null;
        
        try {
            dbConnection = dataSource.getConnection();
            dbStatement = dbConnection.createStatement();
            System.out.println("Connection established and statement issued");
            String query = String.format(
                    "INSERT INTO Meetings "
                    + "(start_date, end_date, start_time, end_time, title, description, location, chairperson, attendees) "
                    + "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', "
                    + "'" + attendees + "'" 
                    + "); ",
                    startDate,
                    endDate,
                    startTime,
                    endTime,
                    title,
                    description,
                    location,
                    chairperson
            );
            
            System.out.println("Query used: " + query);
            dbStatement.executeUpdate(query);
            
        } catch(SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            this.close(null, dbStatement, dbConnection);
            return false;
        }
        this.close(null, dbStatement, dbConnection);
        
        addMeetingIdToUser(chairperson);
        addMeetingIdToUser(participant);
        return true;
        
    }
    
    /**
     * Function to retrieve the id of the last meeting that has been added to 
     * the database
     * 
     * @param username: username of user to retrieve
     * 
     * @return string representing the meeting ID
     */
    public String findLastMeetingId(String username){
        
        Connection dbConnection = null;
        Statement dbStatement = null;
        String id = null;
        
        try {
            dbConnection = dataSource.getConnection();
            dbStatement = dbConnection.createStatement();
            String query = String.format("SELECT * FROM Meetings ORDER BY meeting_id DESC LIMIT 1;");
            System.out.println("Query used: " + query);

            ResultSet result = dbStatement.executeQuery(query);
            if (result.next()) {
                id = result.getString("meeting_id");
            }
            
        } catch(SQLException e) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, e);
            this.close(null, dbStatement, dbConnection);
            return "";
        }
        this.close(null, dbStatement, dbConnection);
        return id;
    }
    
    /**
     * Function to add a meeting ID to a users list of meeting IDs
     * 
     * @param username: username of user to retrieve
     */
    public void addMeetingIdToUser(String username){
        
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        UserBean ub = (UserBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "userBean");
        String ids = ub.getUserMeetingIds();
        
        // convert ids from a String into a list of String variables
        List<String> allIds = null;
        ids = ids.substring(ids.indexOf(('{')) + 1);
        ids = ids.substring(0, ids.indexOf(('}')));
        ids = ids.substring(ids.indexOf((':')) + 1);
        ids = ids.replaceAll("\\s","");
        ids = ids.substring(ids.indexOf(('[')) + 1);
        ids = ids.substring(0, ids.indexOf((']')));
        allIds = Arrays.asList(ids.split(","));
        
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        
        String latestID = findLastMeetingId(username);
        int iLatestID = Integer.parseInt(latestID);
        
        for (int i = 0; i < allIds.size(); i++) {
            int id = Integer.parseInt(allIds.get(i));
            array.put(id);
        }
        array.put(iLatestID);
        json.put("ids", array);
        
        Connection dbConnection = null;
        Statement dbStatement = null;
        
        try {
            dbConnection = dataSource.getConnection();
            dbStatement = dbConnection.createStatement();
            System.out.println("Connection established and statement issued");
            String query = String.format(
                    "UPDATE Users "
                    + "SET meeting_ids =  '" + json + "'"
                    + "WHERE username = '%s'"
                    , username
            );
            
            System.out.println("Query used: " + query);
            dbStatement.executeUpdate(query);
            
        } catch(SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            this.close(null, dbStatement, dbConnection);
        }
        this.close(null, dbStatement, dbConnection);
    }
    
}