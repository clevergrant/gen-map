package com.clevergrant.dao;

import org.junit.*;

import java.sql.Connection;

import static org.junit.Assert.*;

import com.clevergrant.exceptions.DataAccessException;
import com.clevergrant.model.User;

public class UserDaoTest {
	private Database db;
	private User testUser;
	private User failUser;
	private User[] users = new User[3];

	@Before
	public void setUp() throws Exception {
		db = new Database();

		testUser = new User(
				"clevergrant",
				"123456",
				"clever@grant.com",
				"Grant",
				"Perdue",
				"m",
				"1"
		);

		users[0] = new User("bonnie", "password", "email", "Bonnie", "Thief", "m","2");
		users[1] = new User("clyde", "password", "email", "Clyde", "Thief", "f","3");
		users[2] = testUser;

		failUser = new User(
				null,
				"123456",
				"clever@grant.com",
				"Grant",
				"Perdue",
				"m",
				"1"
		);

		db.createTables();
	}

	@After
	public void tearDown() throws Exception {
		db.clearTables();
	}

	@Test
	public void create() throws Exception {
		Connection conn = db.openConnection();
		UserDao dao = new UserDao(conn);

		User compareUser;

		dao.create(testUser);

		compareUser = dao.query(testUser.getUserName());

		assertNotNull(compareUser);
		assertEquals(testUser, compareUser);

		db.closeConnection(true);
	}

	@Test
	public void createFails() throws Exception {
		Connection conn = db.openConnection();
		UserDao userDao = new UserDao(conn);

		User compareUser = null;
		boolean fails = false;

		try {
			userDao.create(failUser);
		} catch (Exception e) {
			fails = true;
			compareUser = userDao.query(failUser.getUserName());
		}

		assertTrue(fails);
		assertNull(compareUser);

		db.closeConnection(true);
	}

	@Test
	public void query() throws Exception {
		Connection conn = db.openConnection();
		UserDao pDao = new UserDao(conn);

		User compareUser;

		pDao.create(testUser);

		compareUser = pDao.query(testUser.getUserName());

		assertNotNull(compareUser);
		assertEquals(testUser, compareUser);

		db.closeConnection(true);
	}

	@Test
	public void queryFails() throws Exception {
		Connection conn = db.openConnection();
		UserDao pDao = new UserDao(conn);

		User compareUser = null;
		boolean fails = false;

		pDao.create(testUser);

		try {
			compareUser = pDao.query("not1");
		} catch (DataAccessException e) {
			fails = true;
		}

		assertTrue((fails || compareUser == null));

		db.closeConnection(true);
	}

	@Test
	public void clear() throws Exception {
		Connection conn = db.openConnection();
		UserDao pDao = new UserDao(conn);

		User compareUser = null;

		pDao.create(testUser);
		compareUser = pDao.query(testUser.getUserName());

		assertEquals(testUser, compareUser);

		pDao.clear();

		compareUser = pDao.query(testUser.getUserName());

		assertNull(compareUser);

		db.closeConnection(true);
	}

	@Test
	public void addAll() throws Exception {
		Connection conn = db.openConnection();

		UserDao dao = new UserDao(conn);

		dao.addAll(users);

		assertEquals(users[0], dao.query(users[0].getUserName()));
		assertEquals(users[1], dao.query(users[1].getUserName()));
		assertEquals(users[2], dao.query(users[2].getUserName()));

		db.closeConnection(true);
	}
}