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
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	private MeetingService meetingService;
	private ParticipantService participantService;

	@Autowired
	public MeetingRestController(MeetingService meetingService, ParticipantService participantService) {
		this.meetingService = meetingService;
		this.participantService = participantService;
	}

	// Basic 1.1 List of all meetings
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	// Basic 1.2 Get a meeting by id
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity<Meeting>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	// Basic 1.3 Add a new meeting
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
		Meeting foundMeeting = meetingService.findById(meeting.getId());
		if (foundMeeting != null) {
			return new ResponseEntity<String>("Unable to register. Meeting " + meeting.getId() + 
					" already exists", HttpStatus.CONFLICT);
		}
		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}

	// Gold 3.1 Delete a meeting
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		meetingService.delete(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	// Gold 3.2 Update a meeting
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

	// Advanced 2.2 Get list of all meeting's participants
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findById(id);
		Collection<Participant> participants = meeting.getParticipants();
		if (meeting == null) {
			return new ResponseEntity<Meeting>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	// Advanced 2.1 Add a participant to a meeting
	@RequestMapping(value = "/{id}/{login}", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipant(@PathVariable("id") long meetingId, 
			@PathVariable("login") String login) {
		Meeting meeting = meetingService.findById(meetingId);
		Participant participant = participantService.findByLogin(login);

		if (meeting == null || participant == null) {
			return new ResponseEntity("Unable to update. Participant with login " + login + 
					" or meeting with id " + meetingId + " does not exist", HttpStatus.NOT_FOUND);
		}

		meeting.addParticipant(participant);
		meeting = meetingService.update(meeting);
		return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(), HttpStatus.OK);
	}
	
	// Gold 3.3 Remove participant from a meeting
	@RequestMapping(value = "/{id}/{login}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delParticipant(@PathVariable("id") long meetingId, 
			@PathVariable("login") String login) {
		
		Meeting meeting = meetingService.findById(meetingId);
		Participant participant = participantService.findByLogin(login);

		if (meeting == null || participant == null) {
			return new ResponseEntity("Unable to update. Participant with login " + login + 
					" or meeting with id " + meetingId + " does not exist", HttpStatus.NOT_FOUND);
		}

		meeting.removeParticipant(participant);
		meeting = meetingService.update(meeting);
		return new ResponseEntity<Collection<Participant>>(meeting.getParticipants(), HttpStatus.OK);
	}
}