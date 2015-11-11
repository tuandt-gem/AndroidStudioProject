package com.adapter;

public class Person {

	private String name;
	private String age;
	private String city;

	public Person(String name) {
		this.name = name;
	}

	public void setAge(String string) {
		this.age = string;
	}

	public void setCity(String string) {
		this.city = string;
	}

	public String toString() {
		return this.name + " " + this.age + " " + this.city;
	}
}
