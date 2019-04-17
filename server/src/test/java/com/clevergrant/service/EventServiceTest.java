package com.clevergrant.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import com.clevergrant.dao.Database;
import com.clevergrant.dao.EventDao;
import com.clevergrant.dao.PersonDao;
import com.clevergrant.dao.UserDao;
import com.clevergrant.model.AuthToken;
import com.clevergrant.model.Event;
import com.clevergrant.model.Person;
import com.clevergrant.model.User;
import com.clevergrant.request.EventRequest;
import com.clevergrant.request.LoginRequest;
import com.clevergrant.result.EventResult;
import com.clevergrant.result.LoginResult;

import static org.junit.Assert.*;

public class EventServiceTest {
	private Database db;

	private User[] users = new User[1];
	private Person[] persons = new Person[3];
	private Event[] events = new Event[2];

	@Before
	public void setUp() throws Exception {
		users[0] = new User(
				"sheila",
				"parker",
				"sheila@parker.com",
				"Sheila",
				"Parker",
				"f",
				"Sheila_Parker"
		);

		persons[0] = new Person(
				"Sheila_Parker",
				"sheila",
				"Sheila",
				"Parker",
				"f",
				"Patrick_Spencer",
				"Im_really_good_at_names",
				null
		);
		persons[1] = new Person(
				"Patrick_Spencer",
				"sheila",
				"Patrick",
				"Spencer",
				"m",
				null,
				null,
				"Im_really_good_at_names"
		);
		persons[2] = new Person(
				"Im_really_good_at_names",
				"sheila",
				"CS240",
				"JavaRocks",
				"f",
				null,
				null,
				"Patrick_Spencer"
		);

		events[0] = new Event(
				"Sheila_Family_Map",
				"sheila",
				"Sheila_Parker",
				40,
				-111,
				"United States",
				"Salt Lake City",
				"started family map",
				2016
		);
		events[1] = new Event(
				"I_hate_formatting",
				"sheila",
				"Patrick_Spencer",
				40,
				-111,
				"United States",
				"Provo",
				"fixed this thing",
				2017
		);

		db = new Database();
		db.createTables();

		try {
			Connection conn = db.openConnection();
			new UserDao(conn).addAll(users);
			new PersonDao(conn).addAll(persons);
			new EventDao(conn).addAll(events);
			db.closeConnection(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@After
	public void tearDown() throws Exception {
		db.clearTables();
	}

	@Test
	public void get() throws Exception {

		LoginResult loginResult = new LoginService().login(
				new LoginRequest("sheila","parker")
		);

		AuthToken token = new AuthToken(loginResult.getUserName(),loginResult.getToken());

		EventResult res = new EventService().get(
				new EventRequest(events[0].getEventID(), token.getToken())
		);

		Event e = new Event(res.getEventID(),res.getDescendant(),res.getPersonID(),res.getLatitude(),res.getLongitude(),res.getCountry(),res.getCity(),res.getEventType(),res.getYear());

		assertEquals(e, events[0]);
	}

}