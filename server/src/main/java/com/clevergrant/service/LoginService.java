package com.clevergrant.service;

import java.sql.Connection;

import com.clevergrant.dao.AuthTokenDao;
import com.clevergrant.dao.Database;
import com.clevergrant.dao.UserDao;
import com.clevergrant.exceptions.DataAccessException;
import com.clevergrant.model.AuthToken;
import com.clevergrant.model.User;
import com.clevergrant.request.LoginRequest;
import com.clevergrant.result.LoginResult;

public class LoginService {
	/**
	 * Logs in the user and returns an auth token
	 * Method: POST
	 * AuthToken required: No
	 *
	 * @param request Object containing user credentials
	 * @return Object containing the generated AuthToken, and the user's userName and personID
	 * @throws DataAccessException Throws when database doesn't close properly
	 */
	public LoginResult login(LoginRequest request) throws DataAccessException {
		Database db = new Database();

		Connection conn = db.openConnection();

		LoginResult res;
		boolean commit = false;

		try {

			UserDao userDao = new UserDao(conn);

			User user = userDao.query(request.getUserName(), request.getPassword());

			if (user == null) throw new DataAccessException("User does not exist.");
			else {

				AuthToken token = new AuthToken(user.getUserName());
				AuthTokenDao authTokenDao = new AuthTokenDao(conn);

				try {
					authTokenDao.create(token);

					res = new LoginResult(
							token.getToken(),
							user.getUserName(),
							user.getPersonID(),
							null
					);

					commit = true;
				} catch (Exception e) {
					res = new LoginResult(
							null,
							null,
							null,
							e.getMessage()
					);
				}
			}
		} catch (Exception e) {
			res = new LoginResult(
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
