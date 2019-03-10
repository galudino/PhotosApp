/**
 * User.java
 *
 * Copyright (c) 2019 Gemuele Aludino, Patrick Nogaj. 
 * All rights reserved.
 *
 * Rutgers University: School of Arts and Sciences
 * 01:198:213 Software Methodology, Spring 2019
 * Professor Seshadri Venugopal
 */

package model;

import java.io.Serializable;

public class User implements Comparable<User>, Serializable {

	private String username;
	
	public String getUsername() {
		return username;
	}
	
	public String toString() {
		return username;
	}

	@Override
	public int compareTo(User user) {
		return this.username.compareToIgnoreCase(user.username);
	}
	
}
