package com.clevergrant.service;

import java.sql.Connection;
import java.util.UUID;

import com.clevergrant.dao.AuthTokenDao;
import com.clevergrant.dao.Database;
import com.clevergrant.dao.PersonDao;
import com.clevergrant.dao.UserDao;
import com.clevergrant.exceptions.DataAccessException;
import com.clevergrant.model.AuthToken;
import com.clevergrant.model.Person;
import com.clevergrant.model.User;
import com.clevergrant.request.FillRequest;
import com.clevergrant.request.RegisterRequest;
import com.clevergrant.result.RegisterResult;

public class RegisterService {
	/**
	 * Creates a new user account, generates 4 generations of ancestor com.clevergrant.data for the new user, logs the user in, and returns an auth token.
	 * Method: POST
	 * AuthToken required: No
	 *
	 * @param request Object containing all the com.clevergrant.data needed to register the user
	 * @return Resulting object containing the AuthToken string, the userName, and the new personID
	 * @throws DataAccessException Throws when database doesn't close properly
	 */
	public RegisterResult register(RegisterRequest request) throws DataAccessException {
		Database db = new Database();

		User user = new User(
				request.getUserName(),
				request.getPassword(),
				request.getEmail(),
				request.getFirstName(),
				request.getLastName(),
				request.getGender(),
				UUID.randomUUID().toString()
		);

		AuthToken token = new AuthToken(user.getUserName());

		boolean commit = false;

		RegisterResult res;

		try {

			Connection conn = db.openConnection();

			UserDao userDao = new UserDao(conn);
			userDao.create(user);

			AuthTokenDao authTokenDao = new AuthTokenDao(conn);
			authTokenDao.create(token);

			Person person = new Person(
					user.getPersonID(),
					user.getUserName(),
					user.getFirstName(),
					user.getLastName(),
					user.getGender(),
					null,
					null,
					null
			);

			PersonDao personDao = new PersonDao(conn);
			personDao.create(person);

			res = new RegisterResult(
					token.getToken(),
					user.getUserName(),
					user.getPersonID(),
					null
			);

			commit = true;
		} catch (Exception e) {
			res = new RegisterResult(
					null,
					null,
					null,
					e.getMessage()
			);
		} finally {
			db.closeConnection(commit);
		}

		FillService service = new FillService();
		service.fill(new FillRequest(user.getUserName()));

		return res;
	}
}
