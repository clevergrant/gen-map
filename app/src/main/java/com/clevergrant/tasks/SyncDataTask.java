package com.clevergrant.tasks;

import android.os.AsyncTask;

import com.clevergrant.client.HttpClient;
import com.clevergrant.model.Model;
import com.clevergrant.request.EventsRequest;
import com.clevergrant.request.PersonsRequest;
import com.clevergrant.result.EventsResult;
import com.clevergrant.result.PersonsResult;

public class SyncDataTask extends AsyncTask<Void, Void, SyncDataTask.SyncResult> {

	public interface Context {
		void dataFetched(SyncResult result);
	}

	private Context context;

	public SyncDataTask(Context c) {
		context = c;
	}

	@Override
	protected SyncResult doInBackground(Void... voids) {
		HttpClient httpClient = new HttpClient();

		String tokenString = Model.getToken();

		EventsRequest eRequest = new EventsRequest(tokenString);
		PersonsRequest pRequest = new PersonsRequest(tokenString);

		String domain = String.format("http://%s:%s", Model.getHostIp(), Model.getHostPort());

		return new SyncResult(
				httpClient.getPersons(domain, pRequest),
				httpClient.getEvents(domain, eRequest),
				null
		);
	}

	@Override
	protected void onPostExecute(SyncResult result) {
		context.dataFetched(result);
	}

	public class SyncResult {
		private PersonsResult personsResult;
		private EventsResult eventsResult;
		private String message = "";

		SyncResult(PersonsResult personsResult, EventsResult eventsResult, String message) {

			if (personsResult == null || eventsResult == null) {
				System.out.println("You done messed up, bud");
				return;
			}

			if (message != null && !message.isEmpty()) {
				this.message = message;
			} else {

				String pMessage = personsResult.getMessage();
				String eMessage = eventsResult.getMessage();

				if (pMessage != null && !pMessage.isEmpty()) {
					this.message = pMessage;
					if (eMessage != null && !eMessage.isEmpty()) this.message += '\n' + eMessage;
					return;
				}

				this.personsResult = personsResult;
				this.eventsResult = eventsResult;
			}
		}

		public PersonsResult getPersonsResult() {
			return personsResult;
		}

		public EventsResult getEventsResult() {
			return eventsResult;
		}

		public String getMessage() {
			return message;
		}
	}
}
