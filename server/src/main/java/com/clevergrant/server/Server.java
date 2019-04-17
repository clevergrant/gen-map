package com.clevergrant.server;

import java.io.*;
import java.net.*;

import com.sun.net.httpserver.*;

import com.clevergrant.dao.Database;
import com.clevergrant.handler.*;

public class Server {

	private static final int MAX_WAITING_CONNECTIONS = 12;

	/**
	 * Initializes the HTTP server
	 *
	 * @param portNumber The port you want the server to run on
	 */
	private void run(String portNumber) {

		System.out.println("Initializing Database");

		Database db = new Database();

		try {
			db.createTables();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Initializing HTTP Server");

		HttpServer server;
		try {
			server = HttpServer.create(new InetSocketAddress(Integer.parseInt(portNumber)), MAX_WAITING_CONNECTIONS);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		server.setExecutor(null);

		System.out.println("Creating contexts");

		server.createContext("/", new FileHandler());
		server.createContext("/clear", new ClearHandler());
		server.createContext("/event", new EventHandler());
		server.createContext("/fill", new FillHandler());
		server.createContext("/load", new LoadHandler());
		server.createContext("/user/login", new LoginHandler());
		server.createContext("/person", new PersonHandler());
		server.createContext("/user/register", new RegisterHandler());

		System.out.println("Starting server on port: " + String.valueOf(portNumber));

		server.start();

		System.out.println("Server started");
	}

	public static void main(String[] args) {
		String port = args[0];
		new Server().run(port);
	}
}
