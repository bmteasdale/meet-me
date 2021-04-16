package ca.csci483.myprojectname.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Rachel
 * This class holds the data of a meeting.
 */
public class Meeting implements Serializable{
    private String meetingId;       // unique ID of the meeting
    private LocalDate startDate;    // start date of the meeting
    private LocalDate endDate;      // end date of the meeting
    private LocalTime startTime;    // start time of the meeting
    private LocalTime endTime;      // end time of the meeting
    private String title;           // title of the meeting
    private String description;     // description of the meeting
    private String location;        // location of the meeting
    private String chairPerson;     // chairperson of the meeting
    private String attendees;       // attendees of the meeting
    
    // Constructor
    
    public Meeting(String meetingId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, String title, String description, String location, String chairperson, String attendees){
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
    
    // Getters & Setters

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
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
