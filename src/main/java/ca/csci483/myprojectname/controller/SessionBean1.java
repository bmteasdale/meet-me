/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.csci483.myprojectname.controller;

import ca.csci483.myprojectname.utils.DataUtils;
import ca.csci483.myprojectname.model.User;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;


/**
 *
 * @author osoufan
 */
@SessionScoped
@Named("sessionBean1")
public class SessionBean1 implements Serializable {

    private final ApplicationBean1 ab = (ApplicationBean1) DataUtils.findBean("applicationBean1");
    private User currentUser = null;
    
    private String myName = "Othman";

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    /**
     * <p>
     * Construct a new session data bean instance.</p>
     */
    public SessionBean1() {
    }

    //More themes at: https://repository.primefaces.org/org/primefaces/themes/
    private String theme = "smoothness";

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
    
}
