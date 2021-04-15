package ca.csci483.myprojectname.model;

import java.io.Serializable;

/**
 * This class holds the data of a user.
 * 
 */
public class User implements Serializable{
    
    private String username;    // unique username of the user
    private String password;    // password of the user
    private String firstName;   // first name of the user
    private String lastName;    // last name of the user
    private String email;       // email address of the user
    private String bio;         // bio of the user
    private String userMeetingIds;  // IDs of the meetings that the user is a participant in

    // Constructor
    
    public User(String username, String password, String firstName, String lastName, String email) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.bio = "";
    }

    public User(String username, String password, String firstName, String lastName, String email, String bio) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.bio = bio; 
    }

    User() {
    }

    // Getters & Setters
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getUsername () {
        return username;
    }

    public String getUserMeetingIds() {
        return userMeetingIds;
    }

    public void setUserMeetingIds(String userMeetingIds) {
        this.userMeetingIds = userMeetingIds;
    }
    
    
    
}
