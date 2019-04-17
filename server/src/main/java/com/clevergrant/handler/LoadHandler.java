package com.clevergrant.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import com.clevergrant.request.LoadRequest;
import com.clevergrant.result.LoadResult;
import com.clevergrant.service.LoadService;
import sun.net.www.protocol.http.HttpURLConnection;

public class LoadHandler implements HttpHandler {
	public LoadHandler() {
		super();
	}

	public void handle(HttpExchange exchange) throws IOException {
		boolean success = false;

		try {
			if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
				try {
					String reqData = readString(exchange.getRequestBody());

					Gson gson = new Gson();
					LoadResult res = new LoadService().load(
							gson.fromJson(
									reqData,
									LoadRequest.class
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
