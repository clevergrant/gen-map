package com.clevergrant.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import com.clevergrant.dao.AuthTokenDao;
import com.clevergrant.dao.Database;
import com.clevergrant.dao.UserDao;

import com.clevergrant.model.User;
import com.clevergrant.request.LoginRequest;
import com.clevergrant.result.LoginResult;

import static org.junit.Assert.*;

public class LoginServiceTest {
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

		try {
			Connection conn = db.openConnection();
			new UserDao(conn).create(user);
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
	public void login() throws Exception {
		LoginResult res = new LoginService().login(
				new LoginRequest(user.getUserName(),user.getPassword())
		);

		boolean logged = false;

		try {
			Connection conn = db.openConnection();
			logged = new AuthTokenDao(conn).isAuthenticated(res.getToken(),res.getUserName());
			db.closeConnection(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertTrue(logged);
	}
}