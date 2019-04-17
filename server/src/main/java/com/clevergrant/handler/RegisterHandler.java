package com.clevergrant.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import com.google.gson.*;

import com.clevergrant.request.RegisterRequest;
import com.clevergrant.result.RegisterResult;
import com.clevergrant.service.RegisterService;
import sun.net.www.protocol.http.HttpURLConnection;

public class RegisterHandler implements HttpHandler {
	public RegisterHandler() {
		super();
	}

	public void handle(HttpExchange exchange) throws IOException {
		boolean success = false;

		try {
			if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
				try {
					Gson gson = new Gson();
					RegisterResult res = new RegisterService().register(
							gson.fromJson(
									readString(exchange.getRequestBody()),
									RegisterRequest.class
							)
					);
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

	private String readString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStreamReader sr = new InputStreamReader(is);
		char[] buf = new char[1024];
		int len;
		while ((len = sr.read(buf)) > 0) {
			sb.append(buf, 0, len);
		}
		return sb.toString();
	}

	private void writeString(String str, OutputStream os) throws IOException {
		OutputStreamWriter sw = new OutputStreamWriter(os);
		sw.write(str);
		sw.flush();
	}
}
