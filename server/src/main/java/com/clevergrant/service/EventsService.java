package com.clevergrant.service;

import java.sql.Connection;

import com.clevergrant.dao.AuthTokenDao;
import com.clevergrant.dao.Database;
import com.clevergrant.dao.EventDao;
import com.clevergrant.exceptions.DataAccessException;
import com.clevergrant.exceptions.NotAuthorizedException;
import com.clevergrant.model.Event;
import com.clevergrant.request.EventsRequest;
import com.clevergrant.result.EventsResult;

public class EventsService {
	/**
	 * Returns all events for all family members of the current user. The current user is determined from the provided auth token.
	 * Method: GET
	 * AuthToken required: Yes
	 *
	 * @param request Object containing the credentials of the user making the com.clevergrant.request
	 * @return Object containing a list of all Event Objects stored in the database
	 * @throws DataAccessException Throws when database doesn't close properly
	 */
	public EventsResult getAll(EventsRequest request) throws DataAccessException {
		Database db = new Database();

		EventsResult res;
		boolean commit = false;

		try {
			Connection conn = db.openConnection();
			AuthTokenDao authTokenDao = new AuthTokenDao(conn);

			String userName = authTokenDao.query(request.getToken());
			if (userName == null) throw new NotAuthorizedException();

			EventDao eventDao = new EventDao(conn);
			Event[] events = eventDao.queryByDescendant(userName);

			res = new EventsResult(events, null);

			commit = true;
		} catch (Exception e) {
			res = new EventsResult(null, e.getMessage());
		} finally {
			db.closeConnection(commit);
		}

		return res;
	}
}
