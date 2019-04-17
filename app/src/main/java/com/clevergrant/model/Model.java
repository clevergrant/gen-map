package com.clevergrant.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

public final class Model {
	private static final Model instance = new Model();

	private String hostIp;
	private String hostPort;

	private AuthToken token = new AuthToken(null,null);

	private User user = new User(null, null, null, null, null, null, null);

	private Person person = new Person(null,null,null,null,null,null,null,null);

	private HashMap<String, Person> persons = new HashMap<>();
	private HashMap<String, Event> events = new HashMap<>();

	private Model() {
	}

	public static void clear() {
		setHostIp("");
		setHostPort("");
		setAuthToken(new AuthToken(null, null));
		setUser(new User(null, null, null, null, null, null, null));
		setPerson(new Person(null,null,null,null,null,null,null,null));
	}

	public static Model getInstance() {
		return instance;
	}

	public static String getHostIp() {
		return (instance.hostIp == null ? "" : instance.hostIp);
	}

	public static void setHostIp(String hostIp) {
		instance.hostIp = hostIp;
	}

	public static String getHostPort() {
		return (instance.hostPort == null ? "" : instance.hostPort);
	}

	public static void setHostPort(String hostPort) {
		instance.hostPort = hostPort;
	}

	public static String getToken() {
		return (instance.token.getToken() == null ? "" : instance.token.getToken());
	}

	public static void setToken(String username, String token) {
		instance.token = new AuthToken(username, token);
	}

//	public static AuthToken getAuthToken() {
//		return instance.token;
//	}

	private static void setAuthToken(AuthToken token) {
		instance.token = token;
	}

	public static User getUser() {
		return instance.user;
	}

	public static void setUser(User user) {
		instance.user = user;
	}

	public String getUserName() {
		return (user.getUserName() == null ? "" : user.getUserName());
	}

	public void setUserName(String userName) {
		user.setUserName(userName);
	}

	public String getPassword() {
		return (user.getPassword() == null ? "" : user.getPassword());
	}

	public void setPassword(String password) {
		user.setPassword(password);
	}

	public String getFirstName() {
		return (instance.user.getFirstName() == null ? "" : instance.user.getFirstName());
	}

	public void setFirstName(String firstName) {
		user.setFirstName(firstName);
	}

	public String getLastName() {
		return (instance.user.getLastName() == null ? "" : instance.user.getLastName());
	}

	public void setLastName(String lastName) {
		user.setLastName(lastName);
	}

	public String getEmail() {
		return (user.getEmail() == null ? "" : user.getEmail());
	}

	public void setEmail(String email) {
		user.setEmail(email);
	}

	public String getGender() {
		return (user.getGender() == null ? "" : user.getGender());
	}

	public void setGender(String gender) {
		user.setGender(gender);
	}

	public String getPersonID() {
		return (user.getPersonID() == null ? "" : user.getPersonID());
	}

	public void setPersonID(String personID) {
		user.setPersonID(personID);
	}

	public static Person getPerson() {
		return instance.person;
	}

	public static void setPerson(Person person) {
		instance.person = person;
	}

	public HashMap<String, Person> getPersons() {
		return persons;
	}

	public void addPerson(Person p) {
		this.persons.put(p.getPersonID(), new Person(p));
	}

	public HashMap<String, Event> getEvents() {
		return events;
	}

	public Event getEvent(String id) {
		return events.get(id);
	}

	public void addEvent(Event e) {
		this.events.put(e.getEventID(), e);
	}

	public void fromSharedPref(Context context) {
		hostIp = getPreferences(context).getString("ServerHost", "");
		hostPort = getPreferences(context).getString("ServerPort", "");
		setUserName(getPreferences(context).getString("Username", ""));
	}

	private SharedPreferences getPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

}
