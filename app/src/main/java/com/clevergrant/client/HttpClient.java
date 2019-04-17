package com.clevergrant.client;

import com.clevergrant.model.Model;
import com.clevergrant.request.EventsRequest;
import com.clevergrant.request.LoginRequest;
import com.clevergrant.request.PersonsRequest;
import com.clevergrant.request.RegisterRequest;
import com.clevergrant.result.EventsResult;
import com.clevergrant.result.LoginResult;
import com.clevergrant.result.PersonsResult;
import com.clevergrant.result.RegisterResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.*;

public class HttpClient {

	public RegisterResult postRegister(String domain, RegisterRequest request) {
		try {
			URL url = new URL(domain + "/user/register");
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("POST");

			// only if there's a body
			http.setDoOutput(true);

			http.addRequestProperty("Accept", "application/json");

			http.connect();

			Gson gson = new Gson();
			String reqData = gson.toJson(request);
			OutputStream reqBody = http.getOutputStream();
			writeString(reqData, reqBody);
			reqBody.close();

			if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream respBody = http.getInputStream();
				String respData = readString(respBody);
				return gson.fromJson(respData, RegisterResult.class);
			} else {
				System.out.println("ERROR: " + http.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Here's the error message, my dude");
			System.out.println(e.getMessage());
		}

		return null;
	}

	public LoginResult postLogin(String domain, LoginRequest request) {
		try {
			URL url = new URL(domain + "/user/login");
			HttpURLConnection http = (HttpURLConnection) url.openConnection();

			http.addRequestProperty("Authorization", Model.getToken());
			http.setRequestMethod("POST");

			// only if there's a body
			http.setDoOutput(true);

			http.addRequestProperty("Accept", "application/json");

			http.connect();

			Gson gson = new Gson();
			String reqData = gson.toJson(request);
			OutputStream reqBody = http.getOutputStream();
			writeString(reqData, reqBody);
			reqBody.close();

			if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream respBody = http.getInputStream();
				String respData = readString(respBody);
				return gson.fromJson(respData, LoginResult.class);
			} else {
				System.out.println("ERROR: " + http.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Here's the error message, my dude");
			System.out.println(e.getMessage());
		}

		return null;
	}

	public PersonsResult getPersons(String domain, PersonsRequest request) {

		try {
			URL url = new URL(domain + "/person");
			HttpURLConnection http = (HttpURLConnection) url.openConnection();

			http.addRequestProperty("Authorization", request.getToken());
			http.setRequestMethod("GET");
			http.connect();

			if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream respBody = http.getInputStream();
				String respData = readString(respBody);
				return new Gson().fromJson(respData, PersonsResult.class);
			} else {
				System.out.println("ERROR: " + http.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Here's the error message, my dude");
			System.out.println(e.getMessage());
		}
		return null;
	}

	public EventsResult getEvents(String domain, EventsRequest request) {
		try {
			URL url = new URL(domain + "/event");
			HttpURLConnection http = (HttpURLConnection) url.openConnection();

			http.addRequestProperty("Authorization", request.getToken());
			http.setRequestMethod("GET");
			http.connect();

			if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream respBody = http.getInputStream();
				String respData = readString(respBody);
				Gson gson = new Gson();
				return gson.fromJson(respData, EventsResult.class);
			} else {
				System.out.println("ERROR: " + http.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Here's the error message, my dude");
			System.out.println(e.getMessage());
		}
		return null;
	}

	// Util

	private static String readString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		InputStreamReader sr = new InputStreamReader(is);
		char[] buf = new char[1024];
		int len;
		while ((len = sr.read(buf)) > 0) {
			sb.append(buf, 0, len);
		}
		return sb.toString();
	}

	private static void writeString(String str, OutputStream os) throws IOException {
		OutputStreamWriter sw = new OutputStreamWriter(os);
		sw.write(str);
		sw.flush();
	}
}
