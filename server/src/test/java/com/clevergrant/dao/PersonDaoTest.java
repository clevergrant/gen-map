package com.clevergrant.dao;

import org.junit.*;

import java.sql.Connection;

import static org.junit.Assert.*;

import com.clevergrant.exceptions.DataAccessException;
import com.clevergrant.model.Person;

public class PersonDaoTest {
	private Database db;
	private Person testPerson;
	private Person failPerson;
	private Person[] persons = new Person[3];

	@Before
	public void setUp() throws Exception {
		db = new Database();

		testPerson = new Person(
				"1",
				"clevergrant",
				"Grant",
				"Perdue",
				"m",
				"3",
				"4",
				"2"
		);

		persons[0] = new Person("2", "descendant", "person", "2", "f", "5", "6", "1");
		persons[1] = new Person("3", "descendant", "person", "3", "m", "7", "8", "4");
		persons[2] = testPerson;

		failPerson = new Person(
				"1",
				null,
				"Grant",
				"Perdue",
				"m",
				"3",
				"4",
				"2"
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
		PersonDao pDao = new PersonDao(conn);

		Person comparePerson;

		pDao.create(testPerson);

		comparePerson = pDao.query(testPerson.getPersonID());

		db.closeConnection(true);

		assertNotNull(comparePerson);
		assertEquals(testPerson, comparePerson);
	}

	@Test
	public void createFails() throws Exception {
		Connection conn = db.openConnection();
		PersonDao pDao = new PersonDao(conn);

		Person comparePerson = null;
		boolean fails = false;

		try {
			pDao.create(failPerson);
		} catch (Exception e) {
			fails = true;
			comparePerson = pDao.query(failPerson.getPersonID());
		}

		assertTrue(fails);
		assertNull(comparePerson);

		db.closeConnection(true);
	}

	@Test
	public void query() throws Exception {
		Connection conn = db.openConnection();
		PersonDao pDao = new PersonDao(conn);

		Person comparePerson;

		pDao.create(testPerson);

		comparePerson = pDao.query(testPerson.getPersonID());

		assertNotNull(comparePerson);
		assertEquals(testPerson, comparePerson);

		db.closeConnection(true);
	}

	@Test
	public void queryFails() throws Exception {
		Connection conn = db.openConnection();
		PersonDao pDao = new PersonDao(conn);

		Person comparePerson = null;
		boolean fails = false;

		pDao.create(testPerson);

		try {
			comparePerson = pDao.query("not1");
		} catch (DataAccessException e) {
			fails = true;
		}

		assertTrue((fails || comparePerson == null));

		db.closeConnection(true);
	}

	@Test
	public void clear() throws Exception {
		Connection conn = db.openConnection();
		PersonDao pDao = new PersonDao(conn);

		Person comparePerson;

		pDao.create(testPerson);
		comparePerson = pDao.query(testPerson.getPersonID());

		assertEquals(testPerson, comparePerson);

		pDao.clear();

		comparePerson = pDao.query(testPerson.getPersonID());

		assertNull(comparePerson);

		db.closeConnection(true);
	}

	@Test
	public void addAll() throws Exception {
		Connection conn = db.openConnection();

		PersonDao dao = new PersonDao(conn);

		dao.addAll(persons);

		assertEquals(persons[0], dao.query(persons[0].getPersonID()));
		assertEquals(persons[1], dao.query(persons[1].getPersonID()));
		assertEquals(persons[2], dao.query(persons[2].getPersonID()));

		db.closeConnection(true);
	}

	@Test
	public void queryByDescendant() throws Exception {
		Connection conn = db.openConnection();

		PersonDao dao = new PersonDao(conn);

		dao.addAll(persons);

		String test1 = persons[0].getDescendant();
		String test2 = persons[2].getDescendant();

		Person[] fromDb1 = dao.queryByDescendant(test1);
		Person[] fromDb2 = dao.queryByDescendant(test2);

		for (Person event : fromDb1)
			assertEquals(event.getDescendant(), persons[0].getDescendant());

		for (Person event : fromDb2)
			assertEquals(event.getDescendant(), persons[2].getDescendant());

		db.closeConnection(true);
	}

	@Test
	public void deleteByDescendant() throws Exception {
		Connection conn = db.openConnection();

		PersonDao dao = new PersonDao(conn);

		dao.addAll(persons);

		String test = persons[0].getDescendant();

		dao.deleteByDescendant(test);

		assertNull(dao.query(persons[0].getPersonID()));
		assertNull(dao.query(persons[1].getPersonID()));
		assertNotNull(dao.query(persons[2].getPersonID()));

		db.closeConnection(true);
	}
}