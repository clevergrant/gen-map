package com.clevergrant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clevergrant.exceptions.DataAccessException;
import com.clevergrant.model.AuthToken;

public class AuthTokenDao {

	private Connection conn;

	/**
	 * Creates a new AuthTokenDao with a database connection
	 *
	 * @param conn The predefined connection to the database
	 */
	public AuthTokenDao(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Takes an AuthToken Object and adds it to the database, then returns the object for saving to the client
	 *
	 * @param token The token for the user for which you want to add to the token database
	 * @return The AuthToken object representing the token
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public AuthToken create(AuthToken token) throws DataAccessException {
		String sql = "INSERT INTO AuthTokens (token, userName) " +
				"VALUES (?,?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, token.getToken());
			stmt.setString(2, token.getUserName());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem inserting the AuthToken");
		}
		return token;
	}

	/**
	 * Takes a token and tries to find it in the database
	 *
	 * @param token The token to search for in the table
	 * @return Username associated with the token
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public String query(String token) throws DataAccessException {
		String sql = "SELECT * FROM AuthTokens WHERE token = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, token);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getString("userName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem getting the AuthToken");
		}
		return null;
	}

	/**
	 * Takes a token and a userName and tells you if that token is already logged in
	 *
	 * @param token    The token to search for in the table
	 * @param userName The userName to search for in the table
	 * @return Boolean telling whether the user is already logged in
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public boolean isAuthenticated(String token, String userName) throws DataAccessException {
		String sql = "SELECT * FROM AuthTokens WHERE token = ? AND userName = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, token);
			stmt.setString(2, userName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next() && rs.getString("token").equals(token) && rs.getString("userName").equals(userName)) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem searching for the AuthToken");
		}
		return false;
	}

	/**
	 * Deletes all AuthTokens associated with a specific userName
	 *
	 * @param userName the userName of the user associated with the AuthTokens
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public void deleteByUsername(String userName) throws DataAccessException {
		String sql = "DELETE FROM AuthTokens WHERE userName = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, userName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem deleting the AuthTokens");
		}
	}

	/**
	 * Clears all records from the database
	 *
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public void clear() throws DataAccessException {
		String sql = "DELETE FROM AuthTokens";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem deleting the AuthTokens");
		}
	}
}
