package com.clevergrant.service;

import java.sql.Connection;

import com.clevergrant.dao.Database;
import com.clevergrant.dao.EventDao;
import com.clevergrant.dao.PersonDao;
import com.clevergrant.dao.UserDao;
import com.clevergrant.data.FamilyGenerator;
import com.clevergrant.exceptions.DataAccessException;
import com.clevergrant.model.Person;
import com.clevergrant.model.User;
import com.clevergrant.request.FillRequest;
import com.clevergrant.result.FillResult;

public class FillService {
	/**
	 * Populates the server's database with generated com.clevergrant.data for the specified user name. If there is already com.clevergrant.data, it will be deleted.
	 * Method: POST
	 * AuthToken required: No
	 *
	 * @param request Object containing the userName to generate com.clevergrant.data for, and the number of generations to generate
	 * @return Object with a message reporting how many persons and events were added
	 * @throws DataAccessException Throws when database doesn't close properly
	 */
	public FillResult fill(FillRequest request) throws DataAccessException {
		Database db = new Database();

		FillResult res;
		boolean commit = false;

		try {
			Connection conn = db.openConnection();

			UserDao userDao = new UserDao(conn);
			User user = userDao.query(request.getUserName());

			PersonDao personDao = new PersonDao(conn);
			EventDao eventDao = new EventDao(conn);

			Person root = personDao.query(user.getPersonID());

			personDao.deleteByDescendant(request.getUserName());
			eventDao.deleteByDescendant(request.getUserName());

			FamilyGenerator generator = new FamilyGenerator();
			generator.generate(root, request.getGenerations());
			res = generator.commit(conn);

			commit = true;
		} catch (Exception e) {
			res = new FillResult(
					0,
					0,
					e.getMessage()
			);
		} finally {
			db.closeConnection(commit);
		}

		return res;
	}
}
