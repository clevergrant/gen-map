package com.clevergrant.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import com.clevergrant.result.ClearResult;
import com.clevergrant.service.ClearService;
import sun.net.www.protocol.http.HttpURLConnection;

public class ClearHandler implements HttpHandler {
	public ClearHandler() {
		super();
	}

	public void handle(HttpExchange exchange) throws IOException {

		boolean success = false;

		try {
			if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
				ClearService service = new ClearService();

				try {
					ClearResult res = service.clear();

					Gson gson = new Gson();
					String jsonResponse = gson.toJson(res);

					exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

					OutputStream respBody = exchange.getResponseBody();
					writeString(jsonResponse, respBody);
					respBody.close();

					success = true;
				} catch (Exception e) {
					success = false;
				}
			}

			if (!success) {
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
				exchange.getResponseBody().close();
			}
		} catch (Exception e) {
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
