package com.clevergrant.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.clevergrant.exceptions.DataAccessException;

public class Database {
	private Connection conn;
	private String dbName;

	static {
		try {
			final String driver = "org.sqlite.JDBC";
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets database to specified name
	 *
	 * @param dbName Name of the database to use
	 */
	public Database(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * Sets the database to the default database
	 */
	public Database() {
		this.dbName = "FamMapDb";
	}

	/**
	 * Opens a connection to the database
	 *
	 * @return Object referencing opened connection
	 * @throws DataAccessException Throws if an SQLException throws
	 */
	public Connection openConnection() throws DataAccessException {
		try {
			final String CONNECTION_URL = "jdbc:sqlite:C:/repos/cs240/FamMap/server/src/main/Assets/" + this.dbName + ".db";
			conn = DriverManager.getConnection(CONNECTION_URL);
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("Could not connect to database");
		}

		return conn;
	}

	/**
	 * Closes the connection to the database
	 *
	 * @param commit Specify if you want to commit the changes or not
	 * @throws DataAccessException Throws if an SQLException throws
	 */
	public void closeConnection(boolean commit) throws DataAccessException {
		try {
			if (commit)
				conn.commit();
			else
				conn.rollback();

			conn.close();
			conn = null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("Could not close database connection");
		}
	}

	/**
	 * Initializes the database with the necessary tables
	 *
	 * @throws DataAccessException Throws if an SQLException throws
	 */
	public void createTables() throws DataAccessException {
		openConnection();
		boolean commit = false;
		try (Statement stmt = conn.createStatement()) {

			String sql = "CREATE TABLE IF NOT EXISTS Users (\n" +
					"    userName TEXT NOT NULL UNIQUE,\n" +
					"    password TEXT NOT NULL,\n" +
					"    email TEXT NOT NULL,\n" +
					"    firstName TEXT NOT NULL,\n" +
					"    lastName TEXT NOT NULL,\n" +
					"    gender TEXT NOT NULL,\n" +
					"    personID TEXT NOT NULL,\n" +
					"    PRIMARY KEY (userName),\n" +
					"    FOREIGN KEY (personID) REFERENCES Persons(personID)\n" +
					");\n" +
					"CREATE TABLE IF NOT EXISTS Persons (\n" +
					"    personID TEXT NOT NULL UNIQUE,\n" +
					"    descendant TEXT NOT NULL,\n" +
					"    firstName TEXT NOT NULL,\n" +
					"    lastName TEXT NOT NULL,\n" +
					"    gender TEXT NOT NULL,\n" +
					"    father TEXT NULL,\n" +
					"    mother TEXT NULL,\n" +
					"    spouse TEXT NULL,\n" +
					"    PRIMARY KEY (personID),\n" +
					"    FOREIGN KEY (descendant) REFERENCES Users(userName),\n" +
					"    FOREIGN KEY (father) REFERENCES Persons(personID),\n" +
					"    FOREIGN KEY (mother) REFERENCES Persons(personID),\n" +
					"    FOREIGN KEY (spouse) REFERENCES Persons(personID)\n" +
					");\n" +
					"CREATE TABLE IF NOT EXISTS AuthTokens (\n" +
					"    token TEXT NOT NULL UNIQUE,\n" +
					"    userName TEXT NOT NULL,\n" +
					"    PRIMARY KEY (token),\n" +
					"    FOREIGN KEY (userName) REFERENCES Users(userName)\n" +
					");\n" +
					"CREATE TABLE IF NOT EXISTS Events (\n" +
					"    eventID TEXT NOT NULL UNIQUE,\n" +
					"    descendant TEXT NOT NULL,\n" +
					"    personID TEXT NOT NULL,\n" +
					"    latitude FLOAT NOT NULL,\n" +
					"    longitude FLOAT NOT NULL,\n" +
					"    country TEXT NOT NULL,\n" +
					"    city TEXT NOT NULL,\n" +
					"    eventType TEXT NOT NULL,\n" +
					"    year INT NOT NULL,\n" +
					"    PRIMARY KEY (eventID),\n" +
					"    FOREIGN KEY (descendant) REFERENCES Users(userName),\n" +
					"    FOREIGN KEY (personID) REFERENCES Persons(personID)\n" +
					");";

			stmt.executeUpdate(sql);
			commit = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataAccessException("Could not create tables");
		} finally {
			closeConnection(commit);
		}
	}

	/**
	 * Initializes the database with the necessary tables, and adds dummy com.clevergrant.data for testing
	 *
	 * @throws DataAccessException Throws if an SQLException throws
	 */
	public void createDummyTables() throws DataAccessException {
		createTables();

		openConnection();
		boolean commit = false;
		try (Statement stmt = conn.createStatement()) {

			String sql = "INSERT INTO Events (eventID, descendant, personID, latitude, longitude, country, city, eventType, `year`)\n" +
					"VALUES ('1','userName','1',1,1,'A Country','A City','Birth',1992);\n" +
					"INSERT INTO AuthTokens (token, userName)\n" +
					"VALUES ('1','userName');\n" +
					"INSERT INTO Persons (personID, descendant, firstName, lastName, gender, father, mother, spouse)\n" +
					"VALUES ('1','userName','Grant','Perdue','m','2','3','4');\n" +
					"INSERT INTO Users (userName, password, email, firstName, lastName, gender, personID)\n" +
					"VALUES ('userName','password','clever@grant.me','Grant','Perdue','m','1');";

			stmt.executeUpdate(sql);
			commit = true;
		} catch (Exception e) {
			throw new DataAccessException("Could not populate tables");
		} finally {
			closeConnection(commit);
		}
	}

	/**
	 * Clears all tables of all rows
	 *
	 * @throws DataAccessException Throws if an SQLException throws
	 */
	public void clearTables() throws DataAccessException {
		openConnection();
		boolean commit = false;
		try (Statement stmt = conn.createStatement()) {
			String sql = "DELETE FROM Users;"
					+ "DELETE FROM AuthTokens;"
					+ "DELETE FROM Persons;"
					+ "DELETE FROM Events;";
			stmt.executeUpdate(sql);
			commit = true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException("Could not clear tables");
		} finally {
			closeConnection(commit);
		}
	}
}
