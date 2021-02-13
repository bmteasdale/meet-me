/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.csci483.myprojectname.model;

import java.io.Serializable;

/**
 *
 * @author osoufan
 */
public class ColumnModel implements Serializable {

    private String header;
    private String property;
    private String type; //int/duble/string; also used as name
    
    public ColumnModel(String header, String property, String type) {
        this.header = header;
        this.property = property;
        this.type = type;
    }

    public String getHeader() {
        return header;
    }

    public String getProperty() {
        return property;
    }
    
    public String getType(){
        return type;
    }
}