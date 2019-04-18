package com.clevergrant.client;

import com.clevergrant.model.AuthToken;
import com.clevergrant.model.Model;
import com.clevergrant.model.User;
import com.clevergrant.request.EventsRequest;
import com.clevergrant.request.LoginRequest;
import com.clevergrant.request.PersonsRequest;
import com.clevergrant.request.RegisterRequest;
import com.clevergrant.result.ClearResult;
import com.clevergrant.result.EventsResult;
import com.clevergrant.result.LoginResult;
import com.clevergrant.result.PersonsResult;
import com.clevergrant.result.RegisterResult;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class HttpClientTest {

	private HttpClient client;
	private String domain;

	private User user;
	private User emptyUser;

	private RegisterRequest registerRequest;
	private RegisterRequest badRegisterRequest;

	private LoginRequest loginRequest;
	private LoginRequest badLoginRequestUser;
	private LoginRequest badLoginRequestPass;

	private PersonsRequest personsRequest;
	private EventsRequest eventsRequest;
	private AuthToken token;

	@Before
	public void setUp() {
		domain = String.format("http://%s:%s", Model.getHostIp(), Model.getHostPort());
		client = new HttpClient();
		user = new User("username", "password", "em@i.l", "name", "last", "m", "id");
		emptyUser = new User(null, null, null, null, null, null, null);
		badRegisterRequest = new RegisterRequest(emptyUser.getUserName(), emptyUser.getPassword(), emptyUser.getEmail(), emptyUser.getFirstName(), emptyUser.getLastName(), emptyUser.getGender());
		badLoginRequestUser = new LoginRequest(emptyUser.getUserName(), "wrongpassword");
		badLoginRequestUser = new LoginRequest(user.getUserName(), "wrongpassword");

		registerRequest = new RegisterRequest(
				user.getUserName(),
				user.getPassword(),
				user.getEmail(),
				user.getFirstName(),
				user.getLastName(),
				user.getGender()
		);

		loginRequest = new LoginRequest(
				user.getUserName(),
				user.getPassword()
		);

		badLoginRequestUser = new LoginRequest(
				emptyUser.getUserName(),
				"wrongpassword"
		);

		badLoginRequestPass = new LoginRequest(
				user.getUserName(),
				"wrongpassword"
		);


	}

	@After
	public void tearDown() {
		domain = null;
		client = null;
		user = null;
		emptyUser = null;

		registerRequest = null;
		badRegisterRequest = null;

		loginRequest = null;
		badLoginRequestUser = null;
		badLoginRequestPass = null;
	}

	@Test
	public void postRegister() {
		if (clearServer()) {
			RegisterResult result = client.postRegister(domain, registerRequest);
			token = new AuthToken(result.getUserName(), result.getToken());
			Assert.assertNull(result.getMessage());
			Assert.assertNotNull(result);
		} else Assert.fail();
	}

	@Test
	public void postRegisterFail() {
		Assert.assertNotNull(badRegisterRequest);
		if (clearServer()) {
			RegisterResult result = client.postRegister(domain, badRegisterRequest);
			token = new AuthToken(result.getUserName(), result.getToken());
			Assert.assertNotNull(result.getMessage());
			Assert.assertNull(result.getPersonID());
			Assert.assertNull(result.getToken());
			Assert.assertNull(result.getUserName());
		} else Assert.fail();
	}

	@Test
	public void postLogin() {
		Assert.assertNotNull(loginRequest);

		if (clearServer()) {
			LoginResult result = client.postLogin(domain, loginRequest);
			Assert.assertEquals("User does not exist.", result.getMessage());
			Assert.assertNotNull(result);
		} else Assert.fail();

		this.postRegister();
		LoginResult result = client.postLogin(domain, loginRequest);
		Assert.assertNull(result.getMessage());
		Assert.assertNotNull(result);
	}

	@Test
	public void postLoginFail() {
		Assert.assertNotNull(badLoginRequestPass);
		Assert.assertNotNull(badLoginRequestUser);

		if (clearServer()) {
			LoginResult result = client.postLogin(domain, badLoginRequestUser);
			Assert.assertNotNull(result.getMessage());
			Assert.assertNull(result.getPersonID());
			Assert.assertNull(result.getToken());
			Assert.assertNull(result.getUserName());
		} else Assert.fail();

		this.postRegister();
		LoginResult result = client.postLogin(domain, badLoginRequestUser);
		Assert.assertEquals("User does not exist.", result.getMessage());
		Assert.assertNotNull(result);

		result = client.postLogin(domain, badLoginRequestPass);
		Assert.assertEquals("User does not exist.", result.getMessage());
		Assert.assertNotNull(result);
	}

	@Test
	public void getPersons() {
		if (clearServer()) {
			this.postRegister();
			personsRequest = new PersonsRequest(token.getToken());
			PersonsResult result = client.getPersons(domain, personsRequest);
			Assert.assertNotNull(result);
			Assert.assertNull(result.getMessage());
			Assert.assertNotNull(result.getData());
			Assert.assertEquals(31, result.getData().length);
		} else Assert.fail();
	}

	@Test
	public void getPersonsFail() {
		clearServer();
		try {
			personsRequest = new PersonsRequest(token.getToken());
			PersonsResult result = client.getPersons(domain, personsRequest);
			Assert.assertNull(result);
		} catch (Exception ignored) {
		}
	}

	@Test
	public void getEvents() {
		if (clearServer()) {
			this.postRegister();
			eventsRequest = new EventsRequest(token.getToken());
			EventsResult result = client.getEvents(domain, eventsRequest);
			Assert.assertNull(result.getMessage());
			Assert.assertNotNull(result.getData());
			Assert.assertEquals(91, result.getData().length);
		} else Assert.fail();
	}

	@Test
	public void getEventsFail() {
		clearServer();
		try {
			eventsRequest = new EventsRequest(token.getToken());
			EventsResult result = client.getEvents(domain, eventsRequest);
			Assert.assertNull(result);
		} catch (Exception ignored) {
		}
	}

	private boolean clearServer() {
		try {
			URL url = new URL(domain + "/clear");
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("POST");
			http.addRequestProperty("Accept", "application/json");
			http.connect();

			if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream respBody = http.getInputStream();
				String respData = HttpClient.readString(respBody);
				Gson gson = new Gson();
				return Objects.equals(gson.fromJson(respData, ClearResult.class).getMessage(), "ClearService succeeded.");
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
}