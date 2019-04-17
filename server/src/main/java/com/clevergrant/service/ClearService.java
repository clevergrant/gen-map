package com.clevergrant.service;

import java.sql.Connection;

import com.clevergrant.dao.AuthTokenDao;
import com.clevergrant.dao.Database;
import com.clevergrant.dao.EventDao;
import com.clevergrant.dao.PersonDao;
import com.clevergrant.dao.UserDao;
import com.clevergrant.exceptions.DataAccessException;
import com.clevergrant.result.ClearResult;

public class ClearService {

	/**
	 * Deletes all com.clevergrant.data from the database, including user accounts, auth tokens, and generated person and event com.clevergrant.data
	 * Method: POST
	 * AuthToken required: No
	 *
	 * @return Result containing confirmation of successful operation
	 * @throws DataAccessException Throws when database doesn't close properly
	 */
	public ClearResult clear() throws DataAccessException {

		Database db = new Database();

		ClearResult res;
		boolean commit = false;

		try {

			Connection conn = db.openConnection();

			AuthTokenDao atDao = new AuthTokenDao(conn);
			atDao.clear();

			EventDao eDao = new EventDao(conn);
			eDao.clear();

			PersonDao pDao = new PersonDao(conn);
			pDao.clear();

			UserDao uDao = new UserDao(conn);
			uDao.clear();

			res = new ClearResult(null);

			commit = true;
		} catch (Exception e) {
			res = new ClearResult(e.getMessage());
		} finally {
			db.closeConnection(commit);
		}

		return res;
	}
}
