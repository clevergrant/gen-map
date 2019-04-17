package com.clevergrant.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.google.gson.*;

import com.clevergrant.dao.EventDao;
import com.clevergrant.dao.PersonDao;
import com.clevergrant.model.Event;
import com.clevergrant.model.Person;
import com.clevergrant.result.FillResult;

public class FamilyGenerator {

	private String descendant;
	private MockData mock;
	private TreeMap<String, Person> family = new TreeMap<>();
	private ArrayList<Event> events = new ArrayList<>();

	public void generate(Person root, int generations) throws FileNotFoundException {
		this.mock = new MockData();
		this.descendant = root.getDescendant();

		family.put(root.getPersonID(), root);

		addParents(root, generations, 1992);
	}

	public FillResult commit(Connection conn) {
		FillResult res;

		try {

			int eventsAdded = new EventDao(conn).addAll(events.toArray(new Event[0]));
			int personsAdded = new PersonDao(conn).addAll(family.values().toArray(new Person[0]));

			res = new FillResult(
					personsAdded,
					eventsAdded,
					null
			);
		} catch (Exception e) {
			res = new FillResult(
					0,
					0,
					e.getMessage()
			);
		}

		return res;
	}

	private void addParents(Person child, int generations, int childBirthYear) {

		addBirth(child, childBirthYear);
		if (child.isMarried()) addDeath(child, childBirthYear);

		if (generations > 0) {

			String[] surNames = this.mock.surNames.data;
			String[] femNames = this.mock.femNames.data;
			String[] masNames = this.mock.masNames.data;

			String momID = UUID.randomUUID().toString();
			child.setMother(momID);

			String dadID = UUID.randomUUID().toString();
			child.setFather(dadID);

			String momFirstName = getRandName(femNames);
			String dadFirstName = getRandName(masNames);

			String lastName;
			if (child.getGender().equals("m") || !child.isMarried())
				lastName = child.getLastName();
			else lastName = getRandName(surNames);

			Person mom = new Person(
					momID,
					this.descendant,
					momFirstName,
					lastName,
					"f",
					null,
					null,
					dadID
			);

			Person dad = new Person(
					dadID,
					this.descendant,
					dadFirstName,
					lastName,
					"m",
					null,
					null,
					momID
			);

			int momBirthYear = childBirthYear - (22 + ThreadLocalRandom.current().nextInt(0, 7));
			int dadBirthYear = childBirthYear - (22 + ThreadLocalRandom.current().nextInt(0, 7));

			int newGen = generations - 1;
			addParents(mom, newGen, momBirthYear);
			addParents(dad, newGen, dadBirthYear);

			addMarriage(mom, dad, childBirthYear);

			family.put(momID, mom);
			family.put(dadID, dad);
		}
	}

	private void addDeath(Person person, int birthYear) {

		int deathYear = birthYear + (67 + ThreadLocalRandom.current().nextInt(0, 24));

//		if (deathYear <= Calendar.getInstance().get(Calendar.YEAR)) {
		Location[] locations = this.mock.locations.data;
		Location deathLoc = getRandLoc(locations);

		events.add(
				new Event(
						UUID.randomUUID().toString(),
						person.getDescendant(),
						person.getPersonID(),
						deathLoc.latitude,
						deathLoc.longitude,
						deathLoc.country,
						deathLoc.city,
						"death",
						deathYear
				)
		);
//		}
	}

	private void addMarriage(Person mom, Person dad, int childBirthYear) {
		int marriageYear = childBirthYear - (1 + ThreadLocalRandom.current().nextInt(0, 5));

		Location[] locations = this.mock.locations.data;
		Location marriageLoc = getRandLoc(locations);

		events.add(
				new Event(
						UUID.randomUUID().toString(),
						mom.getDescendant(),
						mom.getPersonID(),
						marriageLoc.latitude,
						marriageLoc.longitude,
						marriageLoc.country,
						marriageLoc.city,
						"marriage",
						marriageYear
				)
		);

		events.add(
				new Event(
						UUID.randomUUID().toString(),
						dad.getDescendant(),
						dad.getPersonID(),
						marriageLoc.latitude,
						marriageLoc.longitude,
						marriageLoc.country,
						marriageLoc.city,
						"marriage",
						marriageYear
				)
		);
	}

	private void addBirth(Person person, int birthYear) {

		Location[] locations = this.mock.locations.data;
		Location birthLoc = getRandLoc(locations);

		events.add(
				new Event(
						UUID.randomUUID().toString(),
						person.getDescendant(),
						person.getPersonID(),
						birthLoc.latitude,
						birthLoc.longitude,
						birthLoc.country,
						birthLoc.city,
						"birth",
						birthYear
				)
		);
	}

	private String getRandName(String[] names) {
		return names[ThreadLocalRandom.current().nextInt(0, names.length)];
	}

	private Location getRandLoc(Location[] locations) {
		return locations[ThreadLocalRandom.current().nextInt(0, locations.length)];
	}

	private class MockData {
		Locations locations;
		Names surNames;
		Names femNames;
		Names masNames;

		MockData() throws FileNotFoundException {
			this.locations = (Locations) readJsonFile("C:/repos/cs240/FamMap/server/src/main/Assets/locations.json", Locations.class);
			this.surNames = (Names) readJsonFile("C:/repos/cs240/FamMap/server/src/main/Assets/snames.json", Names.class);
			this.femNames = (Names) readJsonFile("C:/repos/cs240/FamMap/server/src/main/Assets/fnames.json", Names.class);
			this.masNames = (Names) readJsonFile("C:/repos/cs240/FamMap/server/src/main/Assets/mnames.json", Names.class);
		}

		private Object readJsonFile(String path, Class classType) throws FileNotFoundException {
			return new Gson().fromJson(new FileReader(new File(path)), classType);
		}
	}

	private class Names {
		private String[] data;

		public Names(String[] data) {
			this.data = data;
		}

		public String[] getData() {
			return data;
		}

		public void setData(String[] data) {
			this.data = data;
		}
	}

	private class Locations {

		private Location[] data;

		public Locations(Location[] data) {
			this.data = data;
		}

		public Location[] getData() {
			return data;
		}

		public void setData(Location[] data) {
			this.data = data;
		}
	}

	private class Location {
		String country;
		String city;
		float latitude;
		float longitude;

		public Location(String country, String city, float latitude, float longitude) {
			this.country = country;
			this.city = city;
			this.latitude = latitude;
			this.longitude = longitude;
		}
	}
}
