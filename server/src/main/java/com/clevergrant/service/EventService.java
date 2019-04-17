package com.clevergrant.service;

import java.sql.Connection;

import com.clevergrant.dao.AuthTokenDao;
import com.clevergrant.dao.Database;
import com.clevergrant.dao.EventDao;
import com.clevergrant.exceptions.DataAccessException;
import com.clevergrant.exceptions.NotAuthorizedException;
import com.clevergrant.model.Event;
import com.clevergrant.request.EventRequest;
import com.clevergrant.result.EventResult;

public class EventService {

	/**
	 * Returns a single Event object from the database with the specified ID
	 * Method: GET
	 * AuthToken required: Yes
	 *
	 * @param request Object containing the ID of the event requested and the credentials of the user making the com.clevergrant.request
	 * @return Object containing all of the relevant information on that event
	 * @throws DataAccessException Throws when database doesn't close properly
	 */
	public EventResult get(EventRequest request) throws DataAccessException {
		Database db = new Database();

		EventResult res;
		boolean commit = false;

		try {

			Connection conn = db.openConnection();
			AuthTokenDao authTokenDao = new AuthTokenDao(conn);

			String userName = authTokenDao.query(request.getToken());
			if (userName == null) throw new NotAuthorizedException();

			EventDao eventDao = new EventDao(conn);

			Event event = eventDao.query(request.getEventID());

			if (event.getDescendant().equals(userName)) {
				res = new EventResult(
						event.getDescendant(),
						event.getEventID(),
						event.getPersonID(),
						event.getLongitude(),
						event.getLatitude(),
						event.getCountry(),
						event.getCity(),
						event.getEventType(),
						event.getYear(),
						null
				);

				commit = true;
			} else {
				res = new EventResult(
						null,
						null,
						null,
						0,
						0,
						null,
						null,
						null,
						0,
						"You are not authorized to view this content."
				);
			}
		} catch (Exception e) {
			res = new EventResult(
					null,
					null,
					null,
					0,
					0,
					null,
					null,
					null,
					0,
					e.getMessage()
			);
		} finally {
			db.closeConnection(commit);
		}

		return res;
	}
}
