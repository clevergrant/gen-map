package com.clevergrant.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.HashMap;

public final class Model {
	private static final Model instance = new Model();

	private String hostIp;
	private String hostPort;

	private AuthToken token = new AuthToken(null, null);

	private User user = new User(null, null, null, null, null, null, null);

	private Person person = new Person(null, null, null, null, null, null, null, null);

	private HashMap<String, Person> persons = new HashMap<>();
	private HashMap<String, Event> events = new HashMap<>();
	private HashMap<String, MaterialColors> eventColors = new HashMap<>();
	private HashMap<MaterialColors, Boolean> usedColors = new UsedColors();

	public PreferencesStore store = new PreferencesStore();
	public ColorResource colorResource = new ColorResource();
	public MapResource mapResource = new MapResource();

	public void addEventColor(String type) {
		if (eventColors.get(type) == null)
			for (HashMap.Entry<MaterialColors, Boolean> entry : usedColors.entrySet())
				if (!entry.getValue()) {
					eventColors.put(type, entry.getKey());
					usedColors.put(entry.getKey(), true);
					return;
				}
	}

	public float getEventColor(String type) {
		return colorResource.getColorFloat(eventColors.get(type));
	}

	public static void clear() {
		setAuthToken(new AuthToken(null, null));
		setUser(new User(null, null, null, null, null, null, null));
		setPerson(new Person(null, null, null, null, null, null, null, null));
	}

