package com.clevergrant.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;

import sun.net.www.protocol.http.HttpURLConnection;

public class FileHandler implements HttpHandler {
	public FileHandler() {
		super();
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			if (exchange.getRequestMethod().toUpperCase().equals("GET")) {

				URI uri = exchange.getRequestURI();

				String root = "C:/repos/cs240/FamMap/server/src/main/www";
				String path = uri.getPath();

				if (path.equals("/"))
					path = "/index.html";

				File file = new File(root + path).getCanonicalFile();

				if (!file.isFile()) {
					path = "/HTML/404.html";
					file = new File(root + path).getCanonicalFile();
				}

				// Object exists and is a file: accept with response code 200.
				String mime = Files.probeContentType(file.toPath());

				Headers h = exchange.getResponseHeaders();
				h.set("Content-Type", mime);
				exchange.sendResponseHeaders(200, 0);

				OutputStream os = exchange.getResponseBody();
				FileInputStream fs = new FileInputStream(file);
				final byte[] buffer = new byte[0x10000];

				int count;
				while ((count = fs.read(buffer)) >= 0)
					os.write(buffer, 0, count);

				fs.close();
				os.close();
			}
		} catch (IOException e) {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
			exchange.getResponseBody().close();

			e.printStackTrace();
		}
	}
}
