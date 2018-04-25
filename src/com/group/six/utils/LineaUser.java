package com.group.six.utils;

public class LineaUser {

	String userKey;
	String userAge;
	String sex;

	public LineaUser(String userKey, String userAge, String sex) {
		super();
		this.userKey = userKey;
		this.userAge = userAge;
		this.sex = sex;
	}

	public String getUserKey() {
		return userKey;
	}

	public String getUserAge() {
		return userAge;
	}

	public String getSex() {
		return sex;
	}

}
