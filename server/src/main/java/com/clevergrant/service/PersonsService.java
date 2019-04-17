package com.clevergrant.service;

import java.sql.Connection;

import com.clevergrant.dao.AuthTokenDao;
import com.clevergrant.dao.Database;
import com.clevergrant.dao.PersonDao;
import com.clevergrant.exceptions.DataAccessException;
import com.clevergrant.exceptions.NotAuthorizedException;
import com.clevergrant.model.Person;
import com.clevergrant.request.PersonsRequest;
import com.clevergrant.result.PersonsResult;

public class PersonsService {

	/**
	 * Returns all family members of the current user. The current user is determined from the provided auth token.
	 * Method: GET
	 * AuthToken required: Yes
	 *
	 * @param request Object containing the credentials of the user making the com.clevergrant.request
	 * @return Object containing a list of all the Person Objects in the database
	 * @throws DataAccessException Throws when database doesn't close properly
	 */
	public PersonsResult getAll(PersonsRequest request) throws DataAccessException {
		Database db = new Database();

		PersonsResult res;
		boolean commit = false;

		try {
			Connection conn = db.openConnection();
			AuthTokenDao authTokenDao = new AuthTokenDao(conn);

			String userName = authTokenDao.query(request.getToken());
			if (userName == null) throw new NotAuthorizedException();

			PersonDao personDao = new PersonDao(conn);
			Person[] persons = personDao.queryByDescendant(userName);

			res = new PersonsResult(persons, null);

			commit = true;
		} catch (Exception e) {
			res = new PersonsResult(null, e.getMessage());
		} finally {
			db.closeConnection(commit);
		}

		return res;
	}
}
