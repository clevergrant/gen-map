package com.clevergrant.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import com.clevergrant.model.AuthToken;

import static org.junit.Assert.*;

public class AuthTokenDaoTest {

	private Database db;
	private AuthToken[] tokens = new AuthToken[3];

	@Before
	public void setUp() throws Exception {
		db = new Database("FamMapTestDb");
		tokens[0] = new AuthToken("username", "token1");
		tokens[1] = new AuthToken("username", "token2");
		tokens[2] = new AuthToken("username2", "token3");
		db.createTables();
	}

	@After
	public void tearDown() throws Exception {
		db.clearTables();
	}

	@Test
	public void create() throws Exception {
		Connection conn = db.openConnection();

		AuthTokenDao dao = new AuthTokenDao(conn);

		AuthToken test = tokens[0];

		dao.create(test);

		String fromDb = dao.query(test.getToken());

		assertNotNull(fromDb);
		assertEquals(fromDb, test.getUserName());

		db.closeConnection(true);
	}

	@Test
	public void query() throws Exception {
		Connection conn = db.openConnection();

		AuthTokenDao dao = new AuthTokenDao(conn);

		AuthToken test = tokens[1];

		dao.create(test);

		assertEquals(dao.query(test.getToken()), test.getUserName());

		db.closeConnection(true);
	}

	@Test
	public void isAuthenticated() throws Exception {
		Connection conn = db.openConnection();

		AuthTokenDao dao = new AuthTokenDao(conn);

		AuthToken test = tokens[0];

		dao.create(test);

		assertTrue(dao.isAuthenticated(test.getToken(), test.getUserName()));

		db.closeConnection(true);
	}

	@Test
	public void deleteByUsername() throws Exception {
		Connection conn = db.openConnection();

		AuthTokenDao dao = new AuthTokenDao(conn);

		for (AuthToken token : tokens)
			dao.create(token);

		dao.deleteByUsername(tokens[0].getUserName());

		assertFalse(dao.isAuthenticated(tokens[0].getToken(), tokens[0].getUserName()));
		assertFalse(dao.isAuthenticated(tokens[1].getToken(), tokens[1].getUserName()));

		db.closeConnection(true);
	}

	@Test
	public void clear() throws Exception {
		Connection conn = db.openConnection();

		AuthTokenDao dao = new AuthTokenDao(conn);

		for (AuthToken token : tokens)
			dao.create(token);

		dao.clear();

		assertFalse(dao.isAuthenticated(tokens[0].getToken(), tokens[0].getUserName()));
		assertFalse(dao.isAuthenticated(tokens[1].getToken(), tokens[1].getUserName()));
		assertFalse(dao.isAuthenticated(tokens[2].getToken(), tokens[2].getUserName()));

		db.closeConnection(true);
	}
}