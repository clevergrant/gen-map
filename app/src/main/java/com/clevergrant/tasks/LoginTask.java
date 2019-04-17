package com.clevergrant.tasks;

import android.os.AsyncTask;

import com.clevergrant.client.HttpClient;
import com.clevergrant.model.Model;
import com.clevergrant.model.User;
import com.clevergrant.request.LoginRequest;
import com.clevergrant.result.LoginResult;

public class LoginTask extends AsyncTask<Void, Void, LoginResult> {

	public interface Context {
		void loginComplete(LoginResult result);
	}

	private Context context;

	public LoginTask(Context c) {
		context = c;
	}

	@Override
	protected LoginResult doInBackground(Void... voids) {
		User user = Model.getUser();
		HttpClient httpClient = new HttpClient();

		LoginRequest request = new LoginRequest(user.getUserName(), user.getPassword());

		String domain = String.format("http://%s:%s", Model.getHostIp(), Model.getHostPort());

		return httpClient.postLogin(domain, request);
	}

	@Override
	protected void onPostExecute(LoginResult result) {
		context.loginComplete(result);
	}
}
