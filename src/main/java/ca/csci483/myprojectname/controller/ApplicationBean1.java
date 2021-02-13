/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.csci483.myprojectname.controller;

import java.io.File;
import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import ca.csci483.myprojectname.model.User;
import ca.csci483.myprojectname.utils.DataUtils;

/**
 *
 * @author osoufan
 */
@ApplicationScoped
@Named("applicationBean1")
public class ApplicationBean1 implements Serializable {

    private final String realUserHomePath, resourceDir, data_path;
    private final String usr_home = "/resources/users/";
    private final String data_dir = "/resources/data";
    private final String domain_url;

    public String getResourceDir() {
        return resourceDir;
    }

    /**
     * <p>
     * Construct a new application data bean instance.</p>
     */
    //ApplicationBean1 called before Init (and called during application start
    public ApplicationBean1() {
        //get resources path
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        this.domain_url = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRe‌​quest()).getRequestURL().toString();
        this.resourceDir = context.getRealPath("/resources");
        this.realUserHomePath = context.getRealPath(usr_home);
        data_path = context.getRealPath(data_dir);
        
    }

    public String getRealUserHomePath() {
        return realUserHomePath;
    }

    // create a tempature user account if user log in as a guest will not remember, only session only
    public User createNewUser() {
        try {
            //try to clean the user folder to remove old files (more than 1 day)
            DataUtils.deleteFilesOlderThanNdays(realUserHomePath, 1);
            //first create a random user names
            User user = new User();
            String guestName = File.createTempFile("guest", "tmp").getName();
            String guestDir = realUserHomePath + File.separator + guestName;
            File guestFolder = new File(guestDir);
            while (guestFolder.exists()) {
                guestName = File.createTempFile("guest", "tmp").getName();
                guestDir = realUserHomePath + File.separator + guestName;
                guestFolder = new File(realUserHomePath + File.separator + guestName);
            }
            guestFolder.mkdir();
            user.setName(guestName);
            user.setRelativeDir(usr_home + guestName);
            user.setHomeDir(guestDir);
            return user;
        } catch (Exception e) {
            System.out.println("Error in creating users! ===============");
            e.printStackTrace();
        }
        return null;
    }

    public User createNewUser(String prefix) {
        try {
            //try to clean the user folder to remove old files (more than 1 day)
            DataUtils.deleteFilesOlderThanNdays(realUserHomePath, 1);
            //first create a random user names
            User user = new User();
            String guestName = File.createTempFile(prefix + "guest", "tmp").getName();
            String guestDir = realUserHomePath + File.separator + guestName;
            File guestFolder = new File(guestDir);
            while (guestFolder.exists()) {
                guestName = File.createTempFile(prefix + "guest", "tmp").getName();
                guestDir = realUserHomePath + File.separator + guestName;
                guestFolder = new File(realUserHomePath + File.separator + guestName);
            }
            guestFolder.mkdir();
            user.setName(guestName);
            user.setRelativeDir(usr_home + guestName);
            user.setHomeDir(guestDir);
            return user;
        } catch (Exception e) {
            System.out.println("Error in creating users! ===============");
            e.printStackTrace();
        }
        return null;
    }

    public User loadUser(String guestName) {
        try {
            //try to clean the user folder to remove old files (more than 1 day)
            DataUtils.deleteFilesOlderThanNdays(realUserHomePath, 1);
            //first create a random user names
            User user = new User();
            String guestDir = realUserHomePath + File.separator + guestName;
            File guestFolder = new File(guestDir);
            if (!guestFolder.exists()) {
                guestFolder.mkdir();
            }
            user.setName(guestName);
            user.setRelativeDir(usr_home + guestName);
            user.setHomeDir(guestDir);
            return user;
        } catch (Exception e) {
            System.out.println("Error in creating users! ===============");
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean isOnPublicServer() {
        return domain_url.contains("XYZ.ca");
    }
    
    public String getDomainURL() {
        //note, ends with slash
        if (isOnPublicServer()) {
            String domainUrl = DataUtils.getDomainURL(domain_url);
            return domainUrl + "/CSCI483TemplateSite";
        } else {
            if(domain_url.endsWith("/")){
                return (domain_url.substring(0, domain_url.length()- 1));
            }
            return domain_url;
        }
    }
}
