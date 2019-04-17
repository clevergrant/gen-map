package com.clevergrant.model;

import java.util.TreeMap;

public class Person {
	private String personID;
	private String descendant;
	private String firstName;
	private String lastName;
	private String gender;
	private String father;
	private String mother;
	private String spouse;

	private TreeMap<Integer, String> events = new TreeMap<>();

	/**
	 * Creates a new Person Object
	 *
	 * @param personID   Unique identifier for this person
	 * @param descendant User (userName) to which this person belongs
	 * @param firstName  Person's first name
	 * @param lastName   Person's last name
	 * @param gender     Person's gender (string: "f" or "m")
	 * @param father     ID of person’s father (possibly null)
	 * @param mother     ID of person’s mother (possibly null)
	 * @param spouse     ID of person’s spouse (possibly null)
	 */
	public Person(String personID, String descendant, String firstName, String lastName, String gender, String father, String mother, String spouse) {
		setPersonID(personID);
		setDescendant(descendant);
		setFirstName(firstName);
		setLastName(lastName);
		setGender(gender);
		setFather(father);
		setMother(mother);
		setSpouse(spouse);
	}

	public Person(Person p) {
		setPersonID(p.getPersonID());
		setDescendant(p.getDescendant());
		setFirstName(p.getFirstName());
		setLastName(p.getLastName());
		setGender(p.getGender());
		setFather(p.getFather());
		setMother(p.getMother());
		setSpouse(p.getSpouse());
		events = new TreeMap<>();
	}

	public String getPersonID() {
		return personID;
	}

	private void setPersonID(String personID) {
		this.personID = personID;
	}

	public String getDescendant() {
		return descendant;
	}

	private void setDescendant(String descendant) {
		this.descendant = descendant;
	}

	public String getFirstName() {
		return firstName;
	}

	private void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	private void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	private void setGender(String gender) {
		this.gender = gender;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public String getMother() {
		return mother;
	}

	public void setMother(String mother) {
		this.mother = mother;
	}

	public String getSpouse() {
		return spouse;
	}

	private void setSpouse(String spouse) {
		this.spouse = spouse;
	}

	public boolean isMarried() {
		return this.getSpouse() != null;
	}

	public TreeMap<Integer, String> getEvents() {
		return events;
	}

	public void addEvent(int year, String eventID) {
		this.events.put(year, eventID);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o instanceof Person) {
			Person oPerson = (Person) o;
			boolean nullFathers = oPerson.getFather() == null && getFather() == null;
			boolean nullMothers = oPerson.getMother() == null && getMother() == null;
			boolean nullSpouses = oPerson.getSpouse() == null && getSpouse() == null;

			return oPerson.getPersonID().equals(getPersonID()) &&
					oPerson.getDescendant().equals(getDescendant()) &&
					oPerson.getFirstName().equals(getFirstName()) &&
					oPerson.getLastName().equals(getLastName()) &&
					oPerson.getGender().equals(getGender()) &&
					(nullFathers || oPerson.getFather().equals(getFather())) &&
					(nullMothers || oPerson.getMother().equals(getMother())) &&
					(nullSpouses || oPerson.getSpouse().equals(getSpouse()));
		}
		return false;
	}
}
