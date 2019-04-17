package com.clevergrant.tasks;

import android.os.AsyncTask;

import com.clevergrant.client.HttpClient;
import com.clevergrant.model.Model;
import com.clevergrant.model.User;
import com.clevergrant.request.RegisterRequest;
import com.clevergrant.result.RegisterResult;

public class RegisterTask extends AsyncTask<Void, Void, RegisterResult> {

	public interface Context {
		void registerComplete(RegisterResult result);
	}

	private Context context;

	public RegisterTask(Context c) {
		context = c;
	}

	@Override
	protected RegisterResult doInBackground(Void... voids) {
		User user = Model.getUser();
		HttpClient httpClient = new HttpClient();

		RegisterRequest request = new RegisterRequest(user.getUserName(), user.getPassword(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getGender());

		String domain = String.format("http://%s:%s", Model.getHostIp(), Model.getHostPort());

		return httpClient.postRegister(domain, request);
	}

	@Override
	protected void onPostExecute(RegisterResult result) {
		context.registerComplete(result);
	}
}
