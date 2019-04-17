package com.clevergrant.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import com.clevergrant.request.FillRequest;
import com.clevergrant.result.FillResult;
import com.clevergrant.service.FillService;
import sun.net.www.protocol.http.HttpURLConnection;

public class FillHandler implements HttpHandler {
	public FillHandler() {
		super();
	}

	public void handle(HttpExchange exchange) throws IOException {
		boolean success = false;

		try {
			if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
				try {
					String[] pathItems = exchange.getRequestURI().toString().split("/");

					String userName;
					int generations;

					try {
						userName = pathItems[2];
					} catch (ArrayIndexOutOfBoundsException e) {
						throw new Exception("Invalid Request");
					}

					try {
						generations = Integer.parseInt(pathItems[3]);
					} catch (ArrayIndexOutOfBoundsException e) {
						generations = 4;
					}

					FillResult res = new FillService().fill(
							new FillRequest(
									userName,
									generations
							)
					);

					String jsonResponse = new Gson().toJson(res);

					exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

					OutputStream respBody = exchange.getResponseBody();
					writeString(jsonResponse, respBody);
					respBody.close();

					success = true;
				} catch (Exception e) {
					success = false;
					e.printStackTrace();
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
