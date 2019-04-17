package com.clevergrant.service;

import java.sql.Connection;

import com.clevergrant.dao.AuthTokenDao;
import com.clevergrant.dao.Database;
import com.clevergrant.dao.PersonDao;
import com.clevergrant.exceptions.DataAccessException;
import com.clevergrant.exceptions.NotAuthorizedException;
import com.clevergrant.model.Person;
import com.clevergrant.request.PersonRequest;
import com.clevergrant.result.PersonResult;

public class PersonService {

	/**
	 * Gets a single person object from the database with the specified ID
	 * Method: GET
	 * AuthToken required: Yes
	 *
	 * @param request Object containing the personID requested
	 * @return Object containing all the relevant information for a person object
	 * @throws DataAccessException Throws when database doesn't close properly
	 */
	public PersonResult get(PersonRequest request) throws DataAccessException {
		Database db = new Database();

		PersonResult res;
		boolean commit = false;

		try {

			Connection conn = db.openConnection();
			AuthTokenDao authTokenDao = new AuthTokenDao(conn);

			String userName = authTokenDao.query(request.getToken());
			if (userName == null) throw new NotAuthorizedException();

			PersonDao personDao = new PersonDao(conn);
			Person person = personDao.query(request.getPersonID());

			if (person.getDescendant().equals(userName)) {
				res = new PersonResult(
						person.getDescendant(),
						person.getPersonID(),
						person.getFirstName(),
						person.getLastName(),
						person.getGender(),
						person.getFather(),
						person.getMother(),
						person.getSpouse(),
						null
				);

				commit = true;
			} else {
				res = new PersonResult(
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						"You are not authorized to view this content."
				);
			}
		} catch (Exception e) {
			res = new PersonResult(
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					e.getMessage()
			);
		} finally {
			db.closeConnection(commit);
		}

		return res;
	}
}
