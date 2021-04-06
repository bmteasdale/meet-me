/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.csci483.myprojectname.controller;

import ca.csci483.myprojectname.model.DBConnection;
import ca.csci483.myprojectname.model.Meeting;
import ca.csci483.myprojectname.model.User;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author bmteasdale
 */
@SessionScoped
@Named("userBean")
public class UserBean implements Serializable {
    private String username;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String email;
    private String bio;  
    
    private String userMeetingIds;
    private List<String> userMeetingIdsList;
    private List<Meeting> allUserMeetings;
    
    private boolean successfulRegistration;
    private boolean successfulLogin;
    
    private ScheduleModel eventModel;
    private ScheduleEvent<?> event = new DefaultScheduleEvent<>();
    
    public UserBean(){
    }
    
    public String handleRegistration(String firstName, String lastName, String username, String email, String password, String confirmPassword){
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.username =  username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        
        DBConnection dbc = new DBConnection();
        boolean duplicateUsername = dbc.duplicateUsername(username);
        boolean validRegistration = password.equals(confirmPassword);
        
        if(validRegistration && !duplicateUsername) {
            this.successfulRegistration = dbc.registerUser(username, password, firstName, lastName, email);
        }
        else {
            this.successfulRegistration = false;
            
            if(!validRegistration){
                showRegistrationMessage("Passwords do not match!");
            }
            else {
                showRegistrationMessage("Sorry, this username already exists!");
            }
            return "";
        }
        
        showRegistrationMessage("You have successfully registered!");
        return "registrationSuccess";
    }
    
    public void showRegistrationMessage(String message){
        FacesContext context = FacesContext.getCurrentInstance();
        
        if (successfulRegistration == true){
            context.addMessage(null, new FacesMessage("Success", message));
            context.getExternalContext().getFlash().setKeepMessages(true);
        }
        else {
            context.addMessage(null, new FacesMessage("Error", message));
        }
        
    }
    
    public void showLoginMessage(){
        FacesContext context = FacesContext.getCurrentInstance();
        
        if (this.successfulLogin == true){
            context.addMessage(null, new FacesMessage("Success", "Successful Login!"));
        }
        else {
            context.addMessage(null, new FacesMessage("Error", "Sorry, we could't find an account with those credentials!"));
        }
        
    }
    
    public String loginValidation(String username, String password) {
        this.successfulLogin = false;
        DBConnection dbc = new DBConnection();
        User currentUser = dbc.findUser(username, password);
        if (currentUser != null) {
            this.successfulLogin = true;
            this.password = null;
            this.firstName = currentUser.getFirstName();
            this.lastName = currentUser.getLastName();
            this.username = currentUser.getUsername();
            this.email = currentUser.getEmail();
            this.bio = currentUser.getBio();
            this.userMeetingIds = currentUser.getUserMeetingIds();
            this.userMeetingIdsList = parseUserMeetingIds(userMeetingIds);
            
            allUserMeetings = dbc.findMeetings(userMeetingIdsList);
            addMeetingsToCalendar(allUserMeetings);
            
            return "success";
        }
        this.password = null;
        
        return "fail";
    }
    
    public void addMeetingsToCalendar(List<Meeting> meetings){
        
        eventModel = new DefaultScheduleModel();
        
        for (int i = 0; i < meetings.size(); i++) {
            
            Meeting currentMeeting = meetings.get(i);
            
            LocalDateTime startDateTime = LocalDateTime.of(currentMeeting.getStartDate(), currentMeeting.getStartTime());
            LocalDateTime endDateTime = LocalDateTime.of(currentMeeting.getEndDate(), currentMeeting.getEndTime());
            
            
            event = DefaultScheduleEvent.builder()
                    .title(currentMeeting.getTitle())
                    .description(currentMeeting.getDescription())
                    .startDate(startDateTime)
                    .endDate(endDateTime)
                    .build();
            eventModel.addEvent(event);
            
        }
        
    }
    
    public List<String> parseUserMeetingIds(String ids) {
    
        List<String> allIds = null;
        
        // remove curly braces
        ids = ids.substring(ids.indexOf(('{')) + 1);
        ids = ids.substring(0, ids.indexOf(('}')));
            
        // get string after ':' (removes '"ids" :')
        ids = ids.substring(ids.indexOf((':')) + 1);
            
        // remove whitespaces
        ids = ids.replaceAll("\\s","");
            
        // remove square brackets
        ids = ids.substring(ids.indexOf(('[')) + 1);
        ids = ids.substring(0, ids.indexOf((']')));
        
        allIds = Arrays.asList(ids.split(","));
        return allIds;
    }
    
    public String editUserInfo(String firstName, String username, String email, String bio){
        
        /* Fix:
            - full name
            - username duplication
        */
        
        DBConnection dbc = new DBConnection();
        boolean editUserSuccess = dbc.editUserInfo(firstName, lastName, username, email, bio);
        
        if(editUserSuccess == false){
            showMessage("Error", "Sorry, something went wrong!");
            return "";
        }
        else{
            showMessage("Success", "Account settings have been updated!");
            return "";
        }
    }
    
    public String logout() {
        this.successfulLogin = false;
        this.firstName = null;
        this.lastName = null;
        this.username = null;
        this.email = null;
        this.bio = null;
        this.userMeetingIds = null;
        return "logout";
    }
    
    public void showMessage(String title, String message){
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(title, message));
    }
        
    public boolean isSuccessfulLogin() {
        return successfulLogin;
    }

    public void setSuccessfulLogin(boolean successfulLogin) {
        this.successfulLogin = successfulLogin;
    }
    
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

    public boolean isSuccessfulRegistration() {
        return successfulRegistration;
    }

    public void setSuccessfulRegistration(boolean successfulRegistration) {
        this.successfulRegistration = successfulRegistration;
    }

    public String getUserMeetingIds() {
        return userMeetingIds;
    }

    public void setuserMeetingIds(String userMeetingIds) {
        this.userMeetingIds = userMeetingIds;
    }

    public List<String> getUserMeetingIdsList() {
        return userMeetingIdsList;
    }

    public void setUserMeetingIdsList(List<String> userMeetingIdsList) {
        this.userMeetingIdsList = userMeetingIdsList;
    }

    public List<Meeting> getAllUserMeetings() {
        return allUserMeetings;
    }

    public void setAllUserMeetings(List<Meeting> allUserMeetings) {
        this.allUserMeetings = allUserMeetings;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public ScheduleEvent<?> getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent<?> event) {
        this.event = event;
    }
    
    
}
