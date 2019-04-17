package com.clevergrant.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import com.clevergrant.dao.AuthTokenDao;
import com.clevergrant.dao.Database;
import com.clevergrant.dao.EventDao;
import com.clevergrant.dao.PersonDao;

import com.clevergrant.model.User;
import com.clevergrant.request.RegisterRequest;
import com.clevergrant.result.RegisterResult;

import static org.junit.Assert.*;

public class RegisterServiceTest {
	private Database db;

	private User user;

	@Before
	public void setUp() throws Exception {
		user = new User(
				"sheila",
				"parker",
				"sheila@parker.com",
				"Sheila",
				"Parker",
				"f",
				"Sheila_Parker"
		);

		db = new Database();
		db.createTables();
	}

	@After
	public void tearDown() throws Exception {
		db.clearTables();
	}

	@Test
	public void register() throws Exception {
		RegisterResult res = new RegisterService().register(
				new RegisterRequest(user.getUserName(),user.getPassword(),user.getEmail(),user.getFirstName(),user.getLastName(),user.getGender())
		);

		int numEvents = 0;
		int numPersons = 0;

		boolean isLogged = false;

		try {
			Connection conn = db.openConnection();
			numEvents = new EventDao(conn).queryByDescendant(res.getUserName()).length;
			numPersons = new PersonDao(conn).queryByDescendant(res.getUserName()).length;
			isLogged = new AuthTokenDao(conn).isAuthenticated(res.getToken(),res.getUserName());
			db.closeConnection(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertTrue(isLogged);
		assertEquals(numPersons, 31);
		assertEquals(numEvents, 91);
	}
}