package com.company.enroller.controllers;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;

@RestController

@RequestMapping("/meetings")

public class MeetingRestController {

	@Autowired
	MeetingService meetingService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity<Meeting>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST) 
	public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting){
		Meeting foundParticipant = meetingService.findById(meeting.getId());
		if (foundParticipant != null) {
			return new ResponseEntity<String>(
					"Unable to register. Meeting " + meeting.getId() + " already exists", HttpStatus.CONFLICT);
		}
		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
	if (meeting == null) { 
	return new ResponseEntity(HttpStatus.NOT_FOUND);
	} 
	meetingService.delete(meeting);
	return new ResponseEntity<Meeting>(meeting, HttpStatus.OK); 
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMeeting(@PathVariable("id") long id, 
			@RequestBody Meeting updateMeeting) {
	    Meeting foundMeeting = meetingService.findById(id);
	    if (foundMeeting == null) {
	    	return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	    foundMeeting.setTitle(updateMeeting.getTitle());
	    foundMeeting.setDescription(updateMeeting.getDescription());
	    foundMeeting.setDate(updateMeeting.getDate());

	    meetingService.update(foundMeeting);
		return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK);
	}

	
	 @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	 public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id){ 
		 Meeting foundMeeting = meetingService.findById(id); 
		 if (foundMeeting == null) {
			 return new ResponseEntity<Meeting>(HttpStatus.NOT_FOUND); 
		 }
		 foundMeeting.getParticipants(); 
		 return new ResponseEntity<Meeting>(foundMeeting,HttpStatus.OK); 
	 }
	 

}