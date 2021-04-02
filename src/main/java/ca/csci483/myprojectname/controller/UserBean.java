/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.csci483.myprojectname.controller;

import ca.csci483.myprojectname.model.DBConnection;
import ca.csci483.myprojectname.model.User;
import java.io.Serializable;
import java.net.UnknownHostException;
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
    private boolean registrationFail;
    
    public UserBean(){
    }
    
    public void handleRegistration(String username, String password, String confirmPassword, String fname, String lname, String email){
        
        /* Not working yet: inputText values are not updating in RegisterView.xhtml */

        //boolean validRegistration = this.password.equals(this.confirmPassword);
        boolean validRegistration = password.equals(confirmPassword);
        
        if(validRegistration) {
            DBConnection dbc = new DBConnection();
            this.registrationFail = !(dbc.registerUser(username, password, fname, lname, email));
        }
    }
    
    public void showRegistrationMessage(){
        FacesContext context = FacesContext.getCurrentInstance();
        
        if (this.registrationFail == false){
            context.addMessage(null, new FacesMessage("Success", "You have successfully registered!"));
        }
        else {
            context.addMessage(null, new FacesMessage("Error", "Please try again!"));
        }
        
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

    public boolean isRegistrationFail() {
        return registrationFail;
    }

    public void setRegistrationFail(boolean registrationFail) {
        this.registrationFail = registrationFail;
    }
    
    
}
