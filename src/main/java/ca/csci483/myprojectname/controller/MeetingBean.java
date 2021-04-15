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
import java.util.Date;
import java.util.List;
import javax.el.ELContext;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 * This class...
 * 
 */
@SessionScoped
@Named("meetingBean")
public class MeetingBean implements Serializable{
    private String meetingId;       // unique ID of the meeting
    private LocalDate startDate;    // start date of the meeting
    private LocalDate endDate;      // end date of the meeting
    private LocalTime startTime;    // start time of the meeting
    private LocalTime endTime;      // end time of the meeting
    private String title;           // title of the meeting
    private String description;     // description of the meeting
    private String location;        // location of the meeting
    private String chairPerson;     // chairperson of the meeting
    private String attendees;       // all attendees of the meeting (chairperson + participants)
    private String participant;     // participants of the meeting
    private String today;           // todays date
    
    private LocalTime meetingTime;              // time of meeting (hh:mm:ss)       
    private String convertedMeetingTime;        // time of meeting ("hh:mm:ss")
    private List<LocalTime> meetingTimes;       // list of all meeting times (hh:mm:ss)
    private List<String> convertedMeetingTimes; // list of all meeting times ("hh:mm:ss")
    private List<LocalTime> availableTimes;     // list of all available meeting times
    
    private ScheduleModel eventModel;
    private ScheduleEvent<?> event = new DefaultScheduleEvent<>();
    
    public MeetingBean(){
    }
    
    /**
     * Function to find meeting times that work for both the chairperson and the
     * participant
     *
     * @param date: date of the meeting
     * @param username: username of the participant of the meeting
     */
    public void findAvailabilities(LocalDate date, String username){
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        UserBean ub = (UserBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "userBean");
        DBConnection dbc = new DBConnection();
        
        meetingTimes = Arrays.asList(LocalTime.of(8,00,00), LocalTime.of(9,00,00), LocalTime.of(10,00,00), LocalTime.of(11,00,00),
                                     LocalTime.of(12,00,00), LocalTime.of(13,00,00), LocalTime.of(14,00,00), LocalTime.of(15,00,00),
                                     LocalTime.of(16,00,00), LocalTime.of(17,00,00));
        String formattedDate = date.format(DateTimeFormatter.ofPattern("00YY-MM-dd"));
        
        if (dbc.findUser(username) == true){
            
            // find available times for chairperson
            List<LocalTime> availableTimes = new ArrayList<>();
            for (int i = 0; i < ub.getAllUserMeetings().size(); i++) {

                Meeting currentMeeting = ub.getAllUserMeetings().get(i);
                String mFormattedDate = currentMeeting.getStartDate().format(DateTimeFormatter.ofPattern("00YY-MM-dd"));

                if(formattedDate.equals(mFormattedDate)){
                    LocalTime startTime = currentMeeting.getStartTime();
                    LocalTime endTime = currentMeeting.getEndTime();

                    for (int j = 0; j < meetingTimes.size(); j++){
                        LocalTime checkTime = meetingTimes.get(j);

                        if ((checkTime != startTime) && !availableTimes.contains(checkTime)){
                            availableTimes.add(checkTime);
                        }

                    }
                    meetingTimes = availableTimes;
                    availableTimes = new ArrayList<>();
                }

            }

            // find available times for participant
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
        else {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Error", "This user does not exist!"));
        }
        
    }
    
    /**
     * Function to convert the meeting times from a list of LocalTime variables 
     * to a list of String variables. 
     *
     * @param meetingTimes: List of meeting times
     */
    public void convertMeetingTimes(List<LocalTime> meetingTimes){
        convertedMeetingTimes = new ArrayList<>();
        for (int i = 0; i < meetingTimes.size(); i++) {
            LocalTime time = meetingTimes.get(i);
            String convertedTime = time.toString();
            convertedMeetingTimes.add(convertedTime);
        }
    }
    
    /**
     * Function to create a new meeting.
     */
    public void createMeeting(){
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        UserBean ub = (UserBean) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null, "userBean");
        DBConnection dbc = new DBConnection();
        
        // FIX THIS - attendees = chairperson + participant
        // format the list of attendees
        String formatAttendees = "{'ids': ['" + participant + "']}";
        
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        
        array.put(participant);
        json.put("ids", array);
        
        // format the start/end date
        String formatDate = startDate.format(DateTimeFormatter.ofPattern("YY-MM-dd"));
        formatDate = "20" + formatDate;
        
        startTime = LocalTime.parse(convertedMeetingTime);
        endTime = startTime.plusHours(1);
        LocalDate localDate = LocalDate.parse(formatDate);
        
        dbc.addMeeting(formatDate, formatDate, convertedMeetingTime, endTime.toString(), title, description, location, ub.getUsername(), participant, json);
        addMeetingToCalendar(title, description, startTime, endTime, localDate);
        resetMeetingValues();
    }
    
    /**
     * Function to reset meeting values after a meeting has been successfully 
     * scheduled.
     *
     */
    public void resetMeetingValues() {
        meetingId = null;
        startDate = null;
        endDate = null;
        startTime = null;
        endTime = null;
        title = null;
        description = null;
        location = null;
        chairPerson = null;
        attendees = null;
        participant = null;
        meetingTimes = null;
        availableTimes = null;
    }
    
    /**
     * Function to add a new meeting to the calendar.
     * 
     * @param title: title of the new meeting
     * @param description: description of the new meeting
     * @param startTime: start time of the new meeting
     * @param endTime: end time of the new meeting
     * @param date: date of the new meeting
     * 
     * @return String representing the name of page to be rendered
     */
    public String addMeetingToCalendar(String title, String description, LocalTime startTime, LocalTime endTime, LocalDate date){
        
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
