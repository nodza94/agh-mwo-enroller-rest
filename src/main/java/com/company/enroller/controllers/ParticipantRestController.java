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
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants() {
		Collection<Participant> participants = participantService.getAll();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
	    Participant participant = participantService.findByLogin(login);
	if (participant == null) { 
	return new ResponseEntity(HttpStatus.NOT_FOUND);
	} 

	return new ResponseEntity<Participant>(participant, HttpStatus.OK); 
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST) 
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant){
		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
		if (foundParticipant != null) {
			return new ResponseEntity<String>("Unable to register. Participant with login " 
			+ participant.getLogin() + " already exists", HttpStatus.CONFLICT);
		}
		
		participantService.add(participant);
		return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
	    Participant participant = participantService.findByLogin(login);
	if (participant == null) { 
	return new ResponseEntity(HttpStatus.NOT_FOUND);
	} 
	
	participantService.delete(participant);
	return new ResponseEntity<Participant>(participant, HttpStatus.OK); 
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateParticipant(@PathVariable("id") String login, 
			@RequestBody Participant updateParticipant) {
	    Participant foundParticipant = participantService.findByLogin(login);
	    if (foundParticipant == null) {
	    	return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	    
	    foundParticipant.setPassword(updateParticipant.getPassword());
		participantService.update(foundParticipant);
		return new ResponseEntity<Participant>(foundParticipant, HttpStatus.OK);
	}
	
	//PREMIUM 4.3 List meetings for a user
	@RequestMapping(value = "/{id}/meetings", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipantMeetings(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		Collection<Meeting> meetings = participant.getMeetings();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}
}