	public static void clearData() {
		instance.persons = new HashMap<>();
		instance.events = new HashMap<>();
		instance.eventColors = new HashMap<>();
		instance.usedColors = new UsedColors();
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

	public class PreferencesStore {

		SharedPreferences.Editor editor;
		SharedPreferences pref;

		PreferencesStore() {
		}

		// Switches

		public void setLifeStoryLine(Context context, boolean set) {
			editor = getPreferences(context).edit();
			editor.putBoolean("LifeStoryLine", set).apply();
		}

		public boolean getLifeStoryLine(Context context) {
			pref = getPreferences(context);
			return pref.getBoolean("LifeStoryLine", true);
		}

		public void setFamilyTreeLine(Context context, boolean set) {
			editor = getPreferences(context).edit();
			editor.putBoolean("FamilyTreeLine", set).apply();
		}

		public boolean getFamilyTreeLine(Context context) {
			pref = getPreferences(context);
			return pref.getBoolean("FamilyTreeLine", true);
		}

		public void setSpouseLine(Context context, boolean set) {
			editor = getPreferences(context).edit();
			editor.putBoolean("SpouseLine", set).apply();
		}

		public boolean getSpouseLine(Context context) {
			pref = getPreferences(context);
			return pref.getBoolean("SpouseLine", true);
		}

		// Colors

		public void setLifeStoryColor(Context context, int set) {
			editor = getPreferences(context).edit();
			editor.putInt("LifeStoryColor", set).apply();
		}

		public int getLifeStoryColor(Context context) {
			pref = getPreferences(context);
			return pref.getInt("LifeStoryColor", 0xFF40C4FF);
		}

		public void setFamilyTreeColor(Context context, int set) {
			editor = getPreferences(context).edit();
			editor.putInt("FamilyTreeColor", set).apply();
		}

		public int getFamilyTreeColor(Context context) {
			pref = getPreferences(context);
			return pref.getInt("FamilyTreeColor", 0xFFB2FF59);
		}

		public void setSpouseColor(Context context, int set) {
			editor = getPreferences(context).edit();
			editor.putInt("SpouseColor", set).apply();
		}

		public int getSpouseColor(Context context) {
			pref = getPreferences(context);
			return pref.getInt("SpouseColor", 0xFFB388FF);
		}

		// Spinner Positions

		public void setLifeStoryColorPosition(Context context, int position) {
			editor = getPreferences(context).edit();
			editor.putInt("lifeStoryColorPosition", position).apply();
		}

		public int getLifeStoryColorPosition(Context context) {
			pref = getPreferences(context);
			return pref.getInt("lifeStoryColorPosition", 0);
		}

		public void setFamilyTreeColorPosition(Context context, int position) {
			editor = getPreferences(context).edit();
			editor.putInt("familyTreeColorPosition", position).apply();
		}

		public int getFamilyTreeColorPosition(Context context) {
			pref = getPreferences(context);
			return pref.getInt("familyTreeColorPosition", 1);
		}

		public void setSpouseColorPosition(Context context, int position) {
			editor = getPreferences(context).edit();
			editor.putInt("spouseColorPosition", position).apply();
		}

		public int getSpouseColorPosition(Context context) {
			pref = getPreferences(context);
			return pref.getInt("spouseColorPosition", 8);
		}

		// Map Types

		public void setMapType(Context context, int type) {
			editor = getPreferences(context).edit();
			editor.putInt("mapType", type).apply();
		}

		public int getMapType(Context context) {
			pref = getPreferences(context);
			return pref.getInt("mapType", 1);
		}

		public void setMapTypePosition(Context context, int position) {
			editor = getPreferences(context).edit();
			editor.putInt("mapTypePosition", position).apply();
		}

		public int getMapTypePosition(Context context) {
			pref = getPreferences(context);
			return pref.getInt("mapTypePosition", 0);
		}
	}

	public class ColorResource {

		public ArrayList<String> markerSpinnerColors = new ArrayList<>();
		public HashMap<String, Float> markerSpinnerValues = new HashMap<>();
		public HashMap<String, Integer> lineColors = new HashMap<>();

		public float getColorFloat(MaterialColors index) {
			switch (index) {
				case GREEN:
					return GREEN;
				case RED:
					return RED;
				case AZURE:
					return AZURE;
				case CYAN:
					return CYAN;
				case MAGENTA:
					return MAGENTA;
				case ORANGE:
					return ORANGE;
				case ROSE:
					return ROSE;
				case VIOLET:
					return VIOLET;
				case YELLOW:
					return YELLOW;
				default:
					return BLUE;
			}
		}

		final float BLUE = BitmapDescriptorFactory.HUE_BLUE;
		final float GREEN = BitmapDescriptorFactory.HUE_GREEN;
		final float RED = BitmapDescriptorFactory.HUE_RED;
		final float AZURE = BitmapDescriptorFactory.HUE_AZURE;
		final float CYAN = BitmapDescriptorFactory.HUE_CYAN;
		final float MAGENTA = BitmapDescriptorFactory.HUE_MAGENTA;
		final float ORANGE = BitmapDescriptorFactory.HUE_ORANGE;
		final float ROSE = BitmapDescriptorFactory.HUE_ROSE;
		final float VIOLET = BitmapDescriptorFactory.HUE_VIOLET;
		final float YELLOW = BitmapDescriptorFactory.HUE_YELLOW;

		private ColorResource() {
			markerSpinnerColors.add("Blue");
			markerSpinnerColors.add("Green");
			markerSpinnerColors.add("Red");
			markerSpinnerColors.add("Azure");
			markerSpinnerColors.add("Cyan");
			markerSpinnerColors.add("Magenta");
			markerSpinnerColors.add("Orange");
			markerSpinnerColors.add("Rose");
			markerSpinnerColors.add("Violet");
			markerSpinnerColors.add("Yellow");

			markerSpinnerValues.put("Blue", this.BLUE);
			markerSpinnerValues.put("Green", this.GREEN);
			markerSpinnerValues.put("Red", this.RED);
			markerSpinnerValues.put("Azure", this.AZURE);
			markerSpinnerValues.put("Cyan", this.CYAN);
			markerSpinnerValues.put("Magenta", this.MAGENTA);
			markerSpinnerValues.put("Orange", this.ORANGE);
			markerSpinnerValues.put("Rose", this.ROSE);
			markerSpinnerValues.put("Violet", this.VIOLET);
			markerSpinnerValues.put("Yellow", this.YELLOW);

			lineColors.put("Blue", 0xFF40C4FF);
			lineColors.put("Green", 0xFFB2FF59);
			lineColors.put("Red", 0xFFFF5252);
			lineColors.put("Azure", 0xFF448AFF);
			lineColors.put("Cyan", 0xFF18FFFF);
			lineColors.put("Magenta", 0xFFFF4081);
			lineColors.put("Orange", 0xFFFFAB40);
			lineColors.put("Rose", 0xFFFF80AB);
			lineColors.put("Violet", 0xFFB388FF);
			lineColors.put("Yellow", 0xFFFFFF00);
		}
	}

	public class MapResource {
		public ArrayList<String> types = new ArrayList<>();

		MapResource() {
			types.add("Normal");
			types.add("Satellite");
			types.add("Terrain");
			types.add("Hybrid");
		}
	}

	private SharedPreferences getPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public enum MaterialColors {
		BLUE,
		GREEN,
		RED,
		AZURE,
		CYAN,
		MAGENTA,
		ORANGE,
		ROSE,
		VIOLET,
		YELLOW,
		EMPTY
	}

	public static class ResultCodes {
		public static final int OK = 0;
		public static final int SYNCHRONIZE = 1;
		public static final int LOGOUT = 2;
	}

	private static class UsedColors extends HashMap<MaterialColors, Boolean> {
		UsedColors() {
			for (MaterialColors color : MaterialColors.values()) this.put(color, false);
		}
	}
}
