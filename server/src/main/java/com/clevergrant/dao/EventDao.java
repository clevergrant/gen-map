package com.clevergrant.dao;

import com.clevergrant.exceptions.DataAccessException;
import com.clevergrant.model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EventDao {

	private Connection conn;

	/**
	 * Creates a new EventDao with a database connection
	 *
	 * @param conn The predefined connection to the database
	 */
	public EventDao(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Takes an Event Object and adds it to the database
	 *
	 * @param event Object containing info about the event to add
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public void create(Event event) throws DataAccessException {
		String sql = "INSERT INTO Events (eventID, descendant, personID, latitude, longitude, country, city, eventType, year) "
				+ "VALUES (?,?,?,?,?,?,?,?,?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, event.getEventID());
			stmt.setString(2, event.getDescendant());
			stmt.setString(3, event.getPersonID());
			stmt.setFloat(4, event.getLatitude());
			stmt.setFloat(5, event.getLongitude());
			stmt.setString(6, event.getCountry());
			stmt.setString(7, event.getCity());
			stmt.setString(8, event.getEventType());
			stmt.setInt(9, event.getYear());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException("There was a problem inserting the Event");
		}
	}

	/**
	 * Creates multiple new records in the event table
	 *
	 * @param events Array of event objects to add to the event table
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public int addAll(Event[] events) throws DataAccessException {
		StringBuilder sql = new StringBuilder("INSERT INTO Events (eventID, descendant, personID, latitude, longitude, country, city, eventType, year) VALUES ");
		int size = events.length;
		int values = 9;
		for (int i = 0; i < size; i++) {
			sql.append("(?,?,?,?,?,?,?,?,?)");
			if (i < size - 1)
				sql.append(", ");
		}
		try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
			for (int i = 0; i < size; i++) {
				stmt.setString(((i * values) + 1), events[i].getEventID());
				stmt.setString(((i * values) + 2), events[i].getDescendant());
				stmt.setString(((i * values) + 3), events[i].getPersonID());
				stmt.setFloat(((i * values) + 4), events[i].getLatitude());
				stmt.setFloat(((i * values) + 5), events[i].getLongitude());
				stmt.setString(((i * values) + 6), events[i].getCountry());
				stmt.setString(((i * values) + 7), events[i].getCity());
				stmt.setString(((i * values) + 8), events[i].getEventType());
				stmt.setInt(((i * values) + 9), events[i].getYear());
			}
			stmt.executeUpdate();
			return size;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem inserting the Events");
		}
	}

	/**
	 * Takes an Event Object and tries to find it in the database
	 *
	 * @param eventID The ID of the Event you want to return
	 * @return Event object representing what was found in the database, or null if none is found
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public Event query(String eventID) throws DataAccessException {
		String sql = "SELECT * FROM Events WHERE  eventID = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, eventID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new Event(
						rs.getString("eventID"),
						rs.getString("descendant"),
						rs.getString("personID"),
						rs.getFloat("latitude"),
						rs.getFloat("longitude"),
						rs.getString("country"),
						rs.getString("city"),
						rs.getString("eventType"),
						rs.getInt("year")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem getting the Event");
		}
		return null;
	}

	/**
	 * Gets all records of all events in the database associated with a specific descendant
	 *
	 * @param username The username of the User associated with the events
	 * @return Array of Event objects found associated with, or null if none is found
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public Event[] queryByDescendant(String username) throws DataAccessException {
		String sql = "SELECT * FROM Events WHERE descendant = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs != null) {

				ArrayList<Event> al = new ArrayList<>();

				while (rs.next())
					al.add(new Event(
							rs.getString("eventID"),
							rs.getString("descendant"),
							rs.getString("personID"),
							rs.getFloat("latitude"),
							rs.getFloat("longitude"),
							rs.getString("country"),
							rs.getString("city"),
							rs.getString("eventType"),
							rs.getInt("year"))
					);

				return al.toArray(new Event[0]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem getting the Events");
		}
		return null;
	}

	/**
	 * Deletes all events associated with a specific username
	 *
	 * @param username the username of the user associated with the events
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public void deleteByDescendant(String username) throws DataAccessException {
		String sql = "DELETE FROM Events WHERE descendant = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem deleting the Events");
		}
	}

	/**
	 * Clears all records from the database
	 *
	 * @throws DataAccessException Throws when it catches a SQLException
	 */
	public void clear() throws DataAccessException {
		String sql = "DELETE FROM Events";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("There was a problem deleting the Events");
		}
	}
}
