/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.csci483.myprojectname.controller;

import ca.csci483.myprojectname.model.DBConnection;
import ca.csci483.myprojectname.model.User;
import java.io.Serializable;
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
            return "success";
        }
        this.password = null;
        
        return "fail";
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
    
    
}
