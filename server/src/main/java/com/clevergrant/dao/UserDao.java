package com.clevergrant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clevergrant.exceptions.DataAccessException;
import com.clevergrant.model.User;

public class UserDao {

	private Connection conn;

	/**
	 * Creates a new UserDao with a database connection
	 *
	 * @param conn The predefined connection to the database
	 */
	public UserDao(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Creates a new record in the user table
	 *
	 * @param user Object containing the information of the user to be created
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public void create(User user) throws DataAccessException {

		User exists = query(user.getUserName());

		if (exists != null) throw new DataAccessException("User already exists.");

		if (user.getEmail() == null
				|| user.getPassword() == null
				|| user.getFirstName() == null
				|| user.getGender() == null
				|| user.getLastName() == null
				|| user.getPersonID() == null
				|| user.getUserName() == null
		) throw new DataAccessException("Invalid Input");

		String sql = "INSERT INTO Users (userName, password, email, firstName, lastName, gender, personID) "
				+ "VALUES (?,?,?,?,?,?,?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, user.getUserName());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getEmail());
			stmt.setString(4, user.getFirstName());
			stmt.setString(5, user.getLastName());
			stmt.setString(6, user.getGender());
			stmt.setString(7, user.getPersonID());
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem inserting the User");
		}
	}

	/**
	 * Creates multiple new records in the user table
	 *
	 * @param users Array of user objects to add to the database
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public int addAll(User[] users) throws DataAccessException {
		StringBuilder sql = new StringBuilder("INSERT INTO Users (userName, password, email, firstName, lastName, gender, personID) VALUES ");
		int size = users.length;
		int values = 7;
		for (int i = 0; i < size; i++) {
			sql.append("(?,?,?,?,?,?,?)");
			if (i < size - 1)
				sql.append(", ");
		}
		try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
			for (int i = 0; i < size; i++) {
				stmt.setString(((i * values) + 1), users[i].getUserName());
				stmt.setString(((i * values) + 2), users[i].getPassword());
				stmt.setString(((i * values) + 3), users[i].getEmail());
				stmt.setString(((i * values) + 4), users[i].getFirstName());
				stmt.setString(((i * values) + 5), users[i].getLastName());
				stmt.setString(((i * values) + 6), users[i].getGender());
				stmt.setString(((i * values) + 7), users[i].getPersonID());
			}
			stmt.executeUpdate();

			return size;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem inserting the Events");
		}
	}

	/**
	 * Takes a userName and tries to find it in the database
	 *
	 * @param userName Username of the user to return
	 * @return User object representing what was found in the database, or null if none is found
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public User query(String userName) throws DataAccessException {
		String sql = "SELECT * FROM Users WHERE  userName = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, userName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new User(
						rs.getString("userName"),
						rs.getString("password"),
						rs.getString("email"),
						rs.getString("firstName"),
						rs.getString("lastName"),
						rs.getString("gender"),
						rs.getString("personID")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem getting the User");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Takes a userName and password and tries to find it in the database
	 *
	 * @param userName Username of the user to check against
	 * @param password Password for the user to check against
	 * @return Whether the user exists in the database or not
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public User query(String userName, String password) throws DataAccessException {
		String sql = "SELECT * FROM Users WHERE userName = ? AND password = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, userName);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if (rs.next() && rs.getString("userName").equals(userName) && rs.getString("password").equals(password)) {
				return new User(
						rs.getString("userName"),
						rs.getString("password"),
						rs.getString("email"),
						rs.getString("firstName"),
						rs.getString("lastName"),
						rs.getString("gender"),
						rs.getString("personID")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was an error checking the database.");
		}

		return null;
	}

	/**
	 * Clears all records from the database
	 *
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public void clear() throws DataAccessException {
		String sql = "DELETE FROM Users";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem deleting the Users");
		}
	}
}
