/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.csci483.myprojectname.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author Rachel
 */
@ViewScoped
@Named("homeSchedule")
public class homeSchedule implements Serializable {
    
    private ScheduleModel eventModel;
    private ScheduleEvent<?> event = new DefaultScheduleEvent<>();
    
    @PostConstruct
    public void init() {
        
        eventModel = new DefaultScheduleModel();
        
        // TEST
        DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder()
				.title("Champions League Match")
				.startDate(LocalDateTime.now().withHour(20).withMinute(0))
				.endDate(LocalDateTime.now().withHour(23).withMinute(0))
				.description("Team A vs. Team B")
                                .build();
	eventModel.addEvent(event);
        
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
    
    public void addEvent() {
	if (event.getId() == null)
            eventModel.addEvent(event);
	else
            eventModel.updateEvent(event);

	event = new DefaultScheduleEvent<>();
    }
    
    public void onDateSelect(SelectEvent<LocalDateTime> selectEvent) {
	event = DefaultScheduleEvent.builder().startDate(selectEvent.getObject()).endDate(selectEvent.getObject().plusHours(1)).build();
    }
    
    public void onEventSelect(SelectEvent<ScheduleEvent<?>> selectEvent) {
	event = selectEvent.getObject();
    }
    
}
