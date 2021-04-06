/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.csci483.myprojectname.controller;

import ca.csci483.myprojectname.model.DBConnection;
import ca.csci483.myprojectname.model.Meeting;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author Rachel
 */
@SessionScoped
@Named("meetingBean")
public class MeetingBean implements Serializable{
    private String meetingId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String title;
    private String description;
    private String location;
    private String chairPerson;
    private String attendees;
    private String participant;
    private String today; // shows blank screen when set to current date...
    private LocalTime meetingTime;
    private String convertedMeetingTime;
    private List<LocalTime> meetingTimes;
    private List<String> convertedMeetingTimes;
    private List<LocalTime> availableTimes;
    
    private ScheduleModel eventModel;
    private ScheduleEvent<?> event = new DefaultScheduleEvent<>();
    
    public MeetingBean(){
    }
    
    public void findAvailabilities(LocalDate date, String username){
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        UserBean ub = (UserBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "userBean");
        DBConnection dbc = new DBConnection();
        
        meetingTimes = Arrays.asList(LocalTime.of(8,00,00), LocalTime.of(9,00,00), LocalTime.of(10,00,00), LocalTime.of(11,00,00),
                                     LocalTime.of(12,00,00), LocalTime.of(13,00,00), LocalTime.of(14,00,00), LocalTime.of(15,00,00),
                                     LocalTime.of(16,00,00), LocalTime.of(17,00,00));
        String formattedDate = date.format(DateTimeFormatter.ofPattern("00YY-MM-dd"));
        
        List<LocalTime> availableTimes = new ArrayList<>();
        for (int i = 0; i < ub.getAllUserMeetings().size(); i++) {
            
            Meeting currentMeeting = ub.getAllUserMeetings().get(i);
            String mFormattedDate = currentMeeting.getStartDate().format(DateTimeFormatter.ofPattern("00YY-MM-dd"));
            
            if(formattedDate.equals(mFormattedDate)){
                LocalTime startTime = currentMeeting.getStartTime();
                LocalTime endTime = currentMeeting.getEndTime();
                
                for (int j = 0; j < meetingTimes.size(); j++){
                    LocalTime checkTime = meetingTimes.get(j);
                    
                   // if (checkTime.isAfter(startTime) && checkTime.isBefore(endTime)){
                    if ((checkTime != startTime) && !availableTimes.contains(checkTime)){
                        availableTimes.add(checkTime);
                    }
                    
                }
                meetingTimes = availableTimes;
                availableTimes = new ArrayList<>();
            }
            
        }
        
        // put this in a for loop for multiple users
        List<Meeting> newUserMeetings = dbc.findUserMeetings(username);
        for (int i = 0; i < newUserMeetings.size(); i++) {
            Meeting newCurrentMeeting = newUserMeetings.get(i);
            String nmFormattedDate = newCurrentMeeting.getStartDate().format(DateTimeFormatter.ofPattern("00YY-MM-dd"));
            
            if(formattedDate.equals(nmFormattedDate)){
                LocalTime startTime = newCurrentMeeting.getStartTime();
                LocalTime endTime = newCurrentMeeting.getEndTime();
                
                for (int j = 0; j < meetingTimes.size(); j++){  
                    LocalTime checkTime = meetingTimes.get(j);
                    
                    if(checkTime != startTime){
                        availableTimes.add(checkTime);
                    }
                    
                }
                meetingTimes = availableTimes;
                availableTimes = new ArrayList<>();
            }
        }
        convertMeetingTimes(meetingTimes);
    }
    
    public void convertMeetingTimes(List<LocalTime> meetingTimes){
        convertedMeetingTimes = new ArrayList<>();
        for (int i = 0; i < meetingTimes.size(); i++) {
            LocalTime time = meetingTimes.get(i);
            String convertedTime = time.toString();
            convertedMeetingTimes.add(convertedTime);
        }
    }
    
    public void createMeeting(){
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        UserBean ub = (UserBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "userBean");
        DBConnection dbc = new DBConnection();
        
        String formatAttendees = "{'ids': ['" + participant + "']}";
        
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        
        array.put(participant);
        json.put("ids", array);
        
        String formatDate = startDate.format(DateTimeFormatter.ofPattern("YY-MM-dd"));
        formatDate = "20" + formatDate;
        
        startTime = LocalTime.parse(convertedMeetingTime);
        endTime = startTime.plusHours(1);
        LocalDate localDate = LocalDate.parse(formatDate);
        
        dbc.addMeeting(formatDate, formatDate, convertedMeetingTime, endTime.toString(), title, description, location, ub.getUsername(), participant, json);
        addMeetingToCalendar(title, description, startTime, endTime, localDate);
        
    }
    
    public String addMeetingToCalendar(String title, String description, LocalTime startTime, LocalTime endTime, LocalDate date){
        
        //eventModel = new DefaultScheduleModel();
        
        LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime);
            
        event = DefaultScheduleEvent.builder()
                .title(title)
                .description(description)
                .startDate(startDateTime)
                .endDate(endDateTime)
                .build();
        eventModel.addEvent(event);
        
        eventModel = new DefaultScheduleModel();
        
        return "meetingCreated";
    }
    
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

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getToday() {
        Date date = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("yy-MM-dd");
        today = DateFor.format(date);
        return today;
    }

    public void Today(String today) {
        this.today = today;
    }

    public LocalTime getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(LocalTime meetingTime) {
        this.meetingTime = meetingTime;
    }

    public List<LocalTime> getMeetingTimes() {
        return meetingTimes;
    }

    public void setMeetingTimes(List<LocalTime> meetingTimes) {
        this.meetingTimes = meetingTimes;
    }

    public List<LocalTime> getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(List<LocalTime> availableTimes) {
        this.availableTimes = availableTimes;
    }

    public String getConvertedMeetingTime() {
        return convertedMeetingTime;
    }

    public void setConvertedMeetingTime(String convertedMeetingTime) {
        this.convertedMeetingTime = convertedMeetingTime;
    }

    public List<String> getConvertedMeetingTimes() {
        return convertedMeetingTimes;
    }

    public void setConvertedMeetingTimes(List<String> convertedMeetingTimes) {
        this.convertedMeetingTimes = convertedMeetingTimes;
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
