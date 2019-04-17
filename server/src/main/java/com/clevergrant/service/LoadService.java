package com.clevergrant.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;

import com.clevergrant.dao.Database;
import com.clevergrant.dao.EventDao;
import com.clevergrant.dao.PersonDao;
import com.clevergrant.dao.UserDao;
import com.clevergrant.exceptions.DataAccessException;
import com.clevergrant.request.LoadRequest;
import com.clevergrant.result.LoadResult;

public class LoadService {
	/**
	 * Clears all com.clevergrant.data from the database and then loads the posted user, person, and event com.clevergrant.data into the database.
	 * Method: POST
	 * AuthToken required: No
	 *
	 * @param request Object containing lists of users, persons, and events to add to the database
	 * @return Object with a message reporting how many users, persons, and events were added to the database
	 * @throws DataAccessException Throws when database doesn't close properly
	 */
	public LoadResult load(LoadRequest request) throws DataAccessException {
		Database db = new Database();

		LoadResult res;
		boolean commit = false;

		try {
			Connection conn = db.openConnection();

			new ClearService().clear();

			int usersAdded = 0;
			if (request.getUsers().length > 0)
				usersAdded = new UserDao(conn).addAll(request.getUsers());

			int personsAdded = 0;
			if (request.getPersons().length > 0)
				personsAdded = new PersonDao(conn).addAll(request.getPersons());

			int eventsAdded = 0;
			if (request.getEvents().length > 0)
				eventsAdded = new EventDao(conn).addAll(request.getEvents());

			res = new LoadResult(
					usersAdded,
					personsAdded,
					eventsAdded,
					null
			);

			commit = true;
		} catch (Exception e) {
			res = new LoadResult(
					0,
					0,
					0,
					e.getMessage()
			);
		} finally {
			db.closeConnection(commit);
		}

		return res;
	}

	private String readString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStreamReader sr = new InputStreamReader(is);
		char[] buf = new char[1024];
		int len;
		while ((len = sr.read(buf)) > 0) {
			sb.append(buf, 0, len);
		}
		return sb.toString();
	}
}
