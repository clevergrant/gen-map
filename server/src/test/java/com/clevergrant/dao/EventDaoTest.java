package com.clevergrant.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import com.clevergrant.model.Event;

import static org.junit.Assert.*;

public class EventDaoTest {

	private Database db;
	private Event[] testEvents = new Event[3];

	@Before
	public void setUp() throws Exception {
		db = new Database("FamMapTestDb");
		testEvents[0] = new Event(
				"Doing_Karate",
				"Yoshimi",
				"Gale123A",
				10.3f,
				10.3f,
				"Japan",
				"Ushiku",
				"Activity",
				2020);
		testEvents[1] = new Event(
				"Fighting_Robots",
				"Yoshimi",
				"Gale123A",
				10.4f,
				10.4f,
				"Japan",
				"Ushiku",
				"Activity",
				2020);
		testEvents[2] = new Event(
				"Surfing",
				"KingKamehameha",
				"kamehameha",
				15.4f,
				15.4f,
				"Hawaii",
				"Oahu",
				"Activity",
				1960);
		db.createTables();
	}

	@After
	public void tearDown() throws Exception {
		db.clearTables();
	}

	@Test
	public void create() throws Exception {
		Connection conn = db.openConnection();

		EventDao dao = new EventDao(conn);

		dao.create(testEvents[0]);

		Event fromDb = dao.query(testEvents[0].getEventID());

		assertNotNull(fromDb);
		assertEquals(testEvents[0], fromDb);

		db.closeConnection(true);
	}

	@Test
	public void addAll() throws Exception {
		Connection conn = db.openConnection();

		EventDao dao = new EventDao(conn);

		dao.addAll(testEvents);

		assertEquals(testEvents[0], dao.query(testEvents[0].getEventID()));
		assertEquals(testEvents[1], dao.query(testEvents[1].getEventID()));
		assertEquals(testEvents[2], dao.query(testEvents[2].getEventID()));

		db.closeConnection(true);
	}

	@Test
	public void query() throws Exception {
		Connection conn = db.openConnection();

		EventDao dao = new EventDao(conn);

		dao.create(testEvents[1]);

		Event fromDb = dao.query(testEvents[1].getEventID());

		assertNotNull(fromDb);
		assertEquals(testEvents[1], fromDb);

		db.closeConnection(true);
	}

	@Test
	public void queryByDescendant() throws Exception {
		Connection conn = db.openConnection();

		EventDao dao = new EventDao(conn);

		dao.addAll(testEvents);

		String test1 = testEvents[0].getDescendant();
		String test2 = testEvents[2].getDescendant();

		Event[] fromDb1 = dao.queryByDescendant(test1);
		Event[] fromDb2 = dao.queryByDescendant(test2);

		for (Event event : fromDb1)
			assertEquals(event.getDescendant(), testEvents[0].getDescendant());

		for (Event event : fromDb2)
			assertEquals(event.getDescendant(), testEvents[2].getDescendant());

		db.closeConnection(true);
	}

	@Test
	public void deleteByDescendant() throws Exception {
		Connection conn = db.openConnection();

		EventDao dao = new EventDao(conn);

		dao.addAll(testEvents);

		String test = testEvents[0].getDescendant();

		dao.deleteByDescendant(test);

		assertNull(dao.query(testEvents[0].getEventID()));
		assertNull(dao.query(testEvents[1].getEventID()));
		assertNotNull(dao.query(testEvents[2].getEventID()));

		db.closeConnection(true);
	}

	@Test
	public void clear() throws Exception {
		Connection conn = db.openConnection();

		EventDao dao = new EventDao(conn);

		dao.addAll(testEvents);

		dao.clear();

		assertNull(dao.query(testEvents[0].getEventID()));
		assertNull(dao.query(testEvents[1].getEventID()));
		assertNull(dao.query(testEvents[2].getEventID()));

		db.closeConnection(true);
	}
}