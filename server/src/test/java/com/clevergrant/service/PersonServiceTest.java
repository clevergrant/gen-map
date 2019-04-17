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
import com.clevergrant.request.LoginRequest;
import com.clevergrant.request.PersonRequest;
import com.clevergrant.result.LoginResult;
import com.clevergrant.result.PersonResult;

import static org.junit.Assert.*;

public class PersonServiceTest {
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
				new LoginRequest("sheila", "parker")
		);

		AuthToken token = new AuthToken(loginResult.getUserName(), loginResult.getToken());

		PersonResult res1 = new PersonService().get(
				new PersonRequest(persons[0].getPersonID(), token.getToken())
		);
		PersonResult res2 = new PersonService().get(
				new PersonRequest(persons[1].getPersonID(), token.getToken())
		);
		PersonResult res3 = new PersonService().get(
				new PersonRequest(persons[2].getPersonID(), token.getToken())
		);

		Person p1 = new Person(res1.getPersonID(), res1.getDescendant(), res1.getFirstName(), res1.getLastName(), res1.getGender(), res1.getFather(), res1.getMother(), res1.getSpouse());
		Person p2 = new Person(res2.getPersonID(), res2.getDescendant(), res2.getFirstName(), res2.getLastName(), res2.getGender(), res2.getFather(), res2.getMother(), res2.getSpouse());
		Person p3 = new Person(res3.getPersonID(), res3.getDescendant(), res3.getFirstName(), res3.getLastName(), res3.getGender(), res3.getFather(), res3.getMother(), res3.getSpouse());

		assertEquals(p1, persons[0]);
		assertEquals(p2, persons[1]);
		assertEquals(p3, persons[2]);
	}
}