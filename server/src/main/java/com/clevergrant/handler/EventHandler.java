package com.clevergrant.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import com.clevergrant.request.EventRequest;
import com.clevergrant.request.EventsRequest;
import com.clevergrant.result.EventResult;
import com.clevergrant.result.EventsResult;
import com.clevergrant.service.EventService;
import com.clevergrant.service.EventsService;
import sun.net.www.protocol.http.HttpURLConnection;

public class EventHandler implements HttpHandler {
	public EventHandler() {
		super();
	}

	public void handle(HttpExchange exchange) throws IOException {
		boolean success = false;

		try {
			if (exchange.getRequestMethod().toUpperCase().equals("GET")) {

				Headers reqHeaders = exchange.getRequestHeaders();

				if (reqHeaders.containsKey("Authorization")) {

					String authToken = reqHeaders.getFirst("Authorization");

					String uri = exchange.getRequestURI().toString();

					try {
						String id = uri.split("/")[2];

						EventService service = new EventService();

						EventRequest req = new EventRequest(id, authToken);

						try {
							EventResult res = service.get(req);

							Gson gson = new Gson();
							String jsonResponse = gson.toJson(res);

							exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

							OutputStream respBody = exchange.getResponseBody();
							writeString(jsonResponse, respBody);
							respBody.close();

							success = true;
						} catch (Exception e) {
							success = false;
							e.printStackTrace();
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						// This means that it's looking for all the event objects

						EventsService service = new EventsService();

						EventsRequest req = new EventsRequest(authToken);

						try {

							EventsResult res = service.getAll(req);

							Gson gson = new Gson();
							String jsonResponse = gson.toJson(res);

							exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

							OutputStream respBody = exchange.getResponseBody();
							writeString(jsonResponse, respBody);
							respBody.close();

							success = true;
						} catch (Exception _e) {
							success = false;
							_e.printStackTrace();
						}
					}
				}
			}

			if (!success) {
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
				exchange.getResponseBody().close();
			}
		} catch (IOException e) {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
			exchange.getResponseBody().close();

			e.printStackTrace();
		}
	}

	private void writeString(String str, OutputStream os) throws IOException {
		OutputStreamWriter sw = new OutputStreamWriter(os);
		sw.write(str);
		sw.flush();
	}
}
