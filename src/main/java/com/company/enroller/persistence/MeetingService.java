package com.company.enroller.persistence;

import java.util.Collection;

import javax.persistence.JoinColumn;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

@Component("meetingService")
public class MeetingService {

	Session session;

	public MeetingService() {
		session = DatabaseConnector.getInstance().getSession();
	}
	//Basic 1.1 List of all meetings
	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = session.createQuery(hql);
		return query.list();
	}
	//Basic 1.2 Get a meeting by id
	public Meeting findById(long id) {
		return (Meeting) session.get(Meeting.class, id);
	}
	//Basic 1.3 Add a new meeting
	public Meeting add(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.save(meeting);
		transaction.commit();
		return meeting;
	}
	//Gold 3.1 Delete a meeting
	public void delete(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.delete(meeting);
		transaction.commit();
		
	}
	//Gold 3.2 Update a meeting 
	public Meeting update(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.update(meeting);
		transaction.commit();
		return meeting;
	}
	
}
