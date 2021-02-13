package ca.csci483.myprojectname.model;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;

/**
 *
 * @author osoufan
 */
public class User implements Serializable{
    
    private String name; // name is randomly assigned by the system
    private String homeDir, relativeDir;

    public String getRelativeDir() {
        return relativeDir;
    }

    public void setRelativeDir(String relativeDir) {
        this.relativeDir = relativeDir;
    }

    public User() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setHomeDir(String dir) {
        this.homeDir = dir;
    }

    public String getHomeDir() {
        return homeDir;
    }
    
    
}
