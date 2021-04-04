/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.csci483.myprojectname.controller;

import ca.csci483.myprojectname.model.DBConnection;
import ca.csci483.myprojectname.model.User;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

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
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String phone;
    private String userMeetingIds;
    private List<String> userMeetingIdsList;
    private List<Meeting> allUserMeetings;
    private boolean successfulRegistration;
    private boolean successfulLogin;
    
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
            this.street = currentUser.getStreet();
            this.city = currentUser.getCity();
            this.state = currentUser.getState();
            this.zipCode = currentUser.getZipCode();
            this.phone = currentUser.getPhone();
            this.userMeetingIds = currentUser.getUserMeetingIds();
            
            // print
            System.out.println("Meeting IDs string:" + this.userMeetingIds);
            
            // parse meetindIds string
            userMeetingIds = parseUserMeetingIds(userMeetingIds);
            
            // split parsed string into list
            this.userMeetingIdsList = Arrays.asList(userMeetingIds.split(","));
            
            // print
            System.out.println("Meeting IDs list: " + userMeetingIdsList);
            for (int i = 0; i < userMeetingIdsList.size(); i++) {
                System.out.println(userMeetingIdsList.get(i));
            }
            
            /*
            Print results:
                Meeting IDs string:{"ids": [1, 2, 3, 4]}|#]
                Meeting IDs list: [1, 2, 3, 4]|#]
                1|#]
                2|#]
                3|#]
                4|#]
            */
            
            return "success";
        }
        this.password = null;
        
        return "fail";
    }
    
    public String parseUserMeetingIds(String ids) {
    
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
        
        return ids;
    }
    
    public String editUserInfo(String firstName, String username, String email, String street, String city, String state, String zipCode, String phone){
        
        /* Fix:
            - full name
            - username duplication
        */
        
        DBConnection dbc = new DBConnection();
        boolean editUserSuccess = dbc.editUserInfo(firstName, lastName, username, email, street, city, state, zipCode, phone);
        
        if(editUserSuccess == false){
            showMessage("Error", "Sorry, something went wrong!");
            return "";
        }
        else{
            showMessage("Success", "Account settings have been updated!");
            return "";
        }
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
    
    
}
