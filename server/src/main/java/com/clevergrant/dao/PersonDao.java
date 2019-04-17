package com.clevergrant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.clevergrant.exceptions.DataAccessException;
import com.clevergrant.model.Person;

public class PersonDao {

	private Connection conn;

	/**
	 * Creates a new PersonDao with a database connection
	 *
	 * @param conn The predefined connection to the database
	 */
	public PersonDao(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Creates a new person record in the person table
	 *
	 * @param person Object containing information about the person to add
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public void create(Person person) throws DataAccessException {
		String sql = "INSERT INTO Persons (personID, descendant, firstName, lastName, gender, father, mother, spouse) "
				+ "VALUES (?,?,?,?,?,?,?,?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, person.getPersonID());
			stmt.setString(2, person.getDescendant());
			stmt.setString(3, person.getFirstName());
			stmt.setString(4, person.getLastName());
			stmt.setString(5, person.getGender());
			stmt.setString(6, person.getFather());
			stmt.setString(7, person.getMother());
			stmt.setString(8, person.getSpouse());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException("There was a problem inserting the Person");
		}
	}

	/**
	 * Creates multiple new records in the person table
	 *
	 * @param persons Array of person objects to add to the database
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public int addAll(Person[] persons) throws DataAccessException {
		StringBuilder sql = new StringBuilder("INSERT INTO Persons (personID, descendant, firstName, lastName, gender, father, mother, spouse) VALUES ");
		int size = persons.length;
		int values = 8;
		for (int i = 0; i < size; i++) {
			sql.append("(?,?,?,?,?,?,?,?)");
			if (i < size - 1)
				sql.append(", ");
		}
		try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
			for (int i = 0; i < size; i++) {
				stmt.setString(((i * values) + 1), persons[i].getPersonID());
				stmt.setString(((i * values) + 2), persons[i].getDescendant());
				stmt.setString(((i * values) + 3), persons[i].getFirstName());
				stmt.setString(((i * values) + 4), persons[i].getLastName());
				stmt.setString(((i * values) + 5), persons[i].getGender());
				stmt.setString(((i * values) + 6), persons[i].getFather());
				stmt.setString(((i * values) + 7), persons[i].getMother());
				stmt.setString(((i * values) + 8), persons[i].getSpouse());
			}
			stmt.executeUpdate();

			return size;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem inserting the Persons");
		}
	}

	/**
	 * Takes a personID and queries the database for the record
	 *
	 * @param personID ID of the person's record you want to find
	 * @return Person object representing what was found in the database, or null if none is found
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public Person query(String personID) throws DataAccessException {
		String sql = "SELECT * FROM Persons WHERE  personID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, personID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				return new Person(
						rs.getString("personID"),
						rs.getString("descendant"),
						rs.getString("firstName"),
						rs.getString("lastName"),
						rs.getString("gender"),
						rs.getString("father"),
						rs.getString("mother"),
						rs.getString("spouse")
				);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem getting the Person");
		}
		return null;
	}

	/**
	 * Takes a username and queries the database for the record
	 *
	 * @param username Username of the person's record you want to find
	 * @return Person object representing what was found in the database, or null if none is found
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public Person[] queryByDescendant(String username) throws DataAccessException {
		String sql = "SELECT * FROM Persons WHERE  descendant = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs != null) {
				ArrayList<Person> al = new ArrayList<>();
				while (rs.next())
					al.add(new Person(
							rs.getString("personID"),
							rs.getString("descendant"),
							rs.getString("firstName"),
							rs.getString("lastName"),
							rs.getString("gender"),
							rs.getString("father"),
							rs.getString("mother"),
							rs.getString("spouse"))
					);

				return al.toArray(new Person[0]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem getting the Person");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Deletes all Persons associated with a specific username
	 *
	 * @param username the username of the user associated with the Persons
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public void deleteByDescendant(String username) throws DataAccessException {
		String sql = "DELETE FROM Persons WHERE descendant = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem deleting the Persons");
		}
	}

	/**
	 * Clears all records from the database
	 *
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public void clear() throws DataAccessException {
		String sql = "DELETE FROM Persons";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem deleting the Persons");
		}
	}
}
