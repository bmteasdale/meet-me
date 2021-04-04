/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.csci483.myprojectname.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 *
 * @author Rachel
 */
public class Meeting implements Serializable{
    private String meetingId;
    private Date startDate;
    private Date endDate;
    private Time startTime;
    private Time endTime;
    private String title;
    private String description;
    private String location;
    private String chairPerson;
    private String attendees;
    
    public Meeting(String meetingId, Date startDate, Date endDate, Time startTime, Time endTime, String title, String description, String location, String chairperson, String attendees){
        this.meetingId = meetingId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.description = description;
        this.location = location;
        this.chairPerson = chairperson;
        this.attendees = attendees;
    }
    
    Meeting(){
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getChairPerson() {
        return chairPerson;
    }

    public void setChairPerson(String chairPerson) {
        this.chairPerson = chairPerson;
    }

    public String getAttendees() {
        return attendees;
    }

    public void setAttendees(String attendees) {
        this.attendees = attendees;
    }
    
    
}
